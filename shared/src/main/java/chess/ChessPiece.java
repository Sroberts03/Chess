package chess;

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
        if (pieceType == PieceType.KING) {
            return PieceMovesCalculator.KingMoveCalculator(board, myPosition, teamColor);
        }
        if (pieceType == PieceType.QUEEN) {
            return PieceMovesCalculator.QueenMoveCalculator(board, myPosition, teamColor);
        }
        if (pieceType == PieceType.BISHOP) {
            return PieceMovesCalculator.BishopMoveCalculator(board, myPosition, teamColor);
        }
        if (pieceType == PieceType.KNIGHT) {
            return PieceMovesCalculator.KnightMoveCalculator(board, myPosition, teamColor);
        }
        if (pieceType == PieceType.ROOK) {
            return PieceMovesCalculator.RookMoveCalculator(board, myPosition, teamColor);
        }
        if (pieceType == PieceType.PAWN) {
            return PieceMovesCalculator.PawnMoveCalculator(board, myPosition, teamColor);
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
