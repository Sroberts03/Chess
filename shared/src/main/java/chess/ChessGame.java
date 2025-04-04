package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
        ArrayList<ChessMove> invalidMoves = new ArrayList<>();
        ChessPiece startPiece = chessBoard.getPiece(startPosition);
        if (startPiece == null) {
            moves.add(null);
            return moves;
        }
        if (isInCheckmate(getTeamTurn())){
            return moves;
        }
        else {
            moves = (ArrayList<ChessMove>) startPiece.pieceMoves(chessBoard, startPosition);
            for (ChessMove move : moves) {
                if (movePutsInCheck(move, startPiece.getTeamColor())) {
                    invalidMoves.add(move);
                }
            }
            moves.removeAll(invalidMoves);
        }
        return moves;
    }

    public boolean movePutsInCheck(ChessMove move, TeamColor color) {
        //make fake move
        ChessPiece startPiece = makeFakeMove(move);
        //check if king is now in check
        boolean status = isInCheck(color);
        //undo fake move
        undoFakeMove(move, startPiece);
        return status;
    }

    public ChessPiece makeFakeMove(ChessMove move) {
        ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
        int row = move.getEndPosition().getRow();
        int col = move.getEndPosition().getColumn();
        ChessPiece startPiece = chessBoard.array[row - 1][col - 1];
        chessBoard.addPiece(move.getStartPosition(), null);
        chessBoard.addPiece(move.getEndPosition(), piece);
        return startPiece;
    }

    public void undoFakeMove(ChessMove move, ChessPiece startPiece) {
        ChessPiece piece = chessBoard.getPiece(move.getEndPosition());
        chessBoard.addPiece(move.getEndPosition(), startPiece);
        chessBoard.addPiece(move.getStartPosition(), piece);
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
            throw new InvalidMoveException("Invalid move");
        }
        if (getTeamTurn() != piece.getTeamColor()) {
            throw new InvalidMoveException("Not your turn");
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
        ArrayList<ChessMove> possibleMoves = possibleMoves(teamColor);
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
        //find piece of same color
        ArrayList<ChessMove> possiblePieceMoves = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                //if any of the moves from that piece lead to not check
                if (chessBoard.array[i - 1][j - 1] != null
                        && chessBoard.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                    ChessPiece piece = chessBoard.getPiece(new ChessPosition(i, j));
                    ArrayList<ChessMove> pieceMoves =
                            (ArrayList<ChessMove>) piece.pieceMoves(chessBoard, new ChessPosition(i, j));
                    possiblePieceMoves.addAll(pieceMoves);
                }
            }
        }
        for (ChessMove move : possiblePieceMoves) {
            ChessPiece startPiece = makeFakeMove(move);
            boolean status = isInCheck(teamColor);
            undoFakeMove(move, startPiece);
            if (!status) {
                //return false
                return false;
            }
        }
        //otherwise return true
        return true;
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
        return validMoves.isEmpty() && !isInCheck(teamColor) && teamColor == getTeamTurn();
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(chessBoard, chessGame.chessBoard) && chessTurn == chessGame.chessTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, chessTurn);
    }
}
