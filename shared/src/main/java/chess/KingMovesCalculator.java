package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator extends PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        // Loops through every square immediately around the piece
        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <= 1; y++){
                ChessPosition newPos = new ChessPosition(myRow + x, myCol + y);

                if(isValidMove(board,myPosition,newPos)){
                    moves.add(new ChessMove(myPosition,newPos,null));
                }

            }
        }

        return moves;
    }
}
