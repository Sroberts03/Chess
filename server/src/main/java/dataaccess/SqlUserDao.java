package dataaccess;

import model.UserData;

public abstract class SqlUserDao implements UserDao{
    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUser() throws DataAccessException {

    }
}
