package chess;

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
        array = new ChessPiece[8][8];
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
        array[row][column] = piece;
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
        return array[row][column];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 3; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                array[i][j] = null;
            }
        }
        ResetBoardHelper();
    }

    public void ResetBoardHelper() {
        ChessPiece[] PieceOrderWhite = {new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.ROOK),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.KNIGHT),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.BISHOP),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.QUEEN),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.KING),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.BISHOP),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.KNIGHT),
                new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.ROOK)};
        ChessPiece[] PieceOrderBlack = {new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.ROOK),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.KNIGHT),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.BISHOP),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.QUEEN),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.KING),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.BISHOP),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.KNIGHT),
                new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.ROOK)};
        for (int i = 0; i < 8; i++) {
            array[0][i] = PieceOrderWhite[i];
            array[7][i] = PieceOrderBlack[i];
        }
        for (int i = 0; i < 8; i++) {
            array[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.PAWN);
            array[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.PAWN);
        }
    }
}
