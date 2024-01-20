package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int pieceRow = myPosition.getRow();
        int pieceCol = myPosition.getColumn();
        ChessGame.TeamColor myTeam = board.getPiece(myPosition).getTeamColor();

        // Loops through every square immediately around the piece
        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <= 1; y++){
                int newRow = pieceRow + x;
                int newCol = pieceCol + y;
                // Staying in place is not an option
                if(x == 0 && y == 0){continue;}
                // Leaving the board is not an option
                if(newRow > 8 || newRow < 1 || newCol > 8 || newCol < 1){continue;}
                // Checking to see if there's another piece in the way
                if(board.getPiece(new ChessPosition(newRow,newCol)) != null){
                    // Is that piece on my team?
                    if(board.getPiece(new ChessPosition(newRow,newCol)).getTeamColor().equals(myTeam)){
                        continue;
                    }
                }
                moves.add(new ChessMove(myPosition,new ChessPosition(newRow, newCol),null));
            }
        }
        return moves;
    }
}
