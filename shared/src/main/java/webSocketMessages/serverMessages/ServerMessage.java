package webSocketMessages.serverMessages;

import model.GameData;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    GameData game = null;
    String errorMessage = null;
    String message = null;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public void setServerMessageValue(GameData game){
        this.game = game;
    }
    public void setServerMessageValue(String message){
        if(this.serverMessageType == ServerMessageType.ERROR){
            this.errorMessage = message;
        } else {
            this.message = message;
        }
    }

    public GameData getGame(){
        return game;
    }
    public String getMessage(){
        if(this.serverMessageType == ServerMessageType.ERROR){
            return errorMessage;
        } else {
            return message;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
