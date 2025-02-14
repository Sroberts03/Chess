package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    public static Map<Integer, GameData> gameMap = new HashMap<>();

    public static void createGame(GameData game) {
        Integer gameID = game.gameID();
        gameMap.put(gameID, game);
    }

    public static GameData getGame(Integer gameID) {
        return gameMap.get(gameID);
    }

    public static ArrayList<GameData> listGames() {
        return new ArrayList<>(gameMap.values());
    }

    public static void updateGame(GameData game) {
        gameMap.put(game.gameID(), game);
    }

    public static void clearGame() {
        gameMap.clear();
    }
}
