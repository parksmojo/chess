package serviceTests;

import chess.ChessGame;
import dataAccess.MemoryGameDAO;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;
import model.*;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

public class ServiceUnitTests {
    UserService userService = new UserService();
    GameService gameService = new GameService();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    UserData registeredUser = new UserData("ryguy", "pass", "ry@gmail.com");
    AuthData registeredAuth;
    UserData newUser = new UserData("jon3", "12345", "jon@gmail.com");
    GameData premadeGame = new GameData(1234,"ryguy","jon3","First Game", new ChessGame());
    String newGame1 = "Second Game";
    String newGame2 = "Third Game";

    @BeforeEach
    public void setup() throws TestException {
        userService.clear();
        gameService.clear();

        String username = registeredUser.username();
        String password = registeredUser.password();
        String email = registeredUser.email();

        //one user already logged in
        registeredAuth = userService.register(username,password,email);

        //already existing games
        gameDAO.createGame("First Game");
    }

    @Test
    @Order(1)
    @DisplayName("Normal Register")
    public void registerSuccess() throws TestException {
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();

        AuthData authResult = userService.register(username,password,email);

        Assertions.assertEquals(username, authResult.username(), "Username changed");
        Assertions.assertNotNull(authResult.authToken(), "No authentication string");
    }

    @Test
    @Order(2)
    @DisplayName("Taken Username")
    public void registerFail() throws TestException {
        String username = registeredUser.username();
        String password = registeredUser.password();
        String email = registeredUser.email();

        AuthData secondRegister = userService.register(username,password,email);

        Assertions.assertNull(secondRegister, "Duplicate register returned authToken");
    }

    @Test
    @Order(3)
    @DisplayName("Normal Login")
    public void loginSuccess() throws TestException {
        String username = registeredUser.username();
        String password = registeredUser.password();

        AuthData loginResult = userService.login(username,password);

        Assertions.assertNotNull(loginResult, "Login not authorized");
        Assertions.assertEquals(username, loginResult.username(), "Username changed");
    }

    @Test
    @Order(4)
    @DisplayName("Incorrect Password")
    public void loginFail() throws TestException {
        String username = registeredUser.username();
        String otherPass = newUser.password();

        AuthData loginResult = userService.login(username,otherPass);

        Assertions.assertNull(loginResult, "Login authorized with incorrect password");
    }

    @Test
    @Order(5)
    @DisplayName("Normal Logout")
    public void logoutSuccess() throws TestException {
        boolean success = userService.logout(registeredAuth.authToken());

        Assertions.assertTrue(success, "Logout failed");
    }

    @Test
    @Order(6)
    @DisplayName("Double logout")
    public void logoutFail() throws TestException {
        userService.logout(registeredAuth.authToken());
        boolean success = userService.logout(registeredAuth.authToken());

        Assertions.assertFalse(success, "Logged out twice");
    }

    @Test
    @Order(7)
    @DisplayName("Create Game")
    public void createGameSuccess() throws TestException {
        int gameID = gameService.makeGame(registeredAuth.authToken(), newGame1);

        Assertions.assertTrue(gameID >= 0, String.format("Invalid gameID given: %d",gameID));
    }

    @Test
    @Order(8)
    @DisplayName("Create more games")
    public void createMoreSuccess() throws TestException {
        int gameID1 = gameService.makeGame(registeredAuth.authToken(), newGame1);
        int gameID2 = gameService.makeGame(registeredAuth.authToken(), newGame2);

        Assertions.assertTrue((gameID1 > 0) && (gameID2 > 0), "Invalid gameID given");
    }

    @Test
    @Order(9)
    @DisplayName("Duplicate Game")
    public void createGameFail() throws TestException {
        int gameID1 = gameService.makeGame(registeredAuth.authToken(), newGame1);
        int gameID2 = gameService.makeGame(registeredAuth.authToken(), newGame1);

        Assertions.assertTrue(gameID1 >= 0, "Invalid gameID given");
        Assertions.assertTrue(gameID2 < 0, "Duplicate game allowed");
    }

    @Test
    @Order(10)
    @DisplayName("List Games")
    public void listGamesSuccess() throws TestException {
        gameService.makeGame(registeredAuth.authToken(), newGame1);
        gameService.makeGame(registeredAuth.authToken(), newGame2);

        ArrayList<GameData> games = gameService.getGames(registeredAuth.authToken());

        Assertions.assertNotNull(games, "Null collection");
    }

    @Test
    @Order(11)
    @DisplayName("List empty Games")
    public void listGamesEmpty() throws TestException {
        gameService.clear();

        ArrayList<GameData> games = gameService.getGames(registeredAuth.authToken());

        Assertions.assertTrue(games.isEmpty(), "Games survived clear");
    }

    @Test
    @Order(12)
    @DisplayName("Join game")
    public void joinGameSuccess() throws TestException {
        int gameID = gameService.makeGame(registeredAuth.authToken(), newGame1);
        GameData newGame = gameService.joinGame(registeredAuth.authToken(), ChessGame.TeamColor.BLACK, gameID);
        Assertions.assertNotNull(newGame, "Join failed");
    }

    @Test
    @Order(13)
    @DisplayName("Spot already taken")
    public void joinGameFailure() throws TestException {
        GameData newGame = gameService.joinGame(registeredAuth.authToken(), ChessGame.TeamColor.BLACK, premadeGame.gameID());
        Assertions.assertNull(newGame, "Joined taken spot");
    }
}
