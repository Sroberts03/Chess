package ui;

import com.google.gson.Gson;
import errors.ErrorResponse;
import errors.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ServerFacade {

    private final String serverUrl;
    private final HttpClient httpClient;

    public ServerFacade(String url) {
        serverUrl = url;
        httpClient = HttpClient.newHttpClient();
    }

    public void clearApp() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData login(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public void logout(String authToken) throws Exception {
        var path = "/session";
        makeRequest("DELETE", path, null, null, authToken);
    }

    public ArrayList<GameData> listGames(String authToken) throws Exception {
        var path = "/game";
        record ListGamesResponse(ArrayList<GameData> games) {}
        return makeRequest("GET", path, null, ListGamesResponse.class, authToken).games();
    }

    public Integer createGame(String authToken, GameName gameName) throws ResponseException {
        var path = "/game";
        record GameId(Integer gameID) {}
        return makeRequest("POST", path, gameName, GameId.class, authToken).gameID();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }

            writeRequest(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeRequest(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
