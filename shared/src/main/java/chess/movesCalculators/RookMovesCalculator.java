package chess.movesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator extends PieceMoveCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();

        recursiveSearch(board, moves, myPosition, myPosition, 1, 0);
        recursiveSearch(board, moves, myPosition, myPosition, 0, 1);
        recursiveSearch(board, moves, myPosition, myPosition, -1, 0);
        recursiveSearch(board, moves, myPosition, myPosition, 0, -1);

        return moves;
    }
}
