package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import static dataaccess.DatabaseManager.configureDatabase;

public class SqlAuthDao implements AuthDao{

    public SqlAuthDao() throws DataAccessException {
        try {
            configureDatabase(createStatements);
        } catch (DataAccessException e) {
            throw new Error500(e.getMessage());
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        String sql = "insert into authData (username, authToken) values (?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            query.setString(1, authData.username());
            query.setString(2, authData.authToken());
            query.executeUpdate();
        } catch (SQLException e) {
            throw new Error500(e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String sql = "select * from authData where authToken = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement query = conn.prepareStatement(sql)) {
            query.setString(1, authToken);
            ResultSet rs = query.executeQuery();
            rs.next();
            return new AuthData(rs.getString(2),rs.getString(1));
        } catch (SQLException e) {
            throw new Error500(e.getMessage());
        }
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clearAuth() throws DataAccessException {
        String sql = "delete from authData";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement query = conn.prepareStatement(sql)) {
            query.executeUpdate();
        } catch (SQLException e) {
            throw new Error500(e.getMessage());
        }
    }

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Map<String, AuthData> getAuthMap() {
        return Map.of();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authData (
                 username varchar(100) not null,
                 authToken varchar(100) not null,
                 primary key (username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
