package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public int hashCode() {
        return getRow() * 2 + getColumn() * 3;
    }

    @Override
    public boolean equals(Object obj) {
        // Checks basic differences
        if(obj == null) {return false;}
        if(obj == this) {return true;}
        if(obj.getClass() != obj.getClass()) {return false;}

        ChessPosition other = (ChessPosition)obj;
        return((this.getRow() == other.getRow()) && (this.getColumn() == other.getColumn()));
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }
}
