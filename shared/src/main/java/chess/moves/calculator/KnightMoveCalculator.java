package chess.moves.calculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;


import java.util.ArrayList;

public class KnightMoveCalculator {

    public static void knightMoveCalculator(ChessBoard board, ChessPosition myPosition,
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
        MoveCalcHelper.moveCalcHelper(downOne, leftTwo , board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(downOne, rightTwo , board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(upOne, rightTwo , board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(upOne, leftTwo , board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(upTwo, leftOne , board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(upTwo, rightOne , board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(downTwo, leftOne , board, myPosition, teamColor, moves);
        MoveCalcHelper.moveCalcHelper(downTwo, rightOne , board, myPosition, teamColor, moves);
    }
}
