package chess;

import java.util.ArrayList;

public interface PieceMovesCalculator {

    public ArrayList<ChessMove> moves = new ArrayList<>();

    public static ArrayList<ChessMove> KingMoveCalculator(ChessBoard chessBoard, ChessPosition startPosition) {
        int startCol = startPosition.getColumn();
        int startRow = startPosition.getRow();
        //up one row
        if (chessBoard.array[startRow + 1][startCol] == null && startRow + 1 < chessBoard.array.length) {
            ChessPosition endPosition = new ChessPosition(startRow + 1, startCol);
            moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KING));
        }
        //down one row
        if (chessBoard.array[startRow - 1][startCol] == null && startRow - 1 > 0) {
            ChessPosition endPosition = new ChessPosition(startRow - 1, startCol);
            moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KING));
        }
        //one to the right
        if (chessBoard.array[startRow][startCol + 1] == null && startCol + 1 < 8) {
            ChessPosition endPosition = new ChessPosition(startRow, startCol + 1);
            moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KING));
        }
        //one to the left
        if (chessBoard.array[startRow][startCol - 1] == null && startCol - 1 > 0) {
            ChessPosition endPosition = new ChessPosition(startRow, startCol - 1);
            moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KING));
        }
        //one up and one to the left
        if (chessBoard.array[startRow + 1][startCol - 1] == null && startCol - 1 > 0
                && startRow + 1 < chessBoard.array.length) {
            ChessPosition endPosition = new ChessPosition(startRow + 1, startCol - 1);
            moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KING));
        }
        //todo
        //one up and one to the right
        if (chessBoard.array[startRow][startCol - 1] == null && startCol - 1 > 0) {
            ChessPosition endPosition = new ChessPosition(startRow, startCol - 1);
            moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KING));
        }
        //todo
        //one down and one to the left
        if (chessBoard.array[startRow][startCol - 1] == null && startCol - 1 > 0) {
            ChessPosition endPosition = new ChessPosition(startRow, startCol - 1);
            moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KING));
        }
        //todo
        //one down and one to the right
        if (chessBoard.array[startRow][startCol - 1] == null && startCol - 1 > 0) {
            ChessPosition endPosition = new ChessPosition(startRow, startCol - 1);
            moves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KING));
        }
        return moves;
    }

    public static ArrayList<ChessMove> QueenMoveCalculator(ChessBoard chessBoard, ChessPosition startPosition) {
        return moves;
    }

    public static ArrayList<ChessMove> BishopMoveCalculator(ChessBoard chessBoard, ChessPosition startPosition) {
        return moves;
    }

    public static ArrayList<ChessMove> KnightMoveCalculator(ChessBoard chessBoard, ChessPosition startPosition) {
        return moves;
    }

    public static ArrayList<ChessMove> RookMoveCalculator(ChessBoard chessBoard, ChessPosition startPosition) {
        return moves;
    }

    public static ArrayList<ChessMove> PawnMoveCalculator(ChessBoard chessBoard, ChessPosition startPosition) {
        return moves;
    }
}
