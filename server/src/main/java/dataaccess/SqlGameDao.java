package dataaccess;

import model.GameData;

import java.util.ArrayList;

public abstract class SqlGameDao implements GameDao{
    @Override
    public void createGame(GameData game) throws DataAccessException {

    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void clearGame() throws DataAccessException {

    }

    @Override
    public int createGameId() {
        return 0;
    }
}
