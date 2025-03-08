package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameDaoTests {

    private final GameDao sqlGameDao = new SqlGameDao();


    public GameDaoTests() throws DataAccessException {
    }

    @Test
    @DisplayName("Create Game good test")
    public void createGameGoodTest() {
        boolean thrown = false;
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, null, null, "testGame2", game);
        try {
            sqlGameDao.clearGame();
            sqlGameDao.createGame(gameData);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
    }

    @Test
    @DisplayName("Create Game bad test")
    public void createGameBadTest() {
        boolean thrown = false;
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, null, null, "testGame2", game);
        try {
            sqlGameDao.clearGame();
            sqlGameDao.createGame(gameData);
            sqlGameDao.createGame(gameData);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert thrown;
    }

//    @Test
//    @DisplayName("get Game good test")
//    public void getGameGoodTest() {
//        boolean thrown = false;
//        ChessGame game = new ChessGame();
//        GameData gameData = new GameData(0, null, null, "testGame2", game);
//        try {
//            sqlGameDao.clearGame();
//            sqlGameDao.createGame(gameData);
//            GameData sqlGameData = sqlGameDao.getGame()
//        } catch (DataAccessException e) {
//            thrown = true;
//        }
//        assert thrown;
//    }
}
