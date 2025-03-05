package server;

import com.google.gson.Gson;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.UserDao;
import resultrequest.ErrorResponse;
import resultrequest.RegisterRequest;
import resultrequest.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {

    private final UserDao userDao;
    private final AuthDao authDao;

    public RegisterHandler(AuthDao authDao, UserDao userDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    @Override
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        RegisterRequest req = gson.fromJson(request.body(), RegisterRequest.class);
        UserService register = new UserService(authDao,userDao);
        try {
            RegisterResult result = register.register(req);
            Object res = gson.toJson(result);
            response.status(200);
            return res;
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                response.status(400);
                ErrorResponse error = new ErrorResponse("Error: bad request");
                return gson.toJson(error);
            }
            if (e.getMessage().equals("Error: already taken")) {
                response.status(403);
                ErrorResponse error = new ErrorResponse("Error: already taken");
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
