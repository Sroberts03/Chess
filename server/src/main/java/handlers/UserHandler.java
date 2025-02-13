package handlers;

import com.google.gson.*;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import resultrequest.LoginRequest;
import resultrequest.LoginResult;
import static service.UserService.login;


public class UserHandler {

    public static String loginHandler(JsonObject json) throws DataAccessException {
        var serializer = new Gson();
        LoginRequest loginRequest = serializer.fromJson(json, LoginRequest.class);
        LoginResult result = login(loginRequest);
        return serializer.toJson(result);
    }
}
