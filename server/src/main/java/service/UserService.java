package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import resultrequest.*;

public class UserService {

    private final AuthDao authDAO;
    private final UserDao userDAO;

    public UserService(AuthDao authDAO, UserDao userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        UserData user = userDAO.getUser(username);
        AuthData newAuth = null;
        if ((username == null || username.isEmpty())
                || (password == null || password.isEmpty())
                || (email == null || email.isEmpty())) {
            throw new Error400("Error: bad request");
        }
        if (user == null) {
            UserData newUser = new UserData(username, password, email);
            userDAO.createUser(newUser);
            newAuth = new AuthData(authDAO.generateToken(), username);
            authDAO.createAuth(newAuth);
        }
        if (user != null) {
            throw new Error403("Error: already taken");
        }
        return new RegisterResult(username, newAuth.authToken());
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        String password = loginRequest.password();
        UserData user = userDAO.getUser(username);
        if (user == null) {
            throw new Error401("Error: unauthorized");
        }
        if (!BCrypt.checkpw(password, user.password())) {
            throw new Error401("Error: unauthorized");
        }
        AuthData auth = new AuthData(authDAO.generateToken(),username);
        authDAO.createAuth(auth);
        return new LoginResult(username, auth.authToken());
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        String auth = logoutRequest.authToken();
        AuthData otherAuth = authDAO.getAuth(auth);
        if (otherAuth == null) {
            throw new Error401("Error: unauthorized");
        }
        authDAO.removeAuth(auth);
    }

}
