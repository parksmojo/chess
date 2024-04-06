package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {

    public Leave(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

}
