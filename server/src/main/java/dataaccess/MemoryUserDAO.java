package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private final Map<String, UserData> userMap = new HashMap<>();

    public void createUser(UserData user) {
        String username = user.username();
        userMap.put(username, user);
    }

    public UserData getUser(String username) {
        return userMap.get(username);
    }

    public void clearUser() {
        this.userMap.clear();
    }

    public Map<String, UserData> getUserMap() {
        return userMap;
    }

}
