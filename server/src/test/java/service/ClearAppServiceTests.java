package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dataaccess.MemoryAuthDAO.authMap;
import static dataaccess.MemoryAuthDAO.createAuth;
import static dataaccess.MemoryGameDAO.createGame;
import static dataaccess.MemoryGameDAO.gameMap;
import static dataaccess.MemoryUserDAO.createUser;
import static dataaccess.MemoryUserDAO.userMap;
import static service.ClearAppService.clearApp;

public class ClearAppServiceTests {

    @Test
    @DisplayName("Clear App Positive")
    public void clearAppPositive() throws DataAccessException {
        AuthData auth = new AuthData("123", "sam");
        GameData game = new GameData(123, "", "","testGame",new ChessGame());
        UserData user = new UserData("samTest","testing123", "123@123.123");
        createAuth(auth);
        createUser(user);
        createGame(game);
        clearApp();
        assert userMap.isEmpty();
        assert authMap.isEmpty();
        assert gameMap.isEmpty();
    }

    @Test
    @DisplayName("Clear App Error")
    public void clearAppError() {
        clearApp();
    }
}
