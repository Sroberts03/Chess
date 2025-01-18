package chess.moves.calculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;


import java.util.ArrayList;

public class KnightMoveCalculator {

    public static void Knight_Move_Calculator(ChessBoard board, ChessPosition myPosition,
                                              ArrayList<ChessMove> moves, ChessGame.TeamColor teamColor) {
        int startRow = myPosition.getRow() - 1;
        int startCol = myPosition.getColumn() - 1;
        int leftTwo = startCol - 2;
        int rightTwo = startCol + 2;
        int downOne = startRow - 1;
        int upOne = startRow + 1;
        int upTwo = startRow + 2;
        int downTwo = startRow - 2;
        int leftOne = startCol - 1;
        int rightOne = startCol + 1;
        MoveCalcHelper.Move_Calc_Helper(downOne, leftTwo , board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(downOne, rightTwo , board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(upOne, rightTwo , board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(upOne, leftTwo , board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(upTwo, leftOne , board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(upTwo, rightOne , board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(downTwo, leftOne , board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(downTwo, rightOne , board, myPosition, teamColor, moves);
    }
}
