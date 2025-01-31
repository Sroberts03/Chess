package chess.moves.calculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class BishopAndQueenHelper {

    public static void bishopAndQueen(int startRow, int startCol, ChessBoard board,
                                        ChessPosition myPosition, ChessGame.TeamColor teamColor,
                                        ArrayList<ChessMove> moves) {
        //up and down movement
        for (int i = 1; i < 8; i++) {
            String returned = MoveCalcHelper.moveCalcHelper(startRow + i, startCol + i, board, myPosition, teamColor, moves);
            if (returned.equals("Stop")) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            String returned = MoveCalcHelper.moveCalcHelper(startRow - i, startCol - i, board, myPosition, teamColor, moves);
            if (returned.equals("Stop")) {
                break;
            }
        }
        //left and right movement
        for (int i = 1; i < 8; i++) {
            String returned = MoveCalcHelper.moveCalcHelper(startRow - i, startCol + i, board, myPosition, teamColor, moves);
            if (returned.equals("Stop")) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            String returned = MoveCalcHelper.moveCalcHelper(startRow + i, startCol - i, board, myPosition, teamColor, moves);
            if (returned.equals("Stop")) {
                break;
            }
        }
    }
}
