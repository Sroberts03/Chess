package server;

import com.google.gson.Gson;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import dataaccess.UserDao;
import resultrequest.ErrorResponse;
import service.ClearAppService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearAppHandler implements Route {

    private final UserDao userDao;
    private final AuthDao authDao;
    private final GameDao gameDao;

    ClearAppHandler(AuthDao authDao, UserDao userDao, GameDao gameDao) {
        this.authDao = authDao;
        this.userDao = userDao;
        this.gameDao = gameDao;
    }

    @Override
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        ClearAppService clearApp = new ClearAppService(gameDao,authDao,userDao);
        try{
            clearApp.clearApp();
            Object res = gson.toJson(null);
            response.status(200);
            return res;
        } catch (DataAccessException e) {
            response.status(500);
            ErrorResponse error = new ErrorResponse("Error: " + e.getMessage());
            return gson.toJson(error);
        }
    }
}
