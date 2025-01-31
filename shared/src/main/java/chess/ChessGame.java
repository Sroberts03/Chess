package chess;

import javax.lang.model.type.ArrayType;
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
            return moves;
        }
//        else if (isInCheck(getTeamTurn())) {
//            return moves;
//        }
        else {
            moves = (ArrayList<ChessMove>) startPiece.pieceMoves(chessBoard, startPosition);
            for (ChessMove move : moves) {
                if (movePutsInCheck(move, getTeamTurn(), startPosition));
            }
        }
        return moves;
    }

    public boolean movePutsInCheck(ChessMove move, TeamColor color, ChessPosition startPosition) {
        //make fake move
        makeFakeMove(move);
        //check if king is now in check
        boolean status = isInCheck(color);
        //undo fake move
        undoFakeMove(move, startPosition);
        return status;
    }

    public void makeFakeMove(ChessMove move) {
        ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
        chessBoard.addPiece(move.getStartPosition(), null);
        chessBoard.addPiece(move.getEndPosition(), piece);
    }

    public void undoFakeMove(ChessMove move, ChessPosition startPosition) {
        ChessPiece piece = chessBoard.getPiece(move.getEndPosition());
        chessBoard.addPiece(move.getEndPosition(), null);
        chessBoard.addPiece(startPosition, piece);
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
        ChessPosition kingPosition = new ChessPosition(1, 6);
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (chessBoard.array[i - 1][j - 1] != null
                        && chessBoard.getPiece(new ChessPosition(i,j)).getTeamColor() == teamColor
                        && chessBoard.getPiece(new ChessPosition(i,j)).getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = new ChessPosition(i, j);
                }
            }
        }
        ArrayList<ChessMove> possibleMoves = (ArrayList<ChessMove>) possibleMoves(teamColor);
        for (ChessMove move : possibleMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ChessMove> possibleMoves(TeamColor teamColor) {
        ArrayList<ChessMove> allPossibleMoves = new ArrayList<>();
        for (int i = 1 ; i < 9 ; i++) {
            for (int j = 1; j < 9; j++) {
                if (chessBoard.array[i - 1][j - 1] != null
                        && chessBoard.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                    ChessPiece piece = chessBoard.getPiece(new ChessPosition(i, j));
                    ArrayList<ChessMove> pieceMoves =
                            (ArrayList<ChessMove>) piece.pieceMoves(chessBoard, new ChessPosition(i, j));
                    allPossibleMoves.addAll(pieceMoves);
                }
            }
        }
        return allPossibleMoves;
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
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessBoard.array[i][j] != null
                        && chessBoard.getPiece(new ChessPosition(i+1,j+1)).getTeamColor() == teamColor) {
                    ArrayList<ChessMove> possibleMoves = (ArrayList<ChessMove>) validMoves(new ChessPosition(i+1,j+1));
                    validMoves.addAll(possibleMoves);
                }
            }
        }
        return validMoves.isEmpty();
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
