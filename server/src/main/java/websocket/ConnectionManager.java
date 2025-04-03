package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session> > connections = new ConcurrentHashMap<>();

    public void addSessionToGame(Integer gameID, Session session) {
        if (connections.get(gameID) == null) {
            Set<Session> sessions = new HashSet<>();
            sessions.add(session);
            connections.put(gameID, sessions);
        } else {
            Set<Session> sessions = connections.get(gameID);
            sessions.add(session);
        }
    }

    public void removeSessionFromGame(Integer gameID, Session session) {
        Set<Session> sessions = connections.get(gameID);
        sessions.remove(session);
    }

    public Set<Session> getSessionsForGame(Integer gameID) {
        return connections.get(gameID);
    }
}
