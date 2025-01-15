package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.ArrayList;


public class KingMoveCalculator {

    public static void King_Move_Calculator(ChessBoard board, ChessPosition myPosition,
                                     ArrayList<ChessMove> moves, ChessGame.TeamColor teamColor) {
        int startCol = myPosition.getColumn()-1;
        int startRow = myPosition.getRow()-1;
        int downRow = startRow - 1;
        int upRow = startRow + 1;
        int downCol = startCol - 1;
        int upCol = startCol + 1;
        MoveCalcHelper.Move_Calc_Helper(downRow, startCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(upRow, startCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(startRow, downCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(startRow, upCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(downRow, downCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(downRow, upCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(upRow, upCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.Move_Calc_Helper(upRow, downCol, board, myPosition, teamColor, moves);
    }
}
