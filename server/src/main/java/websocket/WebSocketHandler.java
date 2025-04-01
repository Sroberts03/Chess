package websocket;

import com.google.gson.Gson;
import dataaccess.AuthDao;
import dataaccess.GameDao;
import dataaccess.UserDao;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

    private final AuthDao authDao;
    private final GameDao gameDao;
    private final UserDao userDao;
    private final ConnectionManager connections;


    public WebSocketHandler(AuthDao authDao, GameDao gameDao, UserDao userDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
        this.userDao = userDao;
        connections = new ConnectionManager();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {

    }

    @OnWebSocketClose
    public void onClose(Session session) {

    }

    @OnWebSocketError
    public void onError(Throwable throwable) {

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        //1. Determine message type
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        //2. call a method
        switch (command.getCommandType()) {
            case CONNECT -> connect(command);
            case MAKE_MOVE -> makeMove(command);
            case LEAVE -> leave(command);
            case RESIGN -> resign(command);
        }
    }

    public void connect(UserGameCommand command) {

    }

    public void makeMove(UserGameCommand command) {

    }

    public void leave(UserGameCommand command) {

    }

    public void resign(UserGameCommand command) {

    }
}
