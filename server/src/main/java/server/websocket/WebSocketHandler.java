package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;



@WebSocket
public class WebSocketHandler {
    private final GameService gameService = new GameService();
    private final UserService userService = new UserService();
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketError
    public void onError(Throwable throwable){
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        System.out.printf("Received WS message: %s\n",message);
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> join(message, session);
            case JOIN_OBSERVER -> observe(message, session);
        }
    }

    private void join(String message, Session session){
        JoinPlayer command = new Gson().fromJson(message, JoinPlayer.class);
        String username = userService.getUserName(command.getAuthString());
        int gameID = command.getGameID();
        ChessGame.TeamColor team = command.getPlayerColor();
        connections.addSessionToGame(gameID, username, session);
        System.out.println(connections.sessionMap);
    }

    private void observe(String message, Session session){
        JoinObserver command = new Gson().fromJson(message, JoinObserver.class);
        String username = userService.getUserName(command.getAuthString());
        int gameID = command.getGameID();
        connections.addSessionToGame(gameID, username, session);
        System.out.println(connections.sessionMap);
    }
}