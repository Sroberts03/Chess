package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import static dataaccess.DatabaseManager.configureDatabase;

public class SqlGameDao implements GameDao{

    public SqlGameDao() throws DataAccessException {
        try {
            configureDatabase(createStatements);
        } catch (DataAccessException e) {
            throw new Error500(e.getMessage());
        }
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        String sql = "insert into gameData (gameId, whiteUsername, blackUsername, gameName, chessGame) values (?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            Gson gson = new Gson();
            String gameJson = gson.toJson(game.game());
            query.setInt(1, game.gameID());
            query.setString(2, game.whiteUsername());
            query.setString(3, game.blackUsername());
            query.setString(4, game.gameName());
            query.setString(5, gameJson);
            query.executeUpdate();
        } catch (SQLException e) {
            throw new Error500(e.getMessage());
        }
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        String sql = "select * from gameData where gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            Gson gson = new Gson();
            query.setInt(1, gameID);
            ResultSet rs = query.executeQuery();
            rs.next();
            String gameString = rs.getString(5);
            ChessGame game = gson.fromJson(gameString, ChessGame.class);
            return new GameData(rs.getInt(1),rs.getString(2),
                    rs.getString(3), rs.getString(4), game);
        } catch (SQLException e) {
            throw new Error500(e.getMessage());
        }
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
        String sql = "delete from gameData";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement query = conn.prepareStatement(sql)) {
            query.executeUpdate();
        } catch (SQLException e) {
            throw new Error500(e.getMessage());
        }
    }

    @Override
    public int createGameId() throws DataAccessException {
        ArrayList<GameData> numOfGames = listGames();
        return numOfGames.size() + 1;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameData (
                 gameId int primary key,
                 whiteUsername varchar(100) null,
                 blackUsername varchar(100) null,
                 gameName varchar(100) not null,
                 chessGame json not null
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    @Override
    public Map<Integer, GameData> getGameMap() {
        return Map.of();
    }
}
