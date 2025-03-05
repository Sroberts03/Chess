package server;

import com.google.gson.Gson;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import resultrequest.CreateGameRequest;
import resultrequest.CreateGameResult;
import resultrequest.ErrorResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {

    private final AuthDao authDao;
    private final GameDao gameDao;

    CreateGameHandler(AuthDao authDao, GameDao gameDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    @Override
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        CreateGameRequest req = gson.fromJson(request.body(), CreateGameRequest.class);
        GameService gameService = new GameService(authDao, gameDao);
        try {
            CreateGameResult res = gameService.createGame(req, request.headers("Authorization"));
            response.status(200);
            return gson.toJson(res);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                response.status(401);
                ErrorResponse error = new ErrorResponse("Error: unauthorized");
                return gson.toJson(error);
            }
            if (e.getMessage().equals("Error: bad request")) {
                response.status(400);
                ErrorResponse error = new ErrorResponse("Error: bad request");
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
