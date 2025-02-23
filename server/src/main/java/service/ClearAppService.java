package service;

import dataaccess.*;

public class ClearAppService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    ClearAppService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public void clearApp()  {
        authDAO.clearAuth();
        gameDAO.clearGame();
        userDAO.clearUser();
    }
}
