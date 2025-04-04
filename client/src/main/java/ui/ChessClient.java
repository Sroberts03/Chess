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


public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private String authToken;
    private Integer gameJoined = 0;
    private String playerColor = "";
    private Integer observingGame = 0;


    public ChessClient(String url) {
        this.serverUrl = url;
        server = new ServerFacade(serverUrl);
        authToken = null;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void resetObservingGame() {
        observingGame = 0;
    }

    public Integer getObservingGame() {
        return observingGame;
    }

    public void setGameJoined() {
        gameJoined = 0;
    }

    public Integer getGameJoined() {
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
                case "O" -> observeGame(params);
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
            return "Bad Request, need User Name and Password";
        }
        SignInData signIn = new SignInData(params[0], params[1]);
        try {
            AuthData auth = server.login(signIn);
            authToken = auth.authToken();
            visitorName = auth.username();
            return "You are signed in as " + visitorName;
        } catch (ResponseException e) {
            throw new ResponseException(e.statusCode(), e.getMessage());
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
            throw new ResponseException(e.statusCode(), e.getMessage());
        }
    }

    public String signOut() throws ResponseException {
        try {
            server.logout(authToken);
            authToken = null;
            visitorName = null;
            return "Goodbye! You are now signed out";
        } catch (ResponseException e) {
            throw new ResponseException(e.statusCode(), e.getMessage());
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
            throw new ResponseException(e.statusCode(), e.getMessage());
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
            throw new ResponseException(e.statusCode(), e.getMessage());
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public String joinGame(String... params) throws ResponseException {
        int numGames = 0;
        if (params.length != 2) {
            return "Bad Request, need Player Color and Game ID";
        }
        if (!isNumeric(params[1])){
            return "Bad Request, order: J <Desired color> <game ID>";
        }
        try {
            numGames = server.listGames(authToken).size();
        } catch (ResponseException e) {
            throw new ResponseException(e.statusCode(), e.getMessage());
        }
        if (Integer.parseInt(params[1]) > numGames) {
            return "Game ID " + params[1] + " doesn't exist. Please Enter valid game ID";
        }
        JoinGame joinGame = new JoinGame(params[0], Integer.valueOf(params[1]));
        try {
            server.joinGame(authToken, joinGame);
            gameJoined = joinGame.gameID();
            playerColor = joinGame.playerColor();
            return "You have joined Game " + params[1] + "\n";
        } catch (ResponseException e) {
            throw new ResponseException(e.statusCode(), e.getMessage());
        }
    }

    public String observeGame(String... params) throws ResponseException {
        int numGames = 0;
        if (params.length != 1) {
            return "Bad Request, need Game ID";
        }
        if (!isNumeric(params[0])){
            return "Bad Request, order: O <game ID>";
        }
        try {
            numGames = server.listGames(authToken).size();
        } catch (ResponseException e) {
            throw new ResponseException(e.statusCode(), e.getMessage());
        }
        observingGame = Integer.valueOf(params[0]);
        if (observingGame > numGames) {
            observingGame = 0;
            return "Game ID " + params[0] + " doesn't exist. Please Enter valid game ID";
        }
        return "Observing Game " + observingGame + "\n";
    }

    public String quit() throws ResponseException {
        if (authToken != null) {
            this.signOut();
        }
        return "quit";
    }

    public String help() {
        if (authToken == null) {
            return """
                    Press any key for Help
                    S <User Name>, <Password> -> Sign In
                    R <User Name>, <Password>, <Email> -> Register
                    Q -> Quit
                    """;
        }
        if (authToken != null) {
            return """
                    Press any key for Help
                    L -> List All Games
                    J <Player Color>, <Game ID> -> Join Game
                    C <Game Name> -> Create Game
                    O <Game ID> -> Observe Game
                    SO -> Sign Out
                    Q -> Quit
                    """;
        }
        return null;
    }
}
