package server;

import dataaccess.AuthDAO;
import dataaccess.Error401;
import model.AuthData;


public class Authorized {

    public static Boolean authorized(AuthDAO authDAO, String token) {
        AuthData auth = authDAO.getAuth(token);
        return auth != null;
    }
}
