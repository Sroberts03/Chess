package websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public interface NotificationHandler {
    void notifyError(ErrorMessage notification);
    void notifyLoadGame(LoadGameMessage notification);
    void notifyNotification(NotificationMessage notification);
}
