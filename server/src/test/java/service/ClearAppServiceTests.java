package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClearAppServiceTests {

    private final GameDao gameDAO = new MemoryGameDao();
    private final AuthDao authDAO = new MemoryAuthDao();
    private final UserDao userDAO = new MemoryUserDao();
    private final ClearAppService clearApp = new ClearAppService(gameDAO,authDAO,userDAO);

    @Test
    @DisplayName("Clear App Positive")
    public void clearAppPositive() throws DataAccessException {
        AuthData auth = new AuthData("123", "sam");
        GameData game = new GameData(123, "", "","testGame",new ChessGame());
        UserData user = new UserData("samTest","testing123", "123@123.123");
        authDAO.createAuth(auth);
        userDAO.createUser(user);
        gameDAO.createGame(game);
        clearApp.clearApp();
        assert userDAO.getUserMap().isEmpty();
        assert authDAO.getAuthMap().isEmpty();
        assert gameDAO.getGameMap().isEmpty();
    }

    @Test
    @DisplayName("Clear App Error")
    public void clearAppError() throws DataAccessException {
        clearApp.clearApp();
    }
}
