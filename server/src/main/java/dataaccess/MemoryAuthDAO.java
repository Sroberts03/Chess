package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    public static Map<String, String> authMap = new HashMap<>();

    public static void createAuth(AuthData authData) {
        String username = authData.username();
        String authToken = authData.authToken();
        authMap.put(authToken, username);
    }

    public static String getAuth(String authToken) {
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
