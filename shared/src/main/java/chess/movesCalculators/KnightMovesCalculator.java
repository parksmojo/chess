package chess.movesCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition[] possible = new ChessPosition[8];
        possible[0] = new ChessPosition(row+1,col-2);
        possible[1] = new ChessPosition(row+1,col+2);
        possible[2] = new ChessPosition(row-1,col-2);
        possible[3] = new ChessPosition(row-1,col+2);
        possible[4] = new ChessPosition(row+2,col-1);
        possible[5] = new ChessPosition(row+2,col+1);
        possible[6] = new ChessPosition(row-2,col-1);
        possible[7] = new ChessPosition(row-2,col+1);

        for(int i = 0; i <= 7; i++){
            if(isValidMove(board,myPosition,possible[i])){
                moves.add(new ChessMove(myPosition,possible[i],null));
            }
        }

        return moves;
    }


}
