package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthDao;
import dataaccess.GameDao;
import dataaccess.UserDao;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private AuthDao authDao;
    private GameDao gameDao;
    private UserDao userDao;
    private ConnectionManager connections;

    public void webSocketSetter(AuthDao authDao, GameDao gameDao, UserDao userDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
        this.userDao = userDao;
        connections = new ConnectionManager();
    }

    public void onError(Throwable throwable, Session session) throws IOException {
        ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, throwable.getMessage());
        sendErrorMessage(message, session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        if (message.contains("move")) {
            MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
            makeMove(command, session);
        } else {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> connect(command, session);
                case LEAVE -> leave(command, session);
                case RESIGN -> resign(command, session);
            }
        }
    }

    public void connect(UserGameCommand command, Session session) throws IOException {
        try {
            String playerColor = "";
            Integer gameID = command.getGameID();
            String username = authDao.getAuth(command.getAuthToken()).username();
            GameData gameData = gameDao.getGame(gameID);
            NotificationMessage notificationMessage;
            if (!(gameData.blackUsername() == null) &&
                    gameData.blackUsername().equals(username)){
                playerColor = "Black";
            }
            else if (!(gameData.whiteUsername() == null) &&
                    gameData.whiteUsername().equals(username)) {
                playerColor = "White";
            }
            connections.addSessionToGame(gameID, session);
            ChessGame game = gameDao.getGame(gameID).game();
            if (!playerColor.isEmpty()) {
                notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        username + " has joined the game as " + playerColor);
            }
            else {
                notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        username + " has joined the game as an observer");
            }
            broadcastNotificationMessage(gameID, notificationMessage, session);
            sendLoadGameMessage(new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game), session);
        } catch (Exception ex) {
            onError(ex, session);
        }
    }

    public void makeMove(MakeMoveCommand command, Session session) throws IOException {
        try {
            ChessGame.TeamColor playerColor = ChessGame.TeamColor.WHITE;
            boolean observer = false;
            String username = authDao.getAuth(command.getAuthToken()).username();
            ChessMove move = command.getMove();
            GameData oldGameData = gameDao.getGame(command.getGameID());
            ChessGame game = oldGameData.game();
            if (!(oldGameData.blackUsername() == null) &&
                    oldGameData.blackUsername().equals(username)){
                playerColor = ChessGame.TeamColor.BLACK;
            }
            else if (!(oldGameData.whiteUsername() == null) &&
                    oldGameData.whiteUsername().equals(username)) {
                playerColor = ChessGame.TeamColor.WHITE;
            }
            else {
                observer = true;
            }
            if (observer) {
                throw new InvalidMoveException("Silly you can't move pieces, you're just watching.");
            }
            int startRow = move.getStartPosition().getRow();
            int startColumn = move.getStartPosition().getColumn();
            ChessPiece movingPiece = game.chessBoard.getPiece(new ChessPosition(startRow, startColumn));
            System.out.print(movingPiece.getTeamColor());
            if (!movingPiece.getTeamColor().equals(playerColor)) {
                throw new InvalidMoveException("You can't move your opponents piece");
            }
            game.makeMove(move);
            GameData newGameData = new GameData(oldGameData.gameID(),oldGameData.whiteUsername(),oldGameData.blackUsername(),
                    oldGameData.gameName(), game);
            gameDao.updateGame(newGameData);
            LoadGameMessage message = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            sendLoadGameMessage(message, session);
            broadcastLoadGameMessage(command.getGameID(), message, session);
            NotificationMessage notifyMoveMade = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    username + "made move: " + move);
            broadcastNotificationMessage(command.getGameID(), notifyMoveMade, session);
            if (game.isInCheckmate(game.getTeamTurn())) {
                NotificationMessage inCheckmate = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        game.getTeamTurn().name() + "is in checkmate. Game Over");
                broadcastNotificationMessage(command.getGameID(), inCheckmate, session);
                sendNotificationMessage(inCheckmate,session);
            }
            else if (game.isInCheck(game.getTeamTurn())) {
                NotificationMessage inCheckmate = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        game.getTeamTurn().name() + "is in check.");
                broadcastNotificationMessage(command.getGameID(), inCheckmate, session);
                sendNotificationMessage(inCheckmate,session);
            }
            else if (game.isInStalemate(game.getTeamTurn())) {
                NotificationMessage inCheckmate = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        game.getTeamTurn().name() + "is in stalemate. Game over.");
                broadcastNotificationMessage(command.getGameID(), inCheckmate, session);
                sendNotificationMessage(inCheckmate,session);
            }
        } catch (Exception ex) {
            onError(ex, session);
        }
    }

    public void leave(UserGameCommand command, Session session) {
    }

    public void resign(UserGameCommand command, Session session) {
    }

    public void sendErrorMessage(ErrorMessage message, Session session) throws IOException {
        String mes = new Gson().toJson(message);
        session.getRemote().sendString(mes);
    }

    public void sendLoadGameMessage(LoadGameMessage message, Session session) throws IOException {
        String gameJson = new Gson().toJson(message);
        session.getRemote().sendString(gameJson);
    }

    public void sendNotificationMessage(NotificationMessage message, Session session) throws IOException {
        String gameJson = new Gson().toJson(message);
        session.getRemote().sendString(gameJson);
    }

    public void broadcastLoadGameMessage(Integer gameID, LoadGameMessage message, Session notThisSession) throws IOException {
        String gameJson = new Gson().toJson(message);
        for(Session s : connections.getSessionsForGame(gameID)){
            if (s.isOpen()) {
                if (!s.equals(notThisSession)) {
                    s.getRemote().sendString(gameJson);
                }
            }
        }
    }

    public void broadcastNotificationMessage(Integer gameID, NotificationMessage message, Session notThisSession) throws IOException {
        for(Session s : connections.getSessionsForGame(gameID)){
            if (s.isOpen()) {
                if (!s.equals(notThisSession)) {
                    String mes = new Gson().toJson(message);
                    s.getRemote().sendString(mes);
                }
            }
        }
    }
}
