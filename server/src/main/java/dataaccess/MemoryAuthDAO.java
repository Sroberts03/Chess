package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private final Map<String, AuthData> authMap = new HashMap<>();

    public void createAuth(AuthData authData) {
        String authToken = authData.authToken();
        authMap.put(authToken, authData);
    }

    public AuthData getAuth(String authToken) {
        return authMap.get(authToken);
    }

    public void removeAuth(String authToken) {
        authMap.remove(authToken);
    }

    public void clearAuth() {
        this.authMap.clear();
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public Map<String, AuthData> getAuthMap() {
        return authMap;
    }
}
