package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator extends PieceMoveCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();

        f0(board, moves, myPosition, myPosition, 1, 0);
        f0(board, moves, myPosition, myPosition, 0, 1);
        f0(board, moves, myPosition, myPosition, -1, 0);
        f0(board, moves, myPosition, myPosition, 0, -1);

        return moves;
    }

    private void f0(ChessBoard board, HashSet<ChessMove> moves, ChessPosition piecePos, ChessPosition myPos, int x, int y){
        ChessPosition next = new ChessPosition(myPos.getRow() + x, myPos.getColumn() + y);
        if(validMove(board, piecePos, next) && !alreadyCaptured(board, piecePos, myPos)) {
            f0(board, moves, piecePos, next, x, y);
            moves.add(new ChessMove(piecePos, next, null));
        }
    }

    private boolean alreadyCaptured(ChessBoard board, ChessPosition piecePos, ChessPosition checkPos){
        return !checkPos.equals(piecePos) && board.getPiece(checkPos) != null;
    }
}
