package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class KnightMovesCalculator extends PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        // List of all possible moves
        HashMap<Integer,int[]> options = new HashMap<>();
        options.put(-2, new int[]{-1, 1});
        options.put(-1, new int[]{-2, 2});
        options.put(1, new int[]{-2, 2});
        options.put(2, new int[]{-1, 1});

        // Loops through every recorded option
        for(int x : options.keySet()){
            for(int y : options.get(x)){
                ChessPosition newPos = new ChessPosition(myRow + x, myCol + y);
                if(validMove(board,myPosition,newPos)){
                    moves.add(new ChessMove(myPosition,newPos,null));
                }
            }
        }

        return moves;
    }


}
