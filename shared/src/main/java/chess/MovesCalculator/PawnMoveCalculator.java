package chess.MovesCalculator;

import chess.*;

import java.util.ArrayList;

public class PawnMoveCalculator {

    public static void Pawn_Move_Calculator(ChessBoard board, ChessPosition myPosition,
                                            ArrayList<ChessMove> moves, ChessGame.TeamColor teamColor) {
        int startRow = myPosition.getRow() - 1;
        int startColumn = myPosition.getColumn() - 1;
        chess.ChessPiece.PieceType[] promotions = new ChessPiece.PieceType[]{ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN};
        //first move check
        if (startRow == 1 && teamColor == ChessGame.TeamColor.WHITE) {
            if (board.array[startRow + 1][startColumn] == null) {
                ChessPosition newPosition = new ChessPosition(startRow + 2, startColumn + 1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
            if (board.array[startRow + 1][startColumn] != null) {
                return;
            }
            if (board.array[startRow + 2][startColumn] == null) {
                ChessPosition newPosition = new ChessPosition(startRow + 3, startColumn + 1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
            return;
        }
        if (startRow == 6 && teamColor == ChessGame.TeamColor.BLACK) {
            if (board.array[startRow - 1][startColumn] == null) {
                ChessPosition newPosition = new ChessPosition(startRow, startColumn + 1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
            if (board.array[startRow - 1][startColumn] != null) {
                return;
            }
            if (board.array[startRow - 2][startColumn] == null) {
                ChessPosition newPosition = new ChessPosition(startRow - 1, startColumn + 1);
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
            return;
        }

        //normal move
        if ((startRow >= 2 && startRow <= 5 && teamColor == ChessGame.TeamColor.WHITE)) {
            MoveCalcHelper.Move_Calc_Helper_Pawn(startRow + 1, startColumn, board, myPosition, teamColor, moves);
        }
        if ((startRow >= 2 && startRow <= 5 && teamColor == ChessGame.TeamColor.BLACK)) {
            MoveCalcHelper.Move_Calc_Helper_Pawn(startRow - 1, startColumn, board, myPosition, teamColor, moves);
        }

        //Capture Piece
        //right side white

        //left side white

        //left side black

        //right side black

        //Capture promotion

        //promotion
        if (startRow + 1 == 7 && teamColor == ChessGame.TeamColor.WHITE) {
            ChessPosition newPosition = new ChessPosition(startRow + 2, startColumn + 1);
            for (int i = 0; i < 4; i++) {
                moves.add(new ChessMove(myPosition, newPosition, promotions[i]));
            }
        }
        if (startRow - 1 == 0 && teamColor == ChessGame.TeamColor.BLACK) {
            ChessPosition newPosition = new ChessPosition(startRow, startColumn + 1);
            for (int i = 0; i < 4; i++) {
                moves.add(new ChessMove(myPosition, newPosition, promotions[i]));
            }
        }
    }
}
