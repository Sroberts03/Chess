package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    public Map<Integer, GameData> gameMap;

    public MemoryGameDAO() {
        gameMap = new HashMap<>();
    }

    public void createGame(GameData game) {
        Integer gameID = game.gameID();
        gameMap.put(gameID, game);
    }

    public GameData getGame(Integer gameID) {
        return gameMap.get(gameID);
    }

    public ArrayList<GameData> listGames() {
        return new ArrayList<>(gameMap.values());
    }

    public void updateGame(GameData game) {
        gameMap.put(game.gameID(), game);
    }
}
