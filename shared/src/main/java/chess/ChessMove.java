package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {


    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public String toString() {
        String promotion;
        switch (promotionPiece){
            case QUEEN -> promotion = "(Q)";
            case ROOK -> promotion = "(R)";
            case BISHOP -> promotion = "(B)";
            case KNIGHT -> promotion = "(K)";
            case null, default -> promotion = "";
        }
        return String.format("%s->%s %s", startPosition, endPosition, promotion);
        //return String.format("%s->%s", startPosition, endPosition);
    }

    @Override
    public boolean equals(Object obj) {
        // Checks basic differences
        if(obj == null) {return false;}
        if(obj == this) {return true;}
        if(obj.getClass() != obj.getClass()) {return false;}

        ChessMove other = (ChessMove) obj;
        return (startPosition.equals(other.getStartPosition())
                && endPosition.equals(other.getEndPosition())
                && (promotionPiece == other.getPromotionPiece()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }
}
