package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        createRoutes();

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

//    public static void createRoutes() {
//        Spark.post("/user", registerHandler); //register
//        Spark.post("/session", ); //login
//        Spark.delete("/session", ); //logout
//        Spark.get("/game", ); //listGames
//        Spark.post("/game", ); //createGame
//        Spark.put("/game", ); //joinGame
//        Spark.delete("/db", ); //clearApp
//    }
}
