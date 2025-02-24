package server;

import spark.Request;
import spark.Response;
import spark.Route;

public class ListGamesHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.status(200);
        response.type("text/plain");
        response.header("CS240", "testing");
        response.body("Testing123");
        return response;
    }
}
