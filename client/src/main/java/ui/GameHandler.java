package ui;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public interface GameHandler {
    public void onLoadGame(LoadGameMessage message);
    public void onError(ErrorMessage message);
    public void onNotifiy(NotificationMessage message);
}
