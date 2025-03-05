package server;

import com.google.gson.Gson;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.UserDao;
import resultrequest.ErrorResponse;
import resultrequest.LoginRequest;
import resultrequest.LoginResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {

    private final UserDao userDao;
    private final AuthDao authDao;

    LoginHandler(AuthDao authDao, UserDao userDao) {
        this.authDao = authDao;
        this.userDao = userDao;
    }

    @Override
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        LoginRequest req = gson.fromJson(request.body(), LoginRequest.class);
        UserService userService = new UserService(authDao,userDao);
        try {
            LoginResult result = userService.login(req);
            Object res = gson.toJson(result);
            response.status(200);
            return res;
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                response.status(401);
                ErrorResponse error = new ErrorResponse("Error: unauthorized");
                return gson.toJson(error);
            }
            else {
                response.status(500);
                ErrorResponse error = new ErrorResponse("Error: " + e.getMessage());
                return gson.toJson(error);
            }
        }
    }
}
