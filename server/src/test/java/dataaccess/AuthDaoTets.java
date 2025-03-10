package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthDaoTets {

    AuthDao sqlAuthDao = new SqlAuthDao();

    public AuthDaoTets() throws DataAccessException {
    }

    @Test
    @DisplayName("create auth good")
    public void createAuthGood() {
        boolean thrown = false;
        try {
            sqlAuthDao.clearAuth();
            AuthData testAuth = new AuthData(sqlAuthDao.generateToken(), "testUser");
            sqlAuthDao.createAuth(testAuth);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
    }

    @Test
    @DisplayName("create auth bad")
    public void createAuthBad() {
        boolean thrown = false;
        try {
            sqlAuthDao.clearAuth();
            AuthData testAuth = new AuthData(sqlAuthDao.generateToken(), "testUser");
            sqlAuthDao.createAuth(testAuth);
            sqlAuthDao.createAuth(testAuth);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert thrown;
    }

    @Test
    @DisplayName("get auth good")
    public void getAuthGood() {
        boolean thrown = false;
        AuthData testAuth = null;
        AuthData auth = null;
        try {
            sqlAuthDao.clearAuth();
            testAuth = new AuthData(sqlAuthDao.generateToken(), "testUser");
            sqlAuthDao.createAuth(testAuth);
            auth = sqlAuthDao.getAuth(testAuth.authToken());
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
        assert auth.equals(testAuth);
    }

    @Test
    @DisplayName("get auth bad")
    public void getAuthBad() {
        boolean thrown = false;
        AuthData testAuth = null;
        try {
            sqlAuthDao.clearAuth();
            testAuth = new AuthData(sqlAuthDao.generateToken(), "testUser");
            sqlAuthDao.createAuth(testAuth);
            sqlAuthDao.getAuth("FakeToken");
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert thrown;
    }

    @Test
    @DisplayName("remove auth good")
    public void removeAuthGood() {
        boolean thrown = false;
        AuthData testAuth = null;
        try {
            sqlAuthDao.clearAuth();
            testAuth = new AuthData(sqlAuthDao.generateToken(), "testUser");
            sqlAuthDao.createAuth(testAuth);
            sqlAuthDao.removeAuth(testAuth.authToken());
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
    }

    @Test
    @DisplayName("remove auth bad")
    public void removeAuthBad() {
        boolean thrown = false;
        AuthData testAuth = null;
        try {
            sqlAuthDao.clearAuth();
            testAuth = new AuthData(sqlAuthDao.generateToken(), "testUser");
            sqlAuthDao.createAuth(testAuth);
            sqlAuthDao.createAuth(testAuth);
            sqlAuthDao.removeAuth("fakeAuth");
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert thrown;
    }

    @Test
    @DisplayName("clear user good")
    public void clearUserTestGood() throws DataAccessException {
        boolean thrown = false;
        try {
            sqlAuthDao.clearAuth();
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
            sqlAuthDao.clearAuth();
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
    }
}
