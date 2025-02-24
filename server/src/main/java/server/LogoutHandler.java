package server;

import com.google.gson.Gson;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.UserDao;
import resultrequest.ErrorResponse;
import resultrequest.LogoutRequest;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {

    private final UserDao userDao;
    private final AuthDao authDao;

    LogoutHandler(AuthDao authDao, UserDao userDao){
        this.userDao = userDao;
        this.authDao = authDao;
    }

    @Override
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        LogoutRequest req = new LogoutRequest(request.headers("Authorization"));
        UserService logout = new UserService(authDao, userDao);
        try {
            logout.logout(req);
            Object res = gson.toJson(null);
            response.status(200);
            return res;
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                response.status(401);
                ErrorResponse res = new ErrorResponse("Error: unauthorized");
                return gson.toJson(res);
            }
        }
        return null;
    }
}
