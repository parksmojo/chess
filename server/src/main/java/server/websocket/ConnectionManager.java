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

//    public void broadcast(String excludePlayerName, ServerMessage message) throws IOException {
//        var removeList = new ArrayList<Connection>();
//        for (var c : sessionMap.values()) {
//            if (c.session.isOpen()) {
//                if (!c.playerName.equals(excludePlayerName)) {
//                    c.send(message.toString());
//                }
//            } else {
//                removeList.add(c);
//            }
//        }
//
//        // Clean up any connections that were left open.
//        for (var c : removeList) {
//            sessionMap.remove(c.username);
//        }
//    }
}
