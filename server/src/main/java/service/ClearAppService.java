package service;

import dataaccess.*;

public class ClearAppService {

    private final GameDao gameDAO;
    private final AuthDao authDAO;
    private final UserDao userDAO;

    public ClearAppService(GameDao gameDAO, AuthDao authDAO, UserDao userDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public void clearApp() throws DataAccessException {
        authDAO.clearAuth();
        gameDAO.clearGame();
        userDAO.clearUser();
    }
}
