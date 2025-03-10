package dataaccess;

import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserDaoTests {

    private final SqlUserDao sqlDao = new SqlUserDao();

    public UserDaoTests() throws DataAccessException {
    }

    @Test
    @DisplayName("Create User good")
    public void createUserTestGood() throws DataAccessException {
        boolean thrown = false;
        try {
            sqlDao.clearUser();
            UserData testUser = new UserData("testSam", "testing123", "sam@testing.com");
            sqlDao.createUser(testUser);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
    }

    @Test
    @DisplayName("Create User Bad")
    public void createUserTestBad() throws DataAccessException {
        boolean thrown = false;
        try {
            sqlDao.clearUser();
            UserData testUser = new UserData("testSam", "testing123", "sam@testing.com");
            sqlDao.createUser(testUser);
            sqlDao.createUser(testUser);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert thrown;
    }

    @Test
    @DisplayName("get User good")
    public void getUserTestGood() throws DataAccessException {
        boolean thrown = false;
        UserData testUser = null;
        UserData user = null;
        try {
            sqlDao.clearUser();
            testUser = new UserData("testSam", "testing123", "sam@testing.com");
            sqlDao.createUser(testUser);
            sqlDao.getUser("testSam");
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
    }

    @Test
    @DisplayName("get User bad")
    public void getBadUserTest() throws DataAccessException {
        boolean thrown = false;
        UserData testUser = null;
        UserData user = null;
        try {
            sqlDao.clearUser();
            user = sqlDao.getUser("testSam");
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert user == null;
    }

    @Test
    @DisplayName("clear user good")
    public void clearUserTestGood() throws DataAccessException {
        boolean thrown = false;
        try {
            sqlDao.clearUser();
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
    }

    @Test
    @DisplayName("clear user bad")
    public void clearUserTestBad() throws DataAccessException {
        boolean thrown = false;
        try {
            sqlDao.clearUser();
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
    }
}
