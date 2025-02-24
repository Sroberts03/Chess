package server;

import dataaccess.*;
import spark.*;

public class Server {

    private static final AuthDAO authDAO = new MemoryAuthDAO();
    private static final UserDAO userDAO = new MemoryUserDAO();
    private static final GameDAO gameDAO = new MemoryGameDAO();

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
        Spark.delete("/db", new ClearAppHandler(authDAO,userDAO,gameDAO)); //clearApp
        Spark.post("/user", new RegisterHandler(authDAO, userDAO)); //register
        Spark.post("/session", new LoginHandler(authDAO, userDAO));//login
        Spark.delete("/session", new LogoutHandler(authDAO, userDAO)); //logout
        Spark.get("/game", new ListGamesHandler(gameDAO, authDAO)); //listGames
        Spark.post("/game", new CreateGameHandler(authDAO, gameDAO)); //createGame
        Spark.put("/game", new JoinGameHandler(authDAO,gameDAO)); //joinGame
    }
}
