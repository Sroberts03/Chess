package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    public static Map<String, AuthData> authMap = new HashMap<>();

    public static void createAuth(AuthData authData) {
        String authToken = authData.authToken();
        authMap.put(authToken, authData);
    }

    public static AuthData getAuth(String authToken) {
        return authMap.get(authToken);
    }

    public static void removeAuth(String authToken) {
        authMap.remove(authToken);
    }

    public static void clearAuth() {
        authMap.clear();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
