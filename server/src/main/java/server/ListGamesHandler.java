package server;

import com.google.gson.Gson;
import dataaccess.AuthDao;
import dataaccess.DataAccessException;
import dataaccess.GameDao;
import resultrequest.ErrorResponse;
import resultrequest.GameListRequest;
import resultrequest.GameListResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGamesHandler implements Route {

    private final GameDao gameDao;
    private final AuthDao authDao;

    ListGamesHandler(GameDao gameDao, AuthDao authDao){
        this.gameDao = gameDao;
        this.authDao = authDao;
    }

    @Override
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        GameListRequest req = new GameListRequest(request.headers("Authorization"));
        GameService gameService = new GameService(authDao, gameDao);
        try {
            GameListResult res = gameService.getGames(req);
            response.status(200);
            return gson.toJson(res);
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
