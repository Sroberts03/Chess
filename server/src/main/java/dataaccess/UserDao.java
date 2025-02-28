package dataaccess;

import model.UserData;

import java.util.Map;

public interface UserDao {

    void createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clearUser() throws DataAccessException;

    Map<String, UserData> getUserMap();

}
