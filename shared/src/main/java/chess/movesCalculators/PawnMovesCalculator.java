package chess.movesCalculators;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator extends PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        boolean promote = false;
        ChessPiece.PieceType[] promotetype = null;
        boolean firstMove = false;

        if(board.getPiece(myPosition).getTeamColor().equals(ChessGame.TeamColor.WHITE)){
            if(myRow == 7) {
                promote = true;
                promotetype = new ChessPiece.PieceType[]{ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT};
            }
            else if (myRow == 2) firstMove = true;

            ChessPosition step = new ChessPosition(myRow + 1, myCol);
            ChessPosition jump = new ChessPosition(myRow + 2, myCol);
            if(isValidMove(board,myPosition,step) && board.getPiece(step) == null){
                if(promote){
                    for(ChessPiece.PieceType type : promotetype){
                        moves.add(new ChessMove(myPosition,step,type));
                    }
                }
                else{
                    moves.add(new ChessMove(myPosition,step,null));
                }

                if(firstMove && isValidMove(board,myPosition,jump) && board.getPiece(jump) == null){
                    moves.add(new ChessMove(myPosition,jump, null));
                }
            }

            ChessPosition[] diagonal = {new ChessPosition(myRow + 1, myCol - 1),new ChessPosition(myRow + 1, myCol + 1)};
            if(isValidMove(board,myPosition,diagonal[0]) && board.getPiece(diagonal[0]) != null){
                if(promote){
                    for(ChessPiece.PieceType type : promotetype){
                        moves.add(new ChessMove(myPosition,diagonal[0],type));
                    }
                }
                else{
                    moves.add(new ChessMove(myPosition,diagonal[0],null));
                }
            }
            if(isValidMove(board,myPosition,diagonal[1]) && board.getPiece(diagonal[1]) != null){
                if(promote){
                    for(ChessPiece.PieceType type : promotetype){
                        moves.add(new ChessMove(myPosition,diagonal[1],type));
                    }
                }
                else{
                    moves.add(new ChessMove(myPosition,diagonal[1],null));
                }
            }
        }
        else{

            if(myRow == 2) {
                promote = true;
                promotetype = new ChessPiece.PieceType[]{ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT};
            }
            else if (myRow == 7) firstMove = true;

            ChessPosition step = new ChessPosition(myRow - 1, myCol);
            ChessPosition jump = new ChessPosition(myRow - 2, myCol);
            if(isValidMove(board,myPosition,step) && board.getPiece(step) == null){
                if(promote){
                    for(ChessPiece.PieceType type : promotetype){
                        moves.add(new ChessMove(myPosition,step,type));
                    }
                }
                else{
                    moves.add(new ChessMove(myPosition,step,null));
                }

                if(firstMove && isValidMove(board,myPosition,jump) && board.getPiece(jump) == null){
                    moves.add(new ChessMove(myPosition,jump, null));
                }
            }

            ChessPosition[] diagonal = {new ChessPosition(myRow - 1, myCol - 1),new ChessPosition(myRow - 1, myCol + 1)};
            if(isValidMove(board,myPosition,diagonal[0]) && board.getPiece(diagonal[0]) != null){
                if(promote){
                    for(ChessPiece.PieceType type : promotetype){
                        moves.add(new ChessMove(myPosition,diagonal[0],type));
                    }
                }
                else{
                    moves.add(new ChessMove(myPosition,diagonal[0],null));
                }
            }
            if(isValidMove(board,myPosition,diagonal[1]) && board.getPiece(diagonal[1]) != null){
                if(promote){
                    for(ChessPiece.PieceType type : promotetype){
                        moves.add(new ChessMove(myPosition,diagonal[1],type));
                    }
                }
                else{
                    moves.add(new ChessMove(myPosition,diagonal[1],null));
                }
            }
        }

        return moves;
    }
}
