package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import resultrequest.ErrorResponse;
import resultrequest.JoinGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    JoinGameHandler(AuthDAO authDOA, GameDAO gameDAO) {
        this.authDAO = authDOA;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) {
       Gson gson = new Gson();
       JoinGameRequest req = gson.fromJson(request.body(), JoinGameRequest.class);
       GameService gameService = new GameService(authDAO,gameDAO);
       try {
           gameService.joinGame(req, request.headers("Authorization"));
           response.status(200);
           return gson.toJson(null);
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
           if (e.getMessage().equals("Error: unauthorized")) {
               response.status(401);
               ErrorResponse res = new ErrorResponse("Error: unauthorized");
               return gson.toJson(res);
           }
       }
       return null;
    }
}
