package client;

import errors.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


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
    static void stopServerAndClearApp() {
        boolean thrown = false;
        try {
            facade.clearApp();
        } catch (ResponseException e) {
            thrown = true;
        }
        assert !thrown;
        server.stop();
    }

    @Test
    @DisplayName("Register positive")
    public void registerGood() throws ResponseException {
        boolean thrown = false;
        AuthData auth = null;
        UserData user = new UserData("test", "testing321", "test@test.test");
        try {
            auth = facade.register(user);
        } catch (ResponseException e) {
            thrown = true;
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
            auth = facade.register(user);
        } catch (ResponseException e) {
            thrown = true;
        }
        assert thrown;
    }

}
