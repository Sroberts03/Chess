package chess;

import chess.MovesCalculator.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor teamColor;
    private final PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (pieceType == PieceType.KING) {
            king_move_calculator.King_Move_Calculator(board, myPosition, moves, teamColor);
            return moves;
        }
        if (pieceType == PieceType.QUEEN) {
            QueenMoveCalculater.Queen_Move_Calculator(board, myPosition, moves, teamColor);
            return moves;
        }
        if (pieceType == PieceType.BISHOP) {
            BishopMoveCalculator.Bishop_Move_Calculator(board, myPosition, moves, teamColor);
            return moves;
        }
        if (pieceType == PieceType.KNIGHT) {
            KnightMoveCalculator.Knight_Move_Calculator(board, myPosition, moves, teamColor);
            return moves;
        }
        if (pieceType == PieceType.ROOK) {
            RookMoveCalculator.Rook_Move_Calculator(board, myPosition, moves, teamColor);
            return moves;
        }
        if (pieceType == PieceType.PAWN) {
            PawnMoveCalculator.Pawn_Move_Calculator(board, myPosition, moves, teamColor);
            return moves;
        }
        return java.util.List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType);
    }
}
