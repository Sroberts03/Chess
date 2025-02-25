package server;

import dataaccess.*;
import spark.*;

public class Server {

    private static final AuthDao AUTH_DAO = new MemoryAuthDao();
    private static final UserDao USER_DAO = new MemoryUserDao();
    private static final GameDao GAME_DAO = new MemoryGameDao();

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
        Spark.delete("/db", new ClearAppHandler(AUTH_DAO,USER_DAO,GAME_DAO)); //clearApp
        Spark.post("/user", new RegisterHandler(AUTH_DAO, USER_DAO)); //register
        Spark.post("/session", new LoginHandler(AUTH_DAO, USER_DAO));//login
        Spark.delete("/session", new LogoutHandler(AUTH_DAO, USER_DAO)); //logout
        Spark.get("/game", new ListGamesHandler(GAME_DAO, AUTH_DAO)); //listGames
        Spark.post("/game", new CreateGameHandler(AUTH_DAO, GAME_DAO)); //createGame
        Spark.put("/game", new JoinGameHandler(AUTH_DAO,GAME_DAO)); //joinGame
    }
}
