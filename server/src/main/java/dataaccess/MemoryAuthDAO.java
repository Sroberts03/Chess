package dataaccess;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    public Map<String, String> authMap;

    MemoryAuthDAO() {
        authMap = new HashMap<String, String>();
    }

    public void createAuth(String authToken, String username) throws DataAccessException {
        authMap.put(authToken, username);
    }

    public String getAuth(String authToken) throws DataAccessException {
        try {
            return authMap.get(authToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAuth(String authToken) throws DataAccessException {
        try {
            authMap.remove(authToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
