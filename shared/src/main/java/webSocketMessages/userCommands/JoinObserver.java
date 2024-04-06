package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {

    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }

}
