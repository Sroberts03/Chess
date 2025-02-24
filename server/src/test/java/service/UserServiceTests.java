package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import resultrequest.*;

public class UserServiceTests {

    private final GameDAO gameDAO = new MemoryGameDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final UserDAO userDAO = new MemoryUserDAO();
    private final UserService userService = new UserService(authDAO,userDAO);
    private final ClearAppService clearApp = new ClearAppService(gameDAO,authDAO,userDAO);

    @Test
    @DisplayName("register positive")
    public void registerTest() throws DataAccessException {
        clearApp.clearApp();
        RegisterRequest request = new RegisterRequest("sam", "123123", "123@123.123");
        boolean thrown = false;
        RegisterResult result = null;
        try {
            result = userService.register(request);
        } catch (DataAccessException e) {
            thrown = true;
        }
        UserData expectedUser = new UserData("sam", "123123", "123@123.123");
        assert !thrown;
        assert userDAO.getUserMap().containsValue(expectedUser);
        assert authDAO.getAuthMap().containsKey(result.authToken());
        assert authDAO.getAuth(result.authToken()).username().equals(result.username());
    }

    @Test
    @DisplayName("register user already exists")
    public void registerTestUserAlreadyExists() throws DataAccessException {
        clearApp.clearApp();
        UserData user = new UserData("sam", "123123", "123@123.123");
        userDAO.createUser(user);
        RegisterRequest request = new RegisterRequest("sam", "123123", "123@123.123");
        boolean thrown = false;
        try {
            userService.register(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: already taken")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("register Bad Request Empty Password")
    public void registerTestBadRequestEmptyPassword() throws DataAccessException {
        clearApp.clearApp();
        RegisterRequest request = new RegisterRequest("sam", "", "123@123.123");
        boolean thrown = false;
        try {
            userService.register(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("register Bad Request Empty Username")
    public void registerTestBadRequestEmptyUsername() throws DataAccessException {
        clearApp.clearApp();
        RegisterRequest request = new RegisterRequest("", "123123", "123@123.123");
        boolean thrown = false;
        try {
            userService.register(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("register Bad Request Empty Email")
    public void registerTestBadRequestEmptyEmail() throws DataAccessException {
        clearApp.clearApp();
        RegisterRequest request = new RegisterRequest("sam", "123123", "");
        boolean thrown = false;
        try {
            userService.register(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("login positive")
    public void loginTest() throws DataAccessException {
        clearApp.clearApp();
        UserData user = new UserData("samTest","testing123", "123@123.123");
        userDAO.createUser(user);
        assert userDAO.getUserMap().containsValue(user);
        LoginRequest loginRequest = new LoginRequest(user.username(),user.password());
        boolean thrown = false;
        LoginResult loginResult = null;
        try {
            loginResult = userService.login(loginRequest);
        } catch (DataAccessException d) {
            thrown = true;
        }
        assert !thrown;
        assert authDAO.getAuthMap().containsKey(loginResult.authToken());
        assert authDAO.getAuth(loginResult.authToken()).username().equals(user.username());
    }

    @Test
    @DisplayName("login bad password")
    public void loginTestBadPassword() throws DataAccessException {
        clearApp.clearApp();
        UserData user = new UserData("samTest","testing123", "123@123.123");
        userDAO.createUser(user);
        assert userDAO.getUserMap().containsValue(user);
        boolean thrown = false;
        LoginRequest loginRequest = new LoginRequest(user.username(), "12341234");
        try {
            userService.login(loginRequest);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("login bad username")
    public void loginTestBadUsername() throws DataAccessException {
        clearApp.clearApp();
        UserData user = new UserData("samTest","testing123", "123@123.123");
        userDAO.createUser(user);
        assert userDAO.getUserMap().containsValue(user);
        boolean thrown = false;
        LoginRequest loginRequest = new LoginRequest("badUsername", user.password());
        try {
            userService.login(loginRequest);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("login bad username and bad password")
    public void loginTestBadUsernameAndBadPassword() throws DataAccessException {
        clearApp.clearApp();
        UserData user = new UserData("samTest","testing123", "123@123.123");
        userDAO.createUser(user);
        assert userDAO.getUserMap().containsValue(user);
        boolean thrown = false;
        LoginRequest loginRequest = new LoginRequest("badUsername", "123");
        try {
            userService.login(loginRequest);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("logout good auth")
    public void logoutGoodAuth() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        authDAO.createAuth(auth);
        LogoutRequest request = new LogoutRequest(auth.authToken());
        boolean thrown = false;
        try {
            userService.logout(request);
        } catch (DataAccessException d) {
            thrown = true;
        }
        assert !thrown;
        assert !authDAO.getAuthMap().containsKey(auth.authToken());
    }

    @Test
    @DisplayName("logout bad auth")
    public void logoutTestBadAuth() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        LogoutRequest request = new LogoutRequest(auth.authToken());
        boolean thrown = false;
        try {
            userService.logout(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                thrown = true;
            }
        }
        assert thrown;
    }
}
