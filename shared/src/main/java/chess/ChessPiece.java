package chess;

import chess.movesCalculators.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (board.getPiece(myPosition).getPieceType()) {
            case KING -> new KingMovesCalculator().pieceMoves(board, myPosition);
            case QUEEN -> new QueenMovesCalculator().pieceMoves(board, myPosition);
            case BISHOP -> new BishopMovesCalculator().pieceMoves(board, myPosition);
            case KNIGHT -> new KnightMovesCalculator().pieceMoves(board, myPosition);
            case ROOK -> new RookMovesCalculator().pieceMoves(board, myPosition);
            case PAWN -> new PawnMovesCalculator().pieceMoves(board, myPosition);
        };
    }

    @Override
    public String toString() {
        String pieceChar = null;

        if(color == ChessGame.TeamColor.WHITE){
            switch (type){
                case KING -> pieceChar = "♚";
                case QUEEN -> pieceChar = "♛";
                case BISHOP -> pieceChar = "♝";
                case KNIGHT -> pieceChar = "♞";
                case ROOK -> pieceChar = "♜";
                case PAWN -> pieceChar = "♟";
            }
        } else {
            switch (type){
                case KING -> pieceChar = "♔";
                case QUEEN -> pieceChar = "♕";
                case BISHOP -> pieceChar = "♗";
                case KNIGHT -> pieceChar = "♘";
                case ROOK -> pieceChar = "♖";
                case PAWN -> pieceChar = "♙";
            }
        }

        return pieceChar;
    }

    @Override
    public boolean equals(Object obj) {
        // Checks basic differences
        if(obj == null) {return false;}
        if(obj == this) {return true;}
        if(obj.getClass() != obj.getClass()) {return false;}

        ChessPiece other = (ChessPiece) obj;
        return((this.color == other.getTeamColor()) && (this.type == other.getPieceType()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}
