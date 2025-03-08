package dataaccess;

import model.GameData;
import java.util.ArrayList;
import java.util.Map;

public interface GameDao {

    void createGame(GameData game) throws DataAccessException;

    GameData getGame(Integer gameID) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    void updateGame(GameData game) throws DataAccessException;

    void clearGame() throws DataAccessException;

    int createGameId() throws DataAccessException;

    Map<Integer, GameData> getGameMap();
}
