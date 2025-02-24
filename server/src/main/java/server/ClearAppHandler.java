package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import resultrequest.ErrorResponse;
import service.ClearAppService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearAppHandler implements Route {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    ClearAppHandler(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        ClearAppService clearApp = new ClearAppService(gameDAO,authDAO,userDAO);
        try{
            clearApp.clearApp();
            Object res = gson.toJson(null);
            response.status(200);
            return res;
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: (description of error)")) {
                response.status(500);
                ErrorResponse res = new ErrorResponse("Error: (description of error)");
                return gson.toJson(res);
            }
        }
        return null;
    }
}
