package service;

import static dataaccess.MemoryAuthDAO.clearAuth;
import static dataaccess.MemoryGameDAO.clearGame;
import static dataaccess.MemoryUserDAO.clearUser;

public class ClearAppService {

    public static void clearApp()  {
        clearAuth();
        clearGame();
        clearUser();
    }
}
