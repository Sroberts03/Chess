package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessPiece.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessPiece[][] array;

    public ChessBoard() {
        this.array = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int column = position.getColumn();
        int row = position.getRow();
        array[row-1][column-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int column = position.getColumn();
        int row = position.getRow();
        return array[row-1][column-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 3; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                addPiece(new ChessPosition(i+1, j+1), null);
            }
        }
        resetBoardHelper(pieceOrderWhite(), pieceOrderBlack());
    }

    private void resetBoardHelper(ChessPiece[] listOfWhite,ChessPiece[] listOfBlack) {
        for (int i = 0; i < 8; i++) {
            addPiece(new ChessPosition(1, i+1), listOfWhite[i]);
            addPiece(new ChessPosition(8, i+1), listOfBlack[i]);
        }
        for (int i = 0; i < 8; i++) {
            addPiece(new ChessPosition(2, i+1), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.PAWN));
            addPiece(new ChessPosition(7, i+1), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.PAWN));
        }
    }

    private ChessPiece[] pieceOrderWhite() {
        return new ChessPiece[]{new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.ROOK),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.KNIGHT),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.BISHOP),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.QUEEN),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.KING),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.BISHOP),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.KNIGHT),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.ROOK)};
    }

    private ChessPiece[] pieceOrderBlack() {
        return new ChessPiece[]{new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.ROOK),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.KNIGHT),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.BISHOP),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.QUEEN),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.KING),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.BISHOP),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.KNIGHT),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.ROOK)};
    }

    @Override
    public boolean equals(Object o) {
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(array, that.array);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(array);
    }

    @Override
    public String toString() {
        String boardString = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (array[i][j] == null) {
                    boardString += " null " + "| ";
                }
                else {
                    boardString += array[i][j].toString() + " | ";
                }
                if (j == 7) {
                    boardString += "\n";
                }
            }
        }
        return boardString;
    }
}