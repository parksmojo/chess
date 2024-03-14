package clientTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseAuthDAO;
import dataAccess.DatabaseGameDAO;
import dataAccess.DatabaseUserDAO;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;
import model.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clearDB(){
        try {
            new DatabaseGameDAO().clear();
            new DatabaseAuthDAO().clear();
            new DatabaseUserDAO().clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerSuccess() throws Exception {
        AuthData result = facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(result.authToken().length() > 10);
    }
    @Test
    public void registerFail() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        int errorCode = 0;
        try {
            facade.register("player1", "password6", "p1F@email.com");
        } catch (ResponseException e){
            errorCode = e.StatusCode();
        }
        Assertions.assertEquals(403, errorCode);
    }

    @Test
    public void loginSuccess() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        AuthData result = facade.login("player1", "password");
        Assertions.assertTrue(result.authToken().length() > 10);
    }
    @Test
    public void loginFail() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        int errorCode = 0;
        try {
            facade.login("player1", "wrong");
        } catch (ResponseException e){
            errorCode = e.StatusCode();
        }
        Assertions.assertEquals(401, errorCode);
    }

    @Test
    public void logoutSuccess() throws Exception {
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        facade.logout(auth.authToken());
        int errorCode = 0;
        try {
            facade.logout(auth.authToken());
        } catch (ResponseException e){
            errorCode = e.StatusCode();
        }
        Assertions.assertEquals(401, errorCode);
    }
    @Test
    public void logoutFail() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        int errorCode = 0;
        try {
            facade.logout("auth.authToken()");
        } catch (ResponseException e){
            errorCode = e.StatusCode();
        }
        Assertions.assertEquals(401, errorCode);
    }

    @Test
    public void createGameSuccess() throws Exception {
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        int result = facade.newGame(auth.authToken(), "Game1");
        Assertions.assertEquals(1,result);
    }
    @Test
    public void createGameFail() {
        int errorCode = 0;
        try {
            facade.newGame("auth.authToken()","NewGame");
        } catch (ResponseException e){
            errorCode = e.StatusCode();
        }
        Assertions.assertEquals(401, errorCode);
    }

    @Test
    public void listGameSuccess() throws Exception {
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        facade.newGame(auth.authToken(), "Game1");
        facade.newGame(auth.authToken(), "Game2");
        facade.newGame(auth.authToken(), "Game3");
        GameData[] result = facade.listGames(auth.authToken());
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Game1",result[0].gameName());
        System.out.println(result[0].gameName());
    }
    @Test
    public void listGameFail() {
        int errorCode = 0;
        try {
            facade.newGame("auth.authToken()","NewGame");
        } catch (ResponseException e){
            errorCode = e.StatusCode();
        }
        Assertions.assertEquals(401, errorCode);
    }


    @Test
    public void joinSuccess() throws Exception {
        AuthData auth = facade.register("player1", "password", "p1@email.com");
        facade.logout(auth.authToken());
        int errorCode = 0;
        try {
            facade.logout(auth.authToken());
        } catch (ResponseException e){
            errorCode = e.StatusCode();
        }
        Assertions.assertEquals(401, errorCode);
    }
    @Test
    public void joinFail() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        int errorCode = 0;
        try {
            facade.logout("auth.authToken()");
        } catch (ResponseException e){
            errorCode = e.StatusCode();
        }
        Assertions.assertEquals(401, errorCode);
    }
}
