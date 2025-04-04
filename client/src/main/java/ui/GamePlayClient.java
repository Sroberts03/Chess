package ui;

import chess.ChessGame;
import errors.ResponseException;
import websocket.WebsocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.Arrays;

public class GamePlayClient implements GameHandler{

    private ChessGame game;
    private WebsocketFacade ws;
    private final String serverUrl;
    private final String playerColor;
    private final String authToken;
    private final Integer gameID;

    public GamePlayClient(String serverUrl, String playerColor,
                          String authToken, Integer gameID) {
        this.serverUrl = serverUrl;
        this.playerColor = playerColor;
        this.authToken = authToken;
        this.gameID = gameID;
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

    public String makeMove(String... params) {

    }

    public String resign() throws ResponseException {
        ws.resign();
        return "resigned";
    }

    public String highlight(String... params) {

    }

    public void printBoard() {
        if (playerColor.equalsIgnoreCase("WHITE")) {
            whiteBoard();
        }
        else if (playerColor.equalsIgnoreCase("BLACK")) {
            blackBoard();
        }
    }

    private void whiteBoard() {

    }

    private void blackBoard() {

    }

    public String help() {
        return """
                press any key for help menu
                L -> leave game
                RD -> Redraw Board
                M <Column> <Row> -> make move to column and row
                R -> Resign game
                H <Column> <Row> -> Highlight Possible Moves of Piece at Column and Row
                """;
    }

    @Override
    public void onLoadGame(LoadGameMessage message) {
        game = message.getGame();
        printBoard();
    }

    @Override
    public void onError(ErrorMessage message) {
        String mes = message.getErrorMessage();
        System.out.print(mes);
    }

    @Override
    public void onNotifiy(NotificationMessage message) {
        String mes = message.getMessage();
        System.out.print(mes);
    }
}
