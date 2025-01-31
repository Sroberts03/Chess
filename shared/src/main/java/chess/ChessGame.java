package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessBoard chessBoard;
    public TeamColor chessTurn;

    public ChessGame() {
        chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        chessTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return chessTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        chessTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece startPiece = chessBoard.getPiece(startPosition);
        if (startPiece == null) {
            moves.add(null);
        }
        else {
            moves = (ArrayList<ChessMove>) startPiece.pieceMoves(chessBoard, startPosition);
        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ArrayList<ChessMove> legalMoves;
        legalMoves = (ArrayList<ChessMove>) validMoves(move.getStartPosition());
        ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
        if (legalMoves.contains(null) && piece == null) {
            throw new InvalidMoveException();
        }
        if (!legalMoves.contains(move)) {
            throw new InvalidMoveException();
        }
        if (getTeamTurn() != piece.getTeamColor()) {
            throw new InvalidMoveException();
        }
        chessBoard.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) {
            ChessPiece.PieceType prom = move.getPromotionPiece();
            TeamColor color = piece.getTeamColor();
            ChessPiece promPawn = new ChessPiece(color, prom);
            chessBoard.addPiece(move.getEndPosition(), promPawn);
        } else {
            chessBoard.addPiece(move.getEndPosition(), piece);
        }
        if (getTeamTurn() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        }
        else if (getTeamTurn() == TeamColor.BLACK) {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
