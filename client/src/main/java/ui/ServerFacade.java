package ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import errors.ErrorResponse;
import errors.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;

public class ServerFacade {

    private final String serverUrl;
    private final HttpClient httpClient;

    public ServerFacade(String url) {
        serverUrl = url;
        httpClient = HttpClient.newHttpClient();
    }

    public void clearApp() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public AuthData login(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public void logout(String authToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/session"))
                .DELETE()
                .header("Authorization", authToken)
                .build();
        sendRequest(request);
    }

    public ArrayList<GameData> listGames(String authToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .GET()
                .header("Authorization", authToken)
                .build();
        record listGamesResponse(ArrayList<GameData> games) {
        }
        HttpResponse<String> response = sendRequest(request);
        Gson gson = new Gson();
        return gson.fromJson(response.body(), listGamesResponse.class).games();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception{
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (!isSuccessful(response.statusCode())) {
            Gson gson = new Gson();
            ErrorResponse err = gson.fromJson(response.body(), ErrorResponse.class);
            throw new ResponseException(response.statusCode(), err.message());
        }
        return response;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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
