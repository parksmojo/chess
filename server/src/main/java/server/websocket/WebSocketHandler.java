package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {
    private final GameService gameService = new GameService();
    private final UserService userService = new UserService();
    private final ConnectionManager connectionManager = new ConnectionManager();

    @OnWebSocketError
    public void onError(Throwable throwable){
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.printf("Received WS message: %s\n",message);
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        if(!checkAuth(command.getAuthString())){
            sendError(session, "Unauthorized");
            return;
        }
        int gameID = command.getGameID();
        if(gameService.findGame(gameID) == null){
            sendError(session, "Unavailable gameID");
            return;
        }

        switch (command.getCommandType()) {
            case JOIN_PLAYER -> join(message, session);
            case JOIN_OBSERVER -> observe(message, session);
            case MAKE_MOVE -> makeMove(message);
            case LEAVE -> leaveGame(message, session);
            case RESIGN -> resign(message);
        }
    }

    // Player Actions
    private void makeMove(String message) throws IOException {
        MakeMove command = new Gson().fromJson(message, MakeMove.class);
        String username = userService.getUserName(command.getAuthString());
        GameData game = gameService.findGame(command.getGameID());
        ChessGame.TeamColor userTeam = getUserTeam(username,game);
        ChessGame.TeamColor pieceTeam = game.game().getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor();
        ChessGame.TeamColor gameTeam = game.game().getTeamTurn();
        try {
            if(userTeam != pieceTeam || userTeam != gameTeam){
                throw new InvalidMoveException("Incorrect team");
            }
            game.game().makeMove(command.getMove());
        } catch (InvalidMoveException e) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            error.setServerMessageValue(e.getMessage());
            sendMessage(game.gameID(), error, username);
            return;
        }

        gameService.setGame(game);
        ServerMessage loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadMessage.setServerMessageValue(game);

        ServerMessage notif = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notif.setServerMessageValue(String.format("%s moved ...",username));

        broadcastMessage(game.gameID(), loadMessage, null);
        broadcastMessage(game.gameID(), notif, username);
    }
    private ChessGame.TeamColor getUserTeam(String username, GameData game){
        if(username.equals(game.whiteUsername())){
            return ChessGame.TeamColor.WHITE;
        } else if(username.equals(game.blackUsername())){
            return ChessGame.TeamColor.BLACK;
        } else {
            return null;
        }
    }

    private void join(String message, Session session) throws IOException {
        JoinPlayer command = new Gson().fromJson(message, JoinPlayer.class);
        String username = userService.getUserName(command.getAuthString());
        int gameID = command.getGameID();
        ChessGame.TeamColor team = command.getPlayerColor();

        GameData game = gameService.findGame(gameID);
        if((team == ChessGame.TeamColor.WHITE && !Objects.equals(game.whiteUsername(), username))
                || (team == ChessGame.TeamColor.BLACK && !Objects.equals(game.blackUsername(), username))) {
            sendError(session, "Spot already taken");
        } else {
            connectionManager.addSessionToGame(gameID, username, session);
            joinMessages(gameID, username, team);
        }
    }

    private void observe(String message, Session session) throws IOException {
        JoinObserver command = new Gson().fromJson(message, JoinObserver.class);
        String username = userService.getUserName(command.getAuthString());
        int gameID = command.getGameID();
        connectionManager.addSessionToGame(gameID, username, session);
        joinMessages(gameID, username, null);
    }

    private void leaveGame(String message, Session session) throws IOException {
        Leave command = new Gson().fromJson(message, Leave.class);
        String username = userService.getUserName(command.getAuthString());
        int gameID = command.getGameID();
        ServerMessage leaveMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        leaveMessage.setServerMessageValue(String.format("%s left the game",username));
        broadcastMessage(gameID,leaveMessage,username);
        connectionManager.removeSessionFromGame(gameID,username,session);
    }

    private void resign(String message) throws IOException {
        Resign command = new Gson().fromJson(message, Resign.class);
        String username = userService.getUserName(command.getAuthString());
        int gameID = command.getGameID();
        GameData game = gameService.findGame(command.getGameID());
        ChessGame.TeamColor userTeam = getUserTeam(username,game);
        ChessGame.TeamColor gameTeam = game.game().getTeamTurn();

        if(userTeam == null || gameTeam == ChessGame.TeamColor.NULL) {
            ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            error.setServerMessageValue("Cannot resign");
            sendMessage(game.gameID(), error, username);
            return;
        }

        game.game().setTeamTurn(ChessGame.TeamColor.NULL);
        gameService.setGame(game);

        ServerMessage resignMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        resignMessage.setServerMessageValue(String.format("%s resigned",username));
        broadcastMessage(gameID,resignMessage,null);
    }


    // Message Functions
    private void joinMessages(int gameID, String username, ChessGame.TeamColor team) throws IOException {
        String teamString = team == null ? "observer" : team.toString();
        GameData game = gameService.findGame(gameID);
        ServerMessage loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadMessage.setServerMessageValue(game);
        sendMessage(gameID,loadMessage, username);

        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.setServerMessageValue(String.format("%s joined the game as %s", username, teamString));
        broadcastMessage(gameID,notification, username);
    }

    private void sendMessage(int gameID, ServerMessage message, String username) throws IOException {
        String jsonMessage = new Gson().toJson(message);
        Map<String, Connection> sessions = connectionManager.getSessionsForGame(gameID);
        Connection connection = sessions.get(username);
        connection.send(jsonMessage);
    }

    private void sendError(Session session, String message) throws IOException {
        ServerMessage serverMsg = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        serverMsg.setServerMessageValue("Error: " + message);
        String jsonMessage = new Gson().toJson(serverMsg);

        session.getRemote().sendString(jsonMessage);
    }

    private void broadcastMessage(int gameID, ServerMessage message, String notUsername) throws IOException {
        String jsonMessage = new Gson().toJson(message);
        Map<String, Connection> connections = connectionManager.getSessionsForGame(gameID);
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.playerName.equals(notUsername)) {
                    c.send(jsonMessage);
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.playerName);
        }
    }

    // Helper functions
    boolean checkAuth(String auth){
        return userService.validateAuth(auth);
    }
}