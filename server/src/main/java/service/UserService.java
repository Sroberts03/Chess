package service;

import dataaccess.DataAccessException;
import dataaccess.Error400;
import dataaccess.Error401;
import dataaccess.Error403;
import model.AuthData;
import model.UserData;
import resultrequest.*;
import static dataaccess.MemoryAuthDAO.*;
import static dataaccess.MemoryUserDAO.createUser;
import static dataaccess.MemoryUserDAO.getUser;

public class UserService {

    public static RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        UserData user = getUser(username);
        AuthData newAuth = null;
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            throw new Error400("Error: bad request");
        }
        if (user == null) {
            UserData newUser = new UserData(username, password, email);
            createUser(newUser);
            newAuth = new AuthData(generateToken(), username);
            createAuth(newAuth);
        }
        if (user != null) {
            throw new Error403("Error: already taken");
        }
        return new RegisterResult(username, newAuth.authToken());
    }

    public static LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        String password = loginRequest.password();
        UserData user = getUser(username);
        if (user == null) {
            throw new Error401("Error: unauthorized");
        }
        if (!user.password().equals(password)) {
            throw new Error401("Error: unauthorized");
        }
        AuthData auth = new AuthData(generateToken(),username);
        createAuth(auth);
        return new LoginResult(username, auth.authToken());
    }

    public static void logout(LogoutRequest logoutRequest) throws DataAccessException {
        String auth = logoutRequest.authToken();
        AuthData otherAuth = getAuth(auth);
        if (otherAuth == null) {
            throw new Error401("Error: unauthorized");
        }
        removeAuth(auth);
    }

}
