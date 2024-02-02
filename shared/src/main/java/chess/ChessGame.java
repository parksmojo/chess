package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private TeamColor TeamTurn = TeamColor.WHITE;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return TeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        TeamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(startPosition == null){
            return null;
        }
        Collection<ChessMove> possibleMoves = board.getPiece(startPosition).pieceMoves(board,startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();
        //TeamColor team = board.getPiece(startPosition).getTeamColor();
        for(ChessMove move : possibleMoves){
            if(tryMove(move.getStartPosition(), move.getEndPosition())){
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    private boolean tryMove(ChessPosition startPosition, ChessPosition endPosition){
        ChessPiece piece = board.getPiece(startPosition);
        TeamColor team = piece.getTeamColor();
        ChessPiece replacedPiece = board.getPiece(endPosition);

        // move piece
        board.addPiece(startPosition, null);
        board.addPiece(endPosition, piece);

        // check status
        boolean isInTrouble = isInCheck(team);

        // move pieces back
        board.addPiece(startPosition, piece);
        board.addPiece(endPosition, replacedPiece);

        // return true if move did not result in check
        return !isInTrouble;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = findKing(teamColor);

        for(int i = 1; i <= 8; i++){
            for(int j = 1; j<= 8; j++){
                ChessPosition piecePos = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(piecePos);
                if(piece == null || piece.getTeamColor() == teamColor){
                    continue;
                }
                if(containsPos(piece.pieceMoves(board,piecePos), kingPos)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean containsPos(Collection<ChessMove> enemyMoves, ChessPosition kingPos){
        for(ChessMove possibleMove : enemyMoves){
            if(possibleMove.getEndPosition().equals(kingPos)){
                return true;
            }
        }
        return false;
    }

    private ChessPosition findKing(TeamColor teamColor){
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j<= 8; j++){
                ChessPosition pos = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(pos);
                if(piece == null){
                    continue;
                }
                if(piece.getPieceType().equals(ChessPiece.PieceType.KING) && piece.getTeamColor().equals(teamColor)){
                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
