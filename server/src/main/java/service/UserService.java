package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import resultrequest.*;
import java.util.Objects;
import static dataaccess.MemoryAuthDAO.createAuth;
import static dataaccess.MemoryUserDAO.generateToken;
import static dataaccess.MemoryUserDAO.getUser;

public class UserService {

    public static RegisterResult register(RegisterRequest registerRequest) {
        return null;
    }

    public static LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        String password = loginRequest.password();
        UserData user = getUser(username);
        if (!Objects.equals(user.password(), password)) {
            throw new DataAccessException("Error: unauthorized");
        }
        AuthData auth = new AuthData(generateToken(),username);
        createAuth(auth);
        return new LoginResult(username, auth.authToken());
    }

    public static void logout(LogoutRequest logoutRequest) {

    }

}
