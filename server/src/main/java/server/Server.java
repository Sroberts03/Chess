package server;

import dataaccess.*;
import spark.*;

public class Server {

    private static final AuthDao AUTH_DAO;
    private static final UserDao USER_DAO;
    private static final GameDao GAME_DAO;

    static {
        try {
            AUTH_DAO = new SqlAuthDao();
            USER_DAO = new SqlUserDao();
            GAME_DAO = new SqlGameDao();
        } catch (DataAccessException e) {
            throw new RuntimeException();
        }
    }

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
