package client;

import errors.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.GameName;
import ui.ServerFacade;
import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String facadeUrl =  "http://localhost:" + Integer.toString(port);
        facade = new ServerFacade(facadeUrl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @DisplayName("Register positive")
    public void registerGood() throws ResponseException {
        boolean thrown = false;
        AuthData auth = null;
        UserData user = new UserData("test", "testing321", "test@test.test");
        try {
            facade.clearApp();
            auth = facade.register(user);
        } catch (ResponseException e) {
            thrown = true;
            System.out.print(e.getMessage());
        }
        assert !thrown;
        assert auth != null;
    }

    @Test
    @DisplayName("Register negative")
    public void registerBad() throws ResponseException {
        boolean thrown = false;
        AuthData auth = null;
        UserData user = new UserData("", "testing321", "test@test.test");
        try {
            facade.clearApp();
            auth = facade.register(user);
        } catch (ResponseException e) {
            thrown = true;
            System.out.print(e.getMessage());
        }
        assert thrown;
    }

    @Test
    @DisplayName("Login Positive")
    public void loginGood() throws Exception{
        boolean thrown = false;
        AuthData auth = null;
        UserData user = new UserData("test", "testing321", "test@test.test");
        try {
            facade.clearApp();
            auth = facade.register(user);
            facade.logout(auth.authToken());
            auth = facade.login(user);
        } catch (ResponseException e) {
            thrown = true;
            System.out.print(e.getMessage());
        }
        assert !thrown;
        assert auth != null;
    }

    @Test
    @DisplayName("Login Negative")
    public void loginBad() throws Exception{
        boolean thrown = false;
        UserData user = new UserData("test", "testing321", "test@test.test");
        try {
            facade.clearApp();
            facade.login(user);
        } catch (ResponseException e) {
            thrown = true;
            System.out.print(e.getMessage());
        }
        assert thrown;
    }

    @Test
    @DisplayName("Logout Positive")
    public void logoutGood() {
        boolean thrown = false;
        UserData user = new UserData("test", "testing321", "test@test.test");
        try {
            facade.clearApp();
            AuthData auth = facade.register(user);
            facade.logout(auth.authToken());
        } catch (Exception e){
            thrown = true;
            System.out.print(e.getMessage());
        }
        assert !thrown;
    }

    @Test
    @DisplayName("Logout Negative")
    public void logoutBad() {
        boolean thrown = false;
        AuthData fakeAuth = new AuthData("fake", "fake");
        try {
            facade.clearApp();
            facade.logout(fakeAuth.authToken());
        } catch (Exception e){
            thrown = true;
            System.out.print(e.getMessage());
        }
        assert thrown;
    }

    @Test
    @DisplayName("List Games Positive")
    public void listGamesGood() {
        boolean thrown = false;
        ArrayList<GameData> games = null;
        UserData user = new UserData("test", "testing321", "test@test.test");
        try {
            facade.clearApp();
            AuthData auth = facade.register(user);
            games = facade.listGames(auth.authToken());
        } catch (Exception e){
            thrown = true;
            System.out.print(e.getMessage());
        }
        assert !thrown;
        assert games != null;
    }

    @Test
    @DisplayName("List Games Negative")
    public void listGamesBad() {
        boolean thrown = false;
        ArrayList<GameData> games = null;
        AuthData fakeAuth = new AuthData("fake", "fake");
        try {
            facade.clearApp();
            games = facade.listGames(fakeAuth.authToken());
        } catch (Exception e){
            thrown = true;
            System.out.print(e.getMessage());
        }
        assert thrown;
    }

    @Test
    @DisplayName("Create Game Positive")
    public void createGameGood() {
        boolean thrown = false;
        Integer gameID = null;
        UserData user = new UserData("test", "testing321", "test@test.test");
        GameName gameName = new GameName("test");
        try {
            facade.clearApp();
            AuthData auth = facade.register(user);
            gameID = facade.createGame(auth.authToken(), gameName);
        } catch (Exception e){
            thrown = true;
            System.out.print(e.getMessage());
        }
        assert !thrown;
        assert gameID != null;
    }

    @Test
    @DisplayName("Create Game Positive Two Games")
    public void createGameGoodTwoGames() {
        boolean thrown = false;
        Integer gameID = null;
        Integer gameID2 = null;
        UserData user = new UserData("test", "testing321", "test@test.test");
        GameName gameName = new GameName("test");
        GameName gameName2 = new GameName("test2");
        try {
            facade.clearApp();
            AuthData auth = facade.register(user);
            gameID = facade.createGame(auth.authToken(), gameName);
            gameID2 = facade.createGame(auth.authToken(), gameName2);
        } catch (Exception e){
            thrown = true;
            System.out.print(e.getMessage());
        }
        assert !thrown;
        assert gameID == 1;
        assert gameID2 == 2;
    }

}
