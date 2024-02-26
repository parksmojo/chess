package chess.movesCalculators;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

abstract class PieceMoveCalculator {
    abstract public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
    boolean isValidMove(ChessBoard board, ChessPosition myPosition, ChessPosition newPos){
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

    void recursiveSearch(ChessBoard board, HashSet<ChessMove> moves, ChessPosition piecePos, ChessPosition myPos, int x, int y){
        ChessPosition next = new ChessPosition(myPos.getRow() + x, myPos.getColumn() + y);
        if(isValidMove(board, piecePos, next) && !isCapturing(board, piecePos, myPos)) {
            recursiveSearch(board, moves, piecePos, next, x, y);
            moves.add(new ChessMove(piecePos, next, null));
        }
    }

    private boolean isCapturing(ChessBoard board, ChessPosition piecePos, ChessPosition checkPos){
        return !checkPos.equals(piecePos) && board.getPiece(checkPos) != null;
    }
}
