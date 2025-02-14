package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import resultrequest.GameListRequest;
import resultrequest.GameListResult;
import static dataaccess.MemoryAuthDAO.createAuth;
import static dataaccess.MemoryGameDAO.createGame;
import static dataaccess.MemoryGameDAO.listGames;
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
        } catch (DataAccessException d) {
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
        } catch (DataAccessException d) {
            if (d.getMessage().equals("Error: unauthorized")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("createGame Authorized")
    public void createGameTest() {

    }

    @Test
    @DisplayName("createGame Unauthorized")
    public void createGameTestUnauthorized() {

    }

    @Test
    @DisplayName("createGame Bad Request")
    public void createGameTestBadRequest() {

    }

    @Test
    @DisplayName("joinGame positive")
    public void joinGameTest() {

    }

    @Test
    @DisplayName("joinGame negative")
    public void joinGameTestNegative() {

    }
}
