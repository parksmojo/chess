package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private final ChessMove move;
    private final String moveStr;

    public MakeMove(String authToken, int gameID, ChessMove move, String moveText) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
        moveStr = moveText;
    }

    public ChessMove getMove() {
        return move;
    }

    public String getMoveStr() {
        return moveStr;
    }
}
