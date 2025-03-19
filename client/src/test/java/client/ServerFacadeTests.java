package client;

import errors.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.util.Arrays;


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




}
