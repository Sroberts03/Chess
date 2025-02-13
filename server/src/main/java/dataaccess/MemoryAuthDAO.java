package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    public static Map<String, String> authMap;

    MemoryAuthDAO() {
        authMap = new HashMap<String, String>();
    }

    public static void createAuth(AuthData authData) {
        String username = authData.username();
        String authToken = authData.authToken();
        authMap.put(authToken, username);
    }

    public static String getAuth(String authToken) throws DataAccessException {
        if (!authMap.containsKey(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        return authMap.get(authToken);
    }

    public static void removeAuth(String authToken) {
        authMap.remove(authToken);
    }
}
