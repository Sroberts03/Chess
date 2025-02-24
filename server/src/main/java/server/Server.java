package server;

import dataaccess.*;
import spark.*;

public class Server {

    private static final AuthDao authDao = new MemoryAuthDao();
    private static final UserDao userDao = new MemoryUserDao();
    private static final GameDao gameDao = new MemoryGameDao();

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
        Spark.delete("/db", new ClearAppHandler(authDao,userDao,gameDao)); //clearApp
        Spark.post("/user", new RegisterHandler(authDao, userDao)); //register
        Spark.post("/session", new LoginHandler(authDao, userDao));//login
        Spark.delete("/session", new LogoutHandler(authDao, userDao)); //logout
        Spark.get("/game", new ListGamesHandler(gameDao, authDao)); //listGames
        Spark.post("/game", new CreateGameHandler(authDao, gameDao)); //createGame
        Spark.put("/game", new JoinGameHandler(authDao,gameDao)); //joinGame
    }
}
