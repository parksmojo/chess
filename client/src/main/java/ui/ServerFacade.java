package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ServerFacade {
    private static String serverURL = "http://localhost:";
    private String currentAuthToken = null;

    public ServerFacade(int port) {
        serverURL += Integer.toString(port);
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        String path = "/user";
        UserData user = new UserData(username,password,email);
        AuthData result = this.makeRequest("POST",path,null,user,AuthData.class);
        currentAuthToken = result.authToken();
        return result;
    }

    public AuthData login(String username, String password) throws ResponseException {
        String path = "/session";
        UserData user = new UserData(username,password,null);
        AuthData result =  this.makeRequest("POST",path,null,user,AuthData.class);
        currentAuthToken = result.authToken();
        return result;
    }

    public void logout() throws ResponseException {
        String path = "/session";
        this.makeRequest("DELETE",path,currentAuthToken,null,null);
        currentAuthToken = null;
    }
    public void logout(String authToken) throws ResponseException {
        String path = "/session";
        this.makeRequest("DELETE",path,authToken,null,null);
        currentAuthToken = null;
    }

    public int newGame(String gameName) throws ResponseException {
        String path = "/game";
        Object body = Map.of("gameName",gameName);
        record GameIDResponse(int gameID) { }
        return this.makeRequest("POST",path,currentAuthToken,body,GameIDResponse.class).gameID();
    }
    public int newGame(String authToken, String gameName) throws ResponseException {
        String path = "/game";
        Object body = Map.of("gameName",gameName);
        record GameIDResponse(int gameID) { }
        return this.makeRequest("POST",path,authToken,body,GameIDResponse.class).gameID();
    }

    public GameData[] listGames() throws ResponseException {
        String path = "/game";
        record listGameData(GameData[] games){}
        var response = this.makeRequest("GET",path,currentAuthToken,null, listGameData.class);
        return response.games;
    }
    public GameData[] listGames(String authToken) throws ResponseException {
        String path = "/game";
        record listGameData(GameData[] games){}
        var response = this.makeRequest("GET",path,authToken,null, listGameData.class);
        return response.games;
    }

    public void joinGame(ChessGame.TeamColor ClientColor, int gameID) throws ResponseException {
        String path = "/game";
        Object body;
        if(ClientColor != null){
            body = Map.of("playerColor",ClientColor,"gameID",gameID);
        } else {
            body = Map.of("gameID",gameID);
        }
        this.makeRequest("PUT",path,currentAuthToken,body,null);
    }
    public void joinGame(String authToken, ChessGame.TeamColor ClientColor, int gameID) throws ResponseException {
        String path = "/game";
        Object body = Map.of("playerColor",ClientColor,"gameID",gameID);
        this.makeRequest("PUT",path,authToken,body,null);
    }


    private <T> T makeRequest(String method, String path, String authToken, Object reqBody, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeHeader(authToken,http);
            writeBody(reqBody, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex){
            throw new ResponseException(ex.StatusCode(),ex.getMessage());
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    private static void writeHeader(String authToken, HttpURLConnection http) {
        if (authToken != null) {
            http.addRequestProperty("authorization", authToken);
        }
    }
    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
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

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
