package chess;

import java.util.ArrayList;

public interface PieceMovesCalculator {

    public static void KingMoveHelper(int endRow, int endCol, ChessBoard chessBoard, ChessPosition startPosition, ChessGame.TeamColor teamColor, ArrayList<ChessMove> moves) {
        if (((endRow <= 7 && endRow >= 0 ) && (endCol <= 7 && endCol >= 0))
                && (chessBoard.array[endRow][endCol] == null
                || chessBoard.array[endRow][endCol].getTeamColor() != teamColor)) {
            ChessPosition newPosition = new ChessPosition(endRow + 1, endCol + 1);
            moves.add(new ChessMove(startPosition, newPosition, ChessPiece.PieceType.KING));
        }
    }

    public static ArrayList<ChessMove> QueenMoveCalculator(ChessBoard chessBoard, ChessPosition startPosition, ChessGame.TeamColor teamColor, ArrayList<ChessMove> moves) {
        int startCol = startPosition.getColumn();
        int startRow = startPosition.getRow();
        return moves;
    }

    public static ArrayList<ChessMove> BishopMoveCalculator(ChessBoard chessBoard, ChessPosition startPosition, ChessGame.TeamColor teamColor, ArrayList<ChessMove> moves) {
        int startCol = startPosition.getColumn();
        int startRow = startPosition.getRow();
        return moves;
    }

    public static ArrayList<ChessMove> KnightMoveCalculator(ChessBoard chessBoard, ChessPosition startPosition, ChessGame.TeamColor teamColor, ArrayList<ChessMove> moves) {
        int startCol = startPosition.getColumn();
        int startRow = startPosition.getRow();
        return moves;
    }

    public static ArrayList<ChessMove> RookMoveCalculator(ChessBoard chessBoard, ChessPosition startPosition, ChessGame.TeamColor teamColor, ArrayList<ChessMove> moves) {
        int startCol = startPosition.getColumn();
        int startRow = startPosition.getRow();
        return moves;
    }

    public static ArrayList<ChessMove> PawnMoveCalculator(ChessBoard chessBoard, ChessPosition startPosition, ChessGame.TeamColor teamColor, ArrayList<ChessMove> moves) {
        int startCol = startPosition.getColumn();
        int startRow = startPosition.getRow();
        if (startRow == 2 &&  teamColor == ChessGame.TeamColor.WHITE) {
            ChessMove move_one = new ChessMove(startPosition, new ChessPosition(startRow + 1, startCol), ChessPiece.PieceType.PAWN);
            ChessMove move_two = new ChessMove(startPosition, new ChessPosition(startRow + 2, startCol), ChessPiece.PieceType.PAWN);
            moves.add(move_one);
            moves.add(move_two);
        }
        else if (startRow == 7 &&  teamColor == ChessGame.TeamColor.BLACK) {
            ChessMove move_one = new ChessMove(startPosition, new ChessPosition(startRow - 1, startCol), ChessPiece.PieceType.PAWN);
            ChessMove move_two = new ChessMove(startPosition, new ChessPosition(startRow - 2, startCol), ChessPiece.PieceType.PAWN);
            moves.add(move_one);
            moves.add(move_two);
        }
        return moves;
    }
}
