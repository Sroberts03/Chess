package chess.moves.calculator;

import chess.*;

import java.util.ArrayList;

public class RookAndQueenHelper {

    public static void rookAndQueen(int startRow, int startCol,
                                      ChessBoard board, ChessPosition myPosition,
                                      ChessGame.TeamColor teamColor, ArrayList<ChessMove> moves) {
        //up and down movement
        for (int i = 1; i < 8; i++) {
            String returned = MoveCalcHelper.moveCalcHelper(startRow + i, startCol, board, myPosition, teamColor, moves);
            if (returned.equals("Stop")) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            String returned = MoveCalcHelper.moveCalcHelper(startRow - i, startCol, board, myPosition, teamColor, moves);
            if (returned.equals("Stop")) {
                break;
            }
        }
        //left and right movement
        for (int i = 1; i < 8; i++) {
            String returned = MoveCalcHelper.moveCalcHelper(startRow, startCol + i, board, myPosition, teamColor, moves);
            if (returned.equals("Stop")) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            String returned = MoveCalcHelper.moveCalcHelper(startRow, startCol - i, board, myPosition, teamColor, moves);
            if (returned.equals("Stop")) {
                break;
            }
        }
    }
}
