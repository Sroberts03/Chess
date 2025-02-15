package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import resultrequest.*;

import static dataaccess.MemoryAuthDAO.createAuth;
import static dataaccess.MemoryGameDAO.*;
import static service.ClearAppService.clearApp;
import static service.GameService.getGames;
import static service.GameService.joinGame;

public class GameServiceTests {

    @Test
    @DisplayName("getGames Authorized")
    public void getGamesTestAuthorized() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, null, null,
                "test",game);
        createGame(gameData);
        ChessGame game2 = new ChessGame();
        GameData gameData2 = new GameData(123, null, null,
                "test",game2);
        createGame(gameData2);
        boolean thrown = false;
        GameListRequest request = new GameListRequest("123");
        GameListResult result = null;
        try {
            result = getGames(request);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
        assert result.gameList().equals(listGames());
    }

    @Test
    @DisplayName("getGames Unauthorized")
    public void getGamesTestUnauthorized() {
        clearApp();
        boolean thrown = false;
        GameListRequest request = new GameListRequest("123");
        try {
            getGames(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("createGame Authorized")
    public void createGameTest() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        createAuth(auth);
        boolean thrown = false;
        CreateGameResult result = null;
        try {
            result = GameService.createGame(new CreateGameRequest(auth.authToken(), "123"));
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
        assert gameMap.containsKey(result.gameID());
    }

    @Test
    @DisplayName("createGame Unauthorized")
    public void createGameTestUnauthorized() {
        clearApp();
        boolean thrown = false;
        try {
            GameService.createGame(new CreateGameRequest("testGame", "123"));
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("createGame Bad Request")
    public void createGameTestBadRequest() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        createAuth(auth);
        boolean thrown = false;
        try {
            GameService.createGame(new CreateGameRequest("", "123"));
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Happy Path for Black")
    public void joinGameTestHappyPathBlack() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("black",123,auth.authToken());
        try {
            joinGame(request);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
        assert !gameMap.get(gameData.gameID()).blackUsername().isEmpty();
        assert gameMap.get(gameData.gameID()).blackUsername().equals("sam");
    }

    @Test
    @DisplayName("joinGame Happy Path for White")
    public void joinGameTestHappyPathWhite() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("White",123,auth.authToken());
        try {
            joinGame(request);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
        assert !gameMap.get(gameData.gameID()).whiteUsername().isEmpty();
        assert gameMap.get(gameData.gameID()).whiteUsername().equals("sam");
    }


    @Test
    @DisplayName("joinGame Unauthorized")
    public void joinGameTestUnauthorized() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("White",123,auth.authToken());
        try {
            joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Bad Request Empty Player Color")
    public void joinGameTestBadRequestEmptyPlayer() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("",123,auth.authToken());
        try {
            joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Bad Request Empty gameId")
    public void joinGameTestBadRequestEmptyGameId() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("black",null,auth.authToken());
        try {
            joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Bad Request Wrong Color")
    public void joinGameTestBadRequestWrongColor() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("purple",123,auth.authToken());
        try {
            joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Already Taken White")
    public void joinGameTestAlreadyTakenWhite() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "henry", "",
                "test",game);
        createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("white",123,auth.authToken());
        try {
            joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: already taken")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Already Taken Black")
    public void joinGameTestAlreadyTakenBlack() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "Henry",
                "test",game);
        createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("black",123,auth.authToken());
        try {
            joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: already taken")) {
                thrown = true;
            }
        }
        assert thrown;
    }
}
