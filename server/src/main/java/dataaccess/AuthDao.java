package dataaccess;

import model.AuthData;

import java.util.Map;

public interface AuthDao {

    void createAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;

    void clearAuth() throws DataAccessException;

    String generateToken();

    Map<String, AuthData> getAuthMap();

}
