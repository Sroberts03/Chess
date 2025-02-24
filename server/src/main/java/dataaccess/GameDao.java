package dataaccess;

import model.GameData;
import java.util.ArrayList;
import java.util.Map;

public interface GameDao {

    void createGame(GameData game);

    GameData getGame(Integer gameID);

    ArrayList<GameData> listGames();

    void updateGame(GameData game);

    void clearGame();

    int createGameId();

    Map<Integer, GameData> getGameMap();
}
