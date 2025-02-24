package dataaccess;

import model.AuthData;

import java.util.Map;

public interface AuthDao {

    void createAuth(AuthData authData);

    AuthData getAuth(String authToken);

    void removeAuth(String authToken);

    void clearAuth();

    String generateToken();

    Map<String, AuthData> getAuthMap();

}
