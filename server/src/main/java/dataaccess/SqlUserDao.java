package dataaccess;

import model.UserData;
import java.sql.*;
import java.util.Map;
import static dataaccess.DatabaseManager.getConnection;


public class SqlUserDao implements UserDao{

    public SqlUserDao() throws DataAccessException {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new Error500(e.getMessage());
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String sql = "insert into userData (username, password, email) values (?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            query.setString(1, user.username());
            query.setString(2, user.password());
            query.setString(3, user.email());
            if (query.executeUpdate() == 1) {
                conn.commit();
            }
            else {
                conn.rollback();
            }
        } catch (SQLException e) {
            throw new Error500(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String sql = "select * from userData where username = ?";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement query = conn.prepareStatement(sql)) {
            query.setString(1, username);
            ResultSet rs = query.executeQuery();
            rs.next();
            return new UserData(rs.getString(1),rs.getString(2),rs.getString(3));
        } catch (SQLException e) {
            throw new Error500(e.getMessage());
        }
    }

    @Override
    public void clearUser() throws DataAccessException {
        String sql = "delete from userData";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement query = conn.prepareStatement(sql)) {
            query.executeUpdate();
        } catch (SQLException e) {
            throw new Error500(e.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  pet (
              username varchar(100) not null,
              password varchar(100) not null,
              email varchar(100) not null,
              primary key (username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new Error500(e.getMessage());
        }
    }

    @Override
    public Map<String, UserData> getUserMap() {
        return Map.of();
    }
}
