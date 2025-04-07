package chess;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    ChessPosition startPosition;
    ChessPosition endPosition;
    ChessPiece.PieceType promotion;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotion = promotionPiece;
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
        return promotion;
    }

    private Map<Integer, String> columnLetter() {
        Map<Integer,String> columnLetter = new HashMap<>();
        columnLetter.put(1, "a");
        columnLetter.put(2, "b");
        columnLetter.put(3, "c");
        columnLetter.put(4, "d");
        columnLetter.put(5, "e");
        columnLetter.put(6, "f");
        columnLetter.put(7, "g");
        columnLetter.put(8, "h");
        return columnLetter;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startPosition, chessMove.startPosition)
                && Objects.equals(endPosition, chessMove.endPosition)
                && promotion == chessMove.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotion);
    }

    @Override
    public String toString() {
        Map<Integer, String> columnLetter = columnLetter();
        return columnLetter.get(startPosition.getColumn()) + startPosition.getRow()
                + " -> " + columnLetter.get(endPosition.getColumn()) + endPosition.getRow();
    }
}
