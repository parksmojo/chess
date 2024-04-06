package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


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
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> join(message, session);
            case JOIN_OBSERVER -> observe(message, session);
            case MAKE_MOVE -> System.out.println("Working on it");
            case LEAVE -> System.out.println("Not ready yet");
            case RESIGN -> System.out.println("Code not finished");
        }
    }

    private void join(String message, Session session) throws IOException {
        JoinPlayer command = new Gson().fromJson(message, JoinPlayer.class);
        String username = userService.getUserName(command.getAuthString());
        int gameID = command.getGameID();
        ChessGame.TeamColor team = command.getPlayerColor();
        connectionManager.addSessionToGame(gameID, username, session);

        ChessGame game = gameService.findGame(gameID).game();
        ServerMessage loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadMessage.SetServerMessageValue(game);
        sendMessage(gameID,loadMessage, username);

        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.SetServerMessageValue(String.format("%s joined the game as %s", username, team));
        broadcastMessage(gameID,notification, username);
    }

    private void observe(String message, Session session) throws IOException {
        JoinObserver command = new Gson().fromJson(message, JoinObserver.class);
        String username = userService.getUserName(command.getAuthString());
        int gameID = command.getGameID();
        connectionManager.addSessionToGame(gameID, username, session);

        ChessGame game = gameService.findGame(gameID).game();
        ServerMessage loadMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadMessage.SetServerMessageValue(game);
        sendMessage(gameID,loadMessage, username);

        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.SetServerMessageValue(String.format("%s joined the game as observer", username));
        broadcastMessage(gameID,notification, username);
    }

    private void sendMessage(int gameID, ServerMessage message, String username) throws IOException {
        String jsonMessage = new Gson().toJson(message);
        Map<String, Connection> sessions = connectionManager.getSessionsForGame(gameID);
        Connection connection = sessions.get(username);
        connection.send(jsonMessage);
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
}