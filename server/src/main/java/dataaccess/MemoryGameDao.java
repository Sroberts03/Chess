package dataaccess;

import model.GameData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDao implements GameDao {

    private final Map<Integer, GameData> gameMap = new HashMap<>();

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
        gameMap.remove(game.gameID());
        gameMap.put(game.gameID(), game);
    }

    public void clearGame() {
        gameMap.clear();
    }

    public int createGameId() {
        return gameMap.size() + 1;
    }

    public Map<Integer, GameData> getGameMap() {
        return gameMap;
    }
}
