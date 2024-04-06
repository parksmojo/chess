package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConnectionManager {
    public Map<Integer, Map<String, Connection>> sessionMap = new HashMap<>();

    public void addSessionToGame(int gameID, String username, Session session) {
        var connection = new Connection(username, session);
        if(!sessionMap.containsKey(gameID)){
            sessionMap.put(gameID, new HashMap<>());
        }
        sessionMap.get(gameID).put(username, connection);
    }

    public void removeSessionFromGame(int gameID, String username, Session session) {
        sessionMap.get(gameID).remove(username);
    }

    public Map<String, Connection> getSessionsForGame(int gameID){
        return sessionMap.get(gameID);
    }
}
