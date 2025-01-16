package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class PawnMoveCalculator {

    public static void Pawn_Move_Calculator(ChessBoard board, ChessPosition myPosition,
                                            ArrayList<ChessMove> moves, ChessGame.TeamColor teamColor) {
        int startRow = myPosition.getRow() - 1;
        int startColumn = myPosition.getColumn() - 1;
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
        }
        //normal move
        //promotion
    }
}
