package chess.moves.calculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class RookMoveCalculator {

    public static void rookMoveCalculator(ChessBoard board, ChessPosition myPosition,
                                             ArrayList<ChessMove> moves, ChessGame.TeamColor teamColor) {
        int startCol = myPosition.getColumn() - 1;
        int startRow = myPosition.getRow() - 1;
        RookAndQueenHelper.rookAndQueen(startRow, startCol,
                board, myPosition, teamColor, moves);
    }
}
