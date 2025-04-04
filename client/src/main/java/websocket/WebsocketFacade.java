package websocket;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import errors.ResponseException;
import ui.GameHandler;
import ui.GamePlayClient;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;

public class WebsocketFacade extends Endpoint implements MessageHandler {

    public Session session;
    public GamePlayClient gamePlayClient;
    public String authToken;
    public Integer gameID;
    public GameHandler gameHandler = new GameHandler() {
        @Override
        public void onLoadGame(LoadGameMessage message) {
            gamePlayClient.onLoadGame(message);
        }

        @Override
        public void onError(ErrorMessage message) {
            gamePlayClient.onError(message);
        }

        @Override
        public void onNotifiy(NotificationMessage message) {
            gamePlayClient.onNotifiy(message);
        }
    };

    public WebsocketFacade(String url, String authToken, Integer gameID, GamePlayClient gamePlayClient) throws ResponseException {
        try {
            this.gamePlayClient = gamePlayClient;
            this.authToken = authToken;
            this.gameID = gameID;
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    if (message.contains("ERROR")) {
                        ErrorMessage notification = new Gson().fromJson(message, ErrorMessage.class);
                        gameHandler.onError(notification);
                    }
                    else if (message.contains("LOAD_GAME")) {
                        LoadGameMessage notification = new Gson().fromJson(message, LoadGameMessage.class);
                        gameHandler.onLoadGame(notification);
                    }
                    else if (message.contains("NOTIFICATION")) {
                        NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                        gameHandler.onNotifiy(notification);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect() throws ResponseException {
        try {
            UserGameCommand connectCom = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(connectCom));
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(ChessMove move) throws ResponseException {
        try {
            MakeMoveCommand makeMoveCommand = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMoveCommand));
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave() throws ResponseException {
        try {
            UserGameCommand leaveCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(leaveCommand));
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign() throws ResponseException {
        try {
            UserGameCommand makeMoveCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMoveCommand));
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
