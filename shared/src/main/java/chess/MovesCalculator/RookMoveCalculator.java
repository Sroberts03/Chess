package chess.MovesCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class RookMoveCalculator {

    public static void Rook_Move_Calculator(ChessBoard board, ChessPosition myPosition,
                                             ArrayList<ChessMove> moves, ChessGame.TeamColor teamColor) {
        int startCol = myPosition.getColumn() - 1;
        int startRow = myPosition.getRow() - 1;
        RookAndQueenHelper.Rook_And_Queen(startRow, startCol,
                board, myPosition, teamColor, moves);
    }
}
