package ui;

import java.sql.PreparedStatement;
import java.util.Arrays;
import com.google.gson.Gson;
import errors.ResponseException;
import model.AuthData;
import model.UserData;
import models.SignInData;
import ui.ServerFacade;


public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private String authToken;


    public ChessClient(String Url) {
        this.serverUrl = Url;
        server = new ServerFacade(serverUrl);
        authToken = null;
    }

    public String eval(String input) {
        try {
            var tokens = input.toUpperCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "S" -> signIn(params);
                case "R" -> register(params);
//                case "L" -> listGames(params);
//                case "J" -> joinGame(params);
//                case "C" -> createGame(params);
                case "SO" -> signOut();
                case "Q" -> "quit";
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String signIn(String... params) throws ResponseException {
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
                    J <Player Color>, <Game Name> -> Join Game
                    C <Game Name> -> Create Game
                    SO -> Sign Out
                    Q -> Quit
                    """;
        }
        return null;
    }
}
