package ui;

import chess.*;
import errors.ResponseException;
import websocket.WebsocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.*;

import static ui.EscapeSequences.*;

public class GamePlayClient implements GameHandler{

    private ChessGame game;
    private WebsocketFacade ws;
    private final String serverUrl;
    private final String playerColor;
    private final String authToken;
    private final Integer gameID;
    private final boolean obs;

    public GamePlayClient(String serverUrl, String playerColor,
                          String authToken, Integer gameID, boolean obs) {
        this.serverUrl = serverUrl;
        this.playerColor = playerColor;
        this.authToken = authToken;
        this.gameID = gameID;
        this.obs = obs;
    }

    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd.toUpperCase()) {
                case "RD" -> redraw();
                case "L" -> leave();
                case "M" -> makeMove(params);
                case "R" -> resign();
                case "H" -> highlight(params);
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void connect() throws ResponseException {
        ws = new WebsocketFacade(serverUrl, authToken, gameID, this);
        ws.connect();
    }

    public String redraw() {
        printBoard();
        return "redrawn";
    }

    public String leave() throws ResponseException {
        ws.leave();
        return "quit";
    }

    public Map<String, Integer> letToCol() {
        HashMap<String, Integer> blackCol = new HashMap<>();
        blackCol.put("a", 1);
        blackCol.put("b", 2);
        blackCol.put("c", 3);
        blackCol.put("d", 4);
        blackCol.put("e", 5);
        blackCol.put("f", 6);
        blackCol.put("g", 7);
        blackCol.put("h", 8);
        return blackCol;
    }

    public String makeMove(String... params) throws ResponseException {
        Map<String, Integer> letToNum;
        String startCol = params[0];
        int startRow = Integer.parseInt(params[1]);
        String endCol = params[2];
        int endRow = Integer.parseInt(params[3]);
        if (playerColor.equals("black")) {
            letToNum = letToCol();
            int startColInt = letToNum.get(startCol);
            int endColInt = letToNum.get(endCol);
            ChessPosition start = new ChessPosition(startRow,startColInt);
            ChessPosition end = new ChessPosition(endRow,endColInt);
            ChessMove move = new ChessMove(start,end,null);
            ws.makeMove(move);
            return "move made";
        }
        else if (playerColor.equals("white")) {
            letToNum = letToCol();
            int startColInt = letToNum.get(startCol);
            int endColInt = letToNum.get(endCol);
            ChessPosition start = new ChessPosition(startRow,startColInt);
            ChessPosition end = new ChessPosition(endRow,endColInt);
            ChessMove move = new ChessMove(start,end,null);
            ws.makeMove(move);
            return "move made";
        }
        return "bad";
    }

    public String resign() throws ResponseException {
        ws.resign();
        return "resigned";
    }

    public String highlight(String... params) {
        Map<String, Integer> letToNum = letToCol();
        int row = Integer.parseInt(params[1]);
        int col = letToNum.get(params[0]);
        ChessPosition chessPosition = new ChessPosition(row,col);
        Collection<ChessMove> validMoves = game.validMoves(chessPosition);
        if (playerColor.equals("white")) {
            highlightBoard(validMoves, -1, chessPosition);
        }
        else if (playerColor.equals("black")) {
            highlightBoard(validMoves, 1, chessPosition);
        }
        return "highlight";
    }

    public void highlightBoard(Collection<ChessMove> validMoves, int minusOrPlus, ChessPosition startPos) {
        Map<Integer, String> numToLetMapWhite = numToLetMapWhite();
        Map<Integer, String> numToLetMapBlack = numToLetMapBlack();
        ChessBoard board = game.getBoard();
        String squareColor = "White";
        if (minusOrPlus == 1) {
            beginAndEnd(numToLetMapBlack);
        }
        else if (minusOrPlus == -1) {
            beginAndEnd(numToLetMapWhite);
        }
        if (minusOrPlus == 1) {
            for (int i = 0; i < board.array.length; i++) {
                squareColor = getStringHighlight(board, squareColor, i, validMoves, startPos);
            }
        }
        if (minusOrPlus == -1) {
            for (int i = board.array.length - 1; i > -1; i--) {
                squareColor = getStringHighlight(board, squareColor, i, validMoves, startPos);
            }
        }
        System.out.print(RESET_BG_COLOR);
        if (minusOrPlus == 1) {
            beginAndEnd(numToLetMapBlack);
        }
        else if (minusOrPlus == -1) {
            beginAndEnd(numToLetMapWhite);
        }
    }

    private String getStringHighlight(ChessBoard board, String squareColor, int i,
                                      Collection<ChessMove> validMoves, ChessPosition startPos) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (i + 1) + " ");
        if (playerColor.equals("white")) {
            for (int j = 0; j < board.array.length; j++) {
                squareColor = highlightBoardHelper(board, squareColor, i, j, validMoves, startPos);
            }
        }
        if (playerColor.equals("black")) {
            for (int j = board.array.length - 1; j > -1; j--) {
                squareColor = highlightBoardHelper(board, squareColor, i, j, validMoves, startPos);
            }
        }
        if (squareColor.equals("Black")) {
            squareColor = "White";
        }
        else if (squareColor.equals("White")) {
            squareColor = "Black";
        }
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK+ " " + (i+1) + " ");
        System.out.print(RESET_BG_COLOR + "\n");
        return squareColor;
    }

    private String highlightBoardHelper(ChessBoard board, String squareColor, int i, int j,
                                        Collection<ChessMove> validMoves, ChessPosition startPos) {
        ChessPosition pos = new ChessPosition(i + 1, j + 1);
        ChessPiece piece = board.getPiece(pos);
        ChessMove move = new ChessMove(startPos, pos, null);
        if (squareColor.equals("White")) {
            pieceCheckerSquareWhite(piece, validMoves, move);
            squareColor = "Black";
        } else if (squareColor.equals("Black")) {
            pieceCheckerSquareBlack(piece, validMoves, move);
            squareColor = "White";
        }
        return squareColor;
    }

    public void printBoard() {
        if (playerColor.equalsIgnoreCase("WHITE")) {
            printBoard(7,-1,-1);
        }
        else if (playerColor.equalsIgnoreCase("BLACK")) {
            printBoard(0,8,1);
        }
    }

    private Map<Integer, String> numToLetMapWhite() {
        HashMap<Integer, String> numToLetMap = new HashMap<>();
        numToLetMap.put(1, "a");
        numToLetMap.put(2, "b");
        numToLetMap.put(3, "c");
        numToLetMap.put(4, "d");
        numToLetMap.put(5, "e");
        numToLetMap.put(6, "f");
        numToLetMap.put(7, "g");
        numToLetMap.put(8, "h");
        return numToLetMap;
    }

    private Map<Integer, String> numToLetMapBlack() {
        HashMap<Integer, String> numToLetMap = new HashMap<>();
        numToLetMap.put(1, "h");
        numToLetMap.put(2, "g");
        numToLetMap.put(3, "f");
        numToLetMap.put(4, "e");
        numToLetMap.put(5, "d");
        numToLetMap.put(6, "c");
        numToLetMap.put(7, "b");
        numToLetMap.put(8, "a");
        return numToLetMap;
    }

    private void beginAndEnd(Map<Integer, String> numMap) {
        for (int i = 0; i < 10; i++) {
            if (i == 0 || i == 9){
                System.out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
            }
            else {
                System.out.print(SET_TEXT_COLOR_BLACK + " " + numMap.get(i) + " ");
            }
        }
        System.out.print(RESET_BG_COLOR + "\n");
    }

    private void pieceCheckerSquareWhite(ChessPiece piece, Collection<ChessMove> validMoves, ChessMove move) {
        if (validMoves == null) {
            if (piece == null) {
                System.out.print(SET_BG_COLOR_WHITE + "   ");
            } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " R ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " N ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " B ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " K ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " Q ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " P ");
                }
            } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " R ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " N ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " B ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " K ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " Q ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " P ");
                }
            }
        }
        else if (validMoves != null) {
            if (piece == null) {
                if (validMoves.contains(move)) {
                    System.out.print(SET_BG_COLOR_YELLOW + "   ");
                }
                else {
                    System.out.print(SET_BG_COLOR_WHITE + "   ");
                }
            } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_RED + " R ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " R ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_RED + " N ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " N ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_RED + " B ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " B ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_RED + " K ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " K ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    if (validMoves.contains(move)) {
                    System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_RED + " Q ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " Q ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_RED + " P ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " P ");
                    }
                }
            } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_BLUE + " R ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " R ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_BLUE + " R ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " N ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_BLUE + " R ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " B ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_BLUE + " R ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " K ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_BLUE + " R ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " Q ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_YELLOW + SET_TEXT_COLOR_BLUE + " R ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " P ");
                    }
                }
            }
        }
    }

    private void pieceCheckerSquareBlack(ChessPiece piece, Collection<ChessMove> validMoves, ChessMove move) {
        if (validMoves == null) {
            if (piece == null) {
                System.out.print(SET_BG_COLOR_BLACK + "   ");
            } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " R ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " N ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " B ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " K ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " Q ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " P ");
                }
            } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " R ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " N ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " B ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " K ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " Q ");
                }
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " P ");
                }
            }
        }
        if (validMoves != null) {
            if (piece == null) {
                if (validMoves.contains(move)) {
                    System.out.print(SET_BG_COLOR_DARK_GREEN + "   ");
                }
                else {
                    System.out.print(SET_BG_COLOR_BLACK + "   ");
                }
            } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_RED + " R ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " R ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_RED + " N ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " N ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_RED + " B ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " B ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_RED + " K ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " K ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_RED + " Q ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " Q ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_RED + " P ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_RED + " P ");
                    }
                }
            } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLUE + " R ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " R ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLUE + " N ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " N ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLUE + " B ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " B ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLUE + " K ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " K ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLUE + " Q ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " Q ");
                    }
                }
                if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    if (validMoves.contains(move)) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_BLUE + " P ");
                    }
                    else {
                        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLUE + " P ");
                    }
                }
            }
        }
    }

    private String printBoardHelper(ChessBoard board, String squareColor, int i, int j) {
        ChessPosition pos = new ChessPosition(i + 1, j + 1);
        ChessPiece piece = board.getPiece(pos);
        if (squareColor.equals("White")) {
            pieceCheckerSquareWhite(piece, null, null);
            squareColor = "Black";
        } else if (squareColor.equals("Black")) {
            pieceCheckerSquareBlack(piece, null, null);
            squareColor = "White";
        }
        return squareColor;
    }

    private void printBoard(int startI, int endI, int minusOrPlus) {
        System.out.print("\n");
        Map<Integer, String> numToLetMapWhite = numToLetMapWhite();
        Map<Integer, String> numToLetMapBlack = numToLetMapBlack();
        ChessBoard board = game.getBoard();
        String squareColor = "White";
        if (minusOrPlus == 1) {
            beginAndEnd(numToLetMapBlack);
        }
        else if (minusOrPlus == -1) {
            beginAndEnd(numToLetMapWhite);
        }
        if (minusOrPlus == 1) {
            for (int i = startI; i < endI; i++) {
                squareColor = getString(board, squareColor, i);
            }
        }
        if (minusOrPlus == -1) {
            for (int i = startI; i > endI; i--) {
                squareColor = getString(board, squareColor, i);
            }
        }
        System.out.print(RESET_BG_COLOR);
        if (minusOrPlus == 1) {
            beginAndEnd(numToLetMapBlack);
        }
        else if (minusOrPlus == -1) {
            beginAndEnd(numToLetMapWhite);
        }
    }

    private String getString(ChessBoard board, String squareColor, int i) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + (i + 1) + " ");
        if (playerColor.equals("white")) {
            for (int j = 0; j < board.array.length; j++) {
                squareColor = printBoardHelper(board, squareColor, i, j);
            }
        }
        if (playerColor.equals("black")) {
            for (int j = board.array.length - 1; j > -1; j--) {
                squareColor = printBoardHelper(board, squareColor, i, j);
            }
        }
        if (squareColor.equals("Black")) {
            squareColor = "White";
        }
        else if (squareColor.equals("White")) {
            squareColor = "Black";
        }
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK+ " " + (i+1) + " ");
        System.out.print(RESET_BG_COLOR + "\n");
        return squareColor;
    }

    public String help() {
        if (!obs) {
            System.out.print("""
                    press any key for help menu
                    L -> leave game
                    RD -> Redraw Board
                    M <Start Column> <Start Row> <End Column> <End Row> -> make move to column and row
                    R -> Resign game
                    H <Column> <Row> -> Highlight Possible Moves of Piece at Column and Row
                    """);
        }
        else if (obs) {
            System.out.print("""
                    press any key for help menu
                    L -> leave game
                    RD -> Redraw Board
                    H <Column> <Row> -> Highlight Possible Moves of Piece at Column and Row
                    """);
        }
        return "help";
    }

    @Override
    public void onLoadGame(LoadGameMessage message) {
        game = message.getGame();
        printBoard();
    }

    @Override
    public void onError(ErrorMessage message) {
        String mes = message.getErrorMessage();
        System.out.print("\n" + SET_TEXT_COLOR_BLUE + mes + "\n" + RESET_TEXT_COLOR);
    }

    @Override
    public void onNotifiy(NotificationMessage message) {
        String mes = message.getMessage();
        System.out.print("\n" + SET_TEXT_COLOR_BLUE + mes + "\n" + RESET_TEXT_COLOR);
    }
}
