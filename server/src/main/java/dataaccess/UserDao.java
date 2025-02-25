package dataaccess;

import model.UserData;

import java.util.Map;

public interface UserDao {

    void createUser(UserData user);

    UserData getUser(String username);

    void clearUser();

    Map<String, UserData> getUserMap();

}
