package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import resultrequest.*;

import java.util.Objects;

import static dataaccess.MemoryAuthDAO.*;
import static dataaccess.MemoryUserDAO.createUser;
import static dataaccess.MemoryUserDAO.userMap;
import static service.ClearAppService.clearApp;
import static service.UserService.*;

public class UserServiceTests {

    @Test
    @DisplayName("register positive")
    public void registerTest() {
        clearApp();
        RegisterRequest request = new RegisterRequest("sam", "123123", "123@123.123");
        boolean thrown = false;
        RegisterResult result = null;
        try {
            result = register(request);
        } catch (DataAccessException e) {
            thrown = true;
        }
        UserData expectedUser = new UserData("sam", "123123", "123@123.123");
        assert !thrown;
        assert userMap.containsValue(expectedUser);
        assert authMap.containsKey(result.authToken());
        assert getAuth(result.authToken()).equals(result.username());
    }

    @Test
    @DisplayName("register user already exists")
    public void registerTestUserAlreadyExists() {
        clearApp();
        UserData user = new UserData("sam", "123123", "123@123.123");
        createUser(user);
        RegisterRequest request = new RegisterRequest("sam", "123123", "123@123.123");
        boolean thrown = false;
        try {
            register(request);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert thrown;
    }

    @Test
    @DisplayName("login positive")
    public void loginTest() {
        clearApp();
        UserData user = new UserData("samTest","testing123", "123@123.123");
        createUser(user);
        assert userMap.containsValue(user);
        LoginRequest loginRequest = new LoginRequest(user.username(),user.password());
        boolean thrown = false;
        LoginResult loginResult = null;
        try {
            loginResult = login(loginRequest);
        } catch (DataAccessException d) {
            thrown = true;
        }
        assert !thrown;
        assert authMap.containsKey(loginResult.authToken());
        assert Objects.equals(getAuth(loginResult.authToken()), user.username());
    }

    @Test
    @DisplayName("login bad password")
    public void loginTestBadPassword() {
        clearApp();
        UserData user = new UserData("samTest","testing123", "123@123.123");
        createUser(user);
        assert userMap.containsValue(user);
        boolean thrown = false;
        LoginRequest loginRequest = new LoginRequest(user.username(), "12341234");
        try {
            login(loginRequest);
        } catch (DataAccessException d) {
            thrown = true;
        }
        assert thrown;
    }

    @Test
    @DisplayName("login bad username")
    public void loginTestBadUsername() {
        clearApp();
        UserData user = new UserData("samTest","testing123", "123@123.123");
        createUser(user);
        assert userMap.containsValue(user);
        boolean thrown = false;
        LoginRequest loginRequest = new LoginRequest("badUsername", user.password());
        try {
            login(loginRequest);
        } catch (DataAccessException d) {
            thrown = true;
        }
        assert thrown;
    }

    @Test
    @DisplayName("login bad username and bad password")
    public void loginTestBadUsernameAndBadPassword() {
        clearApp();
        UserData user = new UserData("samTest","testing123", "123@123.123");
        createUser(user);
        assert userMap.containsValue(user);
        boolean thrown = false;
        LoginRequest loginRequest = new LoginRequest("badUsername", "123");
        try {
            login(loginRequest);
        } catch (DataAccessException d) {
            thrown = true;
        }
        assert thrown;
    }

    @Test
    @DisplayName("logout good auth")
    public void logoutGoodAuth() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        createAuth(auth);
        LogoutRequest request = new LogoutRequest(auth.authToken());
        boolean thrown = false;
        try {
            logout(request);
        } catch (DataAccessException d) {
            thrown = true;
        }
        assert !thrown;
        assert !authMap.containsKey(auth.authToken());
    }

    @Test
    @DisplayName("logout bad auth")
    public void logoutTestBadAuth() {
        clearApp();
        AuthData auth = new AuthData("123", "sam");
        LogoutRequest request = new LogoutRequest(auth.authToken());
        boolean thrown = false;
        try {
            logout(request);
        } catch (DataAccessException d) {
            thrown = true;
        }
        assert thrown;
    }
}
