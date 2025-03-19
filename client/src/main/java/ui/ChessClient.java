package ui;

import java.util.ArrayList;
import java.util.Arrays;
import errors.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import models.GameName;
import models.JoinGame;
import models.SignInData;
import ui.ServerFacade;


public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private String authToken;
    private Boolean gameJoined = false;
    private String playerColor = "";


    public ChessClient(String Url) {
        this.serverUrl = Url;
        server = new ServerFacade(serverUrl);
        authToken = null;
    }

    public Boolean getGameJoined() {
        return gameJoined;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd.toUpperCase()) {
                case "S" -> signIn(params);
                case "R" -> register(params);
                case "L" -> listGames();
                case "J" -> joinGame(params);
                case "C" -> createGame(params);
                case "SO" -> signOut();
                case "Q" -> quit();
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String signIn(String... params) throws ResponseException {
        if (params.length != 2) {
            return "Bad Request, need both User Name and Password";
        }
        SignInData signIn = new SignInData(params[0], params[1]);
        try {
            AuthData auth = server.login(signIn);
            authToken = auth.authToken();
            visitorName = auth.username();
            return "You are signed in as " + visitorName;
        } catch (ResponseException e) {
            throw new ResponseException(e.StatusCode(), e.getMessage());
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length != 3) {
            return "Bad Request, need User Name, Password, and Email";
        }
        UserData user = new UserData(params[0], params[1], params[2]);
        try {
            AuthData auth = server.register(user);
            authToken = auth.authToken();
            visitorName = auth.username();
            return "You have registered and signed in as " + visitorName;
        } catch (ResponseException e) {
            throw new ResponseException(e.StatusCode(), e.getMessage());
        }
    }

    public String signOut() throws ResponseException {
        try {
            server.logout(authToken);
            authToken = null;
            visitorName = null;
            return "Goodbye! You are now signed out";
        } catch (ResponseException e) {
            throw new ResponseException(e.StatusCode(), e.getMessage());
        }
    }

    public String listGames() throws ResponseException {
        try {
            ArrayList<GameData> games = server.listGames(authToken);
            StringBuilder out = new StringBuilder();
            for (GameData game : games) {
                out.append(game.gameID()).append(" ").append(game.gameName())
                        .append(" ").append("WHITE: ")
                        .append(game.whiteUsername()).append(" ").append("BLACK: ")
                        .append(game.blackUsername()).append("\n");
            }
            return out.toString();
        } catch (ResponseException e) {
            throw new ResponseException(e.StatusCode(), e.getMessage());
        }
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length != 1) {
            return "Bad Request, need Game Name";
        }
        GameName gameName = new GameName(params[0]);
        try {
            Integer gameID = server.createGame(authToken, gameName);
            return "New game created: " + gameID + " " + gameName.gameName();
        } catch (ResponseException e) {
            throw new ResponseException(e.StatusCode(), e.getMessage());
        }
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length != 2) {
            return "Bad Request, need both Player Color and Game ID";
        }
        JoinGame joinGame = new JoinGame(params[0], Integer.valueOf(params[1]));
        try {
            server.joinGame(authToken, joinGame);
            gameJoined = true;
            playerColor = joinGame.playerColor();
            return "You have joined Game " + params[1] + "\n";
        } catch (ResponseException e) {
            throw new ResponseException(e.StatusCode(), e.getMessage());
        }
    }

    public String quit() throws ResponseException {
        this.signOut();
        return "quit";
    }

    public String help() {
        if (authToken == null) {
            return """
                    S <User Name>, <Password> -> Sign In
                    R <User Name>, <Password>, <Email> -> Register
                    Q -> Quit
                    """;
        }
        if (authToken != null) {
            return """
                    L -> List All Games
                    J <Player Color>, <Game ID> -> Join Game
                    C <Game Name> -> Create Game
                    SO -> Sign Out
                    Q -> Quit
                    """;
        }
        return null;
    }
}
