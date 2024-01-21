package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        // Taking account for the difference between the array index and the row and column names
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        // Taking account for the difference between the array index and the row and column names
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                squares[i][j] = null;
            }
        }

        // White pieces
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
        int row = 1;
        addPiece(new ChessPosition(row,1),new ChessPiece(color, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(row,2),new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(row,3),new ChessPiece(color, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(row,4),new ChessPiece(color, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(row,5),new ChessPiece(color, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(row,6),new ChessPiece(color, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(row,7),new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(row,8),new ChessPiece(color, ChessPiece.PieceType.ROOK));
        for(int i = 1; i < 9; i++){
            addPiece(new ChessPosition(2,i),new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }

        // Black pieces
        color = ChessGame.TeamColor.BLACK;
        row = 8;
        addPiece(new ChessPosition(row,1),new ChessPiece(color, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(row,2),new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(row,3),new ChessPiece(color, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(row,4),new ChessPiece(color, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(row,5),new ChessPiece(color, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(row,6),new ChessPiece(color, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(row,7),new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(row,8),new ChessPiece(color, ChessPiece.PieceType.ROOK));
        for(int i = 1; i < 9; i++){
            addPiece(new ChessPosition(7,i),new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(squares[i][j] != null){
                    hash += i * j * squares[i][j].hashCode();
                }
                else {
                    hash += 1;
                }
            }
        }
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("ChessBoard{");
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition pos = new ChessPosition(i, j);
                if(getPiece(pos) == null) continue;
                output.append(getPiece(pos)).append(",");
            }
        }
        return output.toString();
    }
}
