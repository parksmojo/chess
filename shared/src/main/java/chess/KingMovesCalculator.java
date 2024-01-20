package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int pieceRow = myPosition.getRow();
        int pieceCol = myPosition.getColumn();

        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <= 1; y++){
                // Staying in place is not an option
                if(x == 0 && y == 0){continue;}

                moves.add(new ChessMove(myPosition,new ChessPosition(pieceRow + x, pieceCol + y),null));
            }
        }

//        moves.add(new ChessMove(new ChessPosition()));

        return moves;
    }
}
