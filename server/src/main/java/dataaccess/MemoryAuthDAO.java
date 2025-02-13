package dataaccess;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    public Map<String, String> authMap;

    MemoryAuthDAO() {
        authMap = new HashMap<String, String>();
    }

    public void createAuth(String authToken, String username) {
        authMap.put(authToken, username);
    }

    public String getAuth(String authToken) throws DataAccessException {
        if (!authMap.containsKey(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        return authMap.get(authToken);
    }

    public void removeAuth(String authToken) {
        authMap.remove(authToken);
    }
}
