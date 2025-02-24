package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import resultrequest.ErrorResponse;
import resultrequest.GameListRequest;
import resultrequest.GameListResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGamesHandler implements Route {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    ListGamesHandler(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) {
        Gson gson = new Gson();
        GameListRequest req = new GameListRequest(request.headers("Authorization"));
        GameService gameService = new GameService(authDAO, gameDAO);
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
        }
        return null;
    }
}
