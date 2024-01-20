package chess;

import java.util.Collection;

abstract class PieceMoveCalculator {
    abstract public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
    boolean validMove(ChessBoard board, ChessPosition myPosition, ChessPosition newPos){
        ChessGame.TeamColor myTeam = board.getPiece(myPosition).getTeamColor();

        // Staying in place is not an option
        if(newPos.equals(myPosition)){
            return false;
        }

        // Leaving the board is not an option
        if(!newPos.inBounds()){
            return false;
        }

        // Taking another piece that is on my team is not an option
        ChessPiece otherPiece = board.getPiece(newPos);
        if(otherPiece != null && otherPiece.getTeamColor().equals(myTeam)){
            return false;
        }
        return true;
    }
}
