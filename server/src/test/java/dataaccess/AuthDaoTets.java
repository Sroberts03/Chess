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
}
