package chess.moves.calculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.ArrayList;


public class KingMoveCalculator {

    public static void kingMoveCalculator(ChessBoard board, ChessPosition myPosition,
                                     ArrayList<ChessMove> moves, ChessGame.TeamColor teamColor) {
        int startCol = myPosition.getColumn()-1;
        int startRow = myPosition.getRow()-1;
        int downRow = startRow - 1;
        int upRow = startRow + 1;
        int downCol = startCol - 1;
        int upCol = startCol + 1;
        MoveCalcHelper.moveCalcHelper(downRow, startCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(upRow, startCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(startRow, downCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(startRow, upCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(downRow, downCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(downRow, upCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(upRow, upCol, board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(upRow, downCol, board, myPosition, teamColor, moves);
    }
}
