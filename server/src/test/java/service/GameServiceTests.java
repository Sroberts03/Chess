package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import resultrequest.CreateGameRequest;
import resultrequest.CreateGameResult;
import resultrequest.GameListRequest;
import resultrequest.GameListResult;
import static dataaccess.MemoryAuthDAO.createAuth;
import static dataaccess.MemoryGameDAO.*;
import static service.ClearAppService.clearApp;
import static service.GameService.getGames;

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
    @DisplayName("joinGame Happy Path")
    public void joinGameTestHappyPath() {

    }

    @Test
    @DisplayName("joinGame Unauthorized")
    public void joinGameTestUnauthorized() {

    }

    @Test
    @DisplayName("joinGame Bad Request")
    public void joinGameTestBadRequest() {

    }

    @Test
    @DisplayName("joinGame Already Taken")
    public void joinGameTestAlreadyTaken() {

    }
}
