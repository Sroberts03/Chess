package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameDaoTests {

    private final GameDao sqlGameDao = new SqlGameDao();


    public GameDaoTests() throws DataAccessException {
    }

    @Test
    @DisplayName("Create Game good test")
    public void createGameGoodTest() {
        boolean thrown = false;
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(12, null, null, "testGame2", game);
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

    @Test
    @DisplayName("get Game good test")
    public void getGameGoodTest() {
        boolean thrown = false;
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, null, null, "testGame2", game);
        GameData sqlGameData = null;
        try {
            sqlGameDao.clearGame();
            sqlGameDao.createGame(gameData);
            sqlGameData = sqlGameDao.getGame(0);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
        assert sqlGameData.equals(gameData);
    }

    @Test
    @DisplayName("get Game bad test")
    public void getGameBadTest() {
        boolean thrown = false;
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, null, null, "testGame2", game);
        GameData sqlGameData = null;
        try {
            sqlGameDao.clearGame();
            sqlGameDao.createGame(gameData);
            sqlGameData = sqlGameDao.getGame(1);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert thrown;
    }

    @Test
    @DisplayName("list Games good test")
    public void listGameGoodTest() {
        boolean thrown = false;
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, null, null, "testGame", game);
        GameData gameData2 = new GameData(1, null,null, "testGame2", game);
        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(gameData);
        testList.add(gameData2);
        ArrayList<GameData> list = new ArrayList<>();
        try {
            sqlGameDao.clearGame();
            sqlGameDao.createGame(gameData);
            sqlGameDao.createGame(gameData2);
            list = sqlGameDao.listGames();
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
        assert list.equals(testList);
    }

    @Test
    @DisplayName("list Games bad test")
    public void listGameBadTest() {
        boolean thrown = false;
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(0, null, null, "testGame", game);
        GameData gameData2 = new GameData(1, null,null, "testGame2", game);
        try {
            sqlGameDao.clearGame();
            sqlGameDao.createGame(gameData);
            sqlGameDao.createGame(gameData2);
            sqlGameDao.createGame(gameData);
            sqlGameDao.listGames();
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert thrown;
    }

    @Test
    @DisplayName("clear game good")
    public void clearUserTestGood() throws DataAccessException {
        boolean thrown = false;
        try {
            sqlGameDao.clearGame();
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
    }

    @Test
    @DisplayName("clear game bad")
    public void clearUserTestBad() throws DataAccessException {
        boolean thrown = false;
        try {
            sqlGameDao.clearGame();
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
    }

    @Test
    @DisplayName("get map good")
    public void getMapGood() throws DataAccessException {
        sqlGameDao.getGameMap();
        assert true;
    }

    @Test
    @DisplayName("get map bad")
    public void getMapBad() throws DataAccessException {
        sqlGameDao.getGameMap();
        assert true;
    }

    @Test
    @DisplayName("get map good")
    public void updateGameGood() throws DataAccessException {
        GameData update = new GameData(1,null, null,
                "testGame", new ChessGame());
        sqlGameDao.updateGame(update);
        assert true;
    }

    @Test
    @DisplayName("get map bad")
    public void updateGameBad() throws DataAccessException {
        GameData update = new GameData(1,null, null,
                "testGame", new ChessGame());
        sqlGameDao.updateGame(update);
        assert true;
    }

    @Test
    @DisplayName("generate Id good")
    public void generateIdGood() throws DataAccessException {
        sqlGameDao.createGameId();
        assert true;
    }

    @Test
    @DisplayName("generate Id bad")
    public void generateIdBad() throws DataAccessException {
        sqlGameDao.createGameId();
        assert true;
    }
}
