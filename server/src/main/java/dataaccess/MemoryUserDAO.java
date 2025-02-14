package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    public static Map<String, UserData> userMap = new HashMap<>();

    public static void createUser(UserData user) {
        String username = user.username();
        userMap.put(username, user);
    }

    public static UserData getUser(String username) {
        return userMap.get(username);
    }

    public static void clearUser() {
        userMap.clear();
    }

}
