package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class MoveCalcHelper {

    public static void Move_Calc_Helper(int endRow, int endCol, ChessBoard chessBoard,
                                        ChessPosition startPosition, ChessGame.TeamColor teamColor,
                                        ArrayList<ChessMove> moves) {
        if (((endRow <= 7 && endRow >= 0) && (endCol <= 7 && endCol >= 0))
                && (chessBoard.array[endRow][endCol] == null
                || chessBoard.array[endRow][endCol].getTeamColor() != teamColor)) {
            ChessPosition newPosition = new ChessPosition(endRow + 1, endCol + 1);
            moves.add(new ChessMove(startPosition, newPosition, null));
        }
    }
}

