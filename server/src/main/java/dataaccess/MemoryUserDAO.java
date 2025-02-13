package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO {

    public static Map<String, UserData> userMap;

    public MemoryUserDAO () {
        userMap = new HashMap<>();
    }

    public static void createUser(UserData user) throws DataAccessException{
        String username = user.username();
        if (userMap.containsKey(username)) {
            throw new DataAccessException("Error: already taken");
        }
        userMap.put(username, user);
    }

    public static UserData getUser(String username) throws DataAccessException {
        if (!userMap.containsKey(username)) {
            throw new DataAccessException("Error: unauthorized");
        }
        return userMap.get(username);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
