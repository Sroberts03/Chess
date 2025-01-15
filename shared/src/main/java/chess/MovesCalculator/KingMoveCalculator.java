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
        KingMoveCalculator.King_Move_Calculator_helper(downRow, startCol, board, myPosition, teamColor, moves);
        KingMoveCalculator.King_Move_Calculator_helper(upRow, startCol, board, myPosition, teamColor, moves);
        KingMoveCalculator.King_Move_Calculator_helper(startRow, downCol, board, myPosition, teamColor, moves);
        KingMoveCalculator.King_Move_Calculator_helper(startRow, upCol, board, myPosition, teamColor, moves);
        KingMoveCalculator.King_Move_Calculator_helper(downRow, downCol, board, myPosition, teamColor, moves);
        KingMoveCalculator.King_Move_Calculator_helper(downRow, upCol, board, myPosition, teamColor, moves);
        KingMoveCalculator.King_Move_Calculator_helper(upRow, upCol, board, myPosition, teamColor, moves);
        KingMoveCalculator.King_Move_Calculator_helper(upRow, downCol, board, myPosition, teamColor, moves);
    }

    public static void King_Move_Calculator_helper(int endRow, int endCol, ChessBoard chessBoard, ChessPosition startPosition, ChessGame.TeamColor teamColor, ArrayList<ChessMove> moves) {
        if (((endRow <= 7 && endRow >= 0 ) && (endCol <= 7 && endCol >= 0))
                && (chessBoard.array[endRow][endCol] == null
                || chessBoard.array[endRow][endCol].getTeamColor() != teamColor)) {
            ChessPosition newPosition = new ChessPosition(endRow + 1, endCol + 1);
            moves.add(new ChessMove(startPosition, newPosition, null));
        }
    }
}
