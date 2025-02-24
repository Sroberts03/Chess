package server;

import dataaccess.*;
import spark.*;

public class Server {

    private static final AuthDAO authDAO = new MemoryAuthDAO();
    private static final UserDAO userDAO = new MemoryUserDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void createRoutes() {
        Spark.post("/user", new RegisterHandler(authDAO, userDAO)); //register
        Spark.post("/session", new LoginHandler()); //login
        Spark.delete("/session", new LogoutHandler()); //logout
        Spark.get("/game", new ListGamesHandler()); //listGames
        Spark.post("/game", new CreateGameHandler()); //createGame
        Spark.put("/game", new JoinGameHandler()); //joinGame
        Spark.delete("/db", new ClearAppHandler()); //clearApp
    }
}
