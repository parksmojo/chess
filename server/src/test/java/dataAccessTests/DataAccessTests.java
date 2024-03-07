package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.*;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;
import service.UserService;

import java.util.ArrayList;

public class DataAccessTests {
    private final GameDAO gameDAO = new DatabaseGameDAO();
    private final UserDAO userDAO = new DatabaseUserDAO();
    private final AuthDAO authDAO = new DatabaseAuthDAO();
    UserService userService = new UserService();

    UserData registeredUser = new UserData("ryguy", "pass", "ry@gmail.com");
    AuthData registeredAuth;
    String loadedGameName = "First Game";
    UserData newUser = new UserData("jon3", "12345", "jon@gmail.com");
    String newGameName = "Second Game";
    String[] newGames = {"Third Game","This Game","That Game","GamerCentral"};

    @BeforeEach
    public void setup() throws TestException {
        String username = registeredUser.username();
        String password = registeredUser.password();
        String email = registeredUser.email();

        try {
            // Start with empty tables
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();

            // One user already logged in
            registeredAuth = userService.register(username,password,email);

            // Already existing games
            gameDAO.createGame(loadedGameName);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Normal New User")
    public void newUserSuccess() throws TestException {
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();
        UserData result;

        try {
            userDAO.createUser(username, password, email);
            result = userDAO.getUser(username);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertEquals(password, result.password(), "Password did not match expected");
        Assertions.assertEquals(email, result.email(), "Email did not match expected");
    }

    @Test
    @Order(2)
    @DisplayName("Duplicate User")
    public void newUserFail() throws TestException {
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();
        UserData result;

        try {
            userDAO.createUser(username, password, email);
            result = userDAO.getUser(username);
            if(result != null) {
                userDAO.createUser(username, "password", "email");
            }
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        assert result != null;
        Assertions.assertNotEquals(result.password(), "password", "Password changed when inserting duplicate username");
    }

    @Test
    @Order(3)
    @DisplayName("Normal Get User")
    public void getUserSuccess() throws TestException {
        String username = registeredUser.username();
        String password = registeredUser.password();
        String email = registeredUser.email();
        UserData result;

        try {
            result = userDAO.getUser(username);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertEquals(password, result.password(), "Password did not match expected");
        Assertions.assertEquals(email, result.email(), "Email did not match expected");
    }

    @Test
    @Order(4)
    @DisplayName("User doesn't exist")
    public void getUserFail() throws TestException {
        UserData result;
        try {
            result = userDAO.getUser(newUser.username());
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNull(result, String.format("Expected null, received: %s", result));
    }

    @Test
    @Order(5)
    @DisplayName("Normal clear users")
    public void clearUsersSuccess() throws TestException {
        String username = registeredUser.username();
        String password = registeredUser.password();
        String email = registeredUser.email();
        UserData result;

        try {
            userDAO.createUser(username, password, email);
            userDAO.clear();
            result = userDAO.getUser(username);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNull(result, "User not deleted");
    }

    @Test
    @Order(6)
    @DisplayName("Normal clear auth tokens")
    public void clearAuthsSuccess() throws TestException {
        try {
            authDAO.clear();
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }
    }

    @Test
    @Order(7)
    @DisplayName("Normal new auth token")
    public void newAuthSuccess() throws TestException {
        String username = newUser.username();
        AuthData result;

        try {
            result = authDAO.createAuth(username);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNotNull(result.authToken(), "Returned null auth token");
        Assertions.assertEquals(username, result.username(), "Returned different username");
    }


    @Test
    @Order(7)
    @DisplayName("Normal new auth token")
    public void newAuthFail() throws TestException {
        String username = "";
        AuthData result;

        try {
            result = authDAO.createAuth(username);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNull(result, "Registered blank user");
    }


    @Test
    @Order(9)
    @DisplayName("Valid auth token")
    public void checkAuthSuccess() throws TestException {
        String username = newUser.username();
        AuthData result1;
        AuthData result2;

        try {
            result1 = authDAO.createAuth(username);
            result2 = authDAO.validateAuth(result1.authToken());
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNotNull(result2.authToken(), "Returned null auth token");
        Assertions.assertEquals(username, result2.username(), "Returned different username");
        Assertions.assertEquals(result1, result2, "Auth tokens not equal");
    }

    @Test
    @Order(10)
    @DisplayName("Invalid auth token")
    public void checkAuthFail() throws TestException {
        AuthData result;

        try {
            result = authDAO.validateAuth("bkh3w7whh5sef5rr8");
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNull(result, "Returned null auth token");
    }

    @Test
    @Order(11)
    @DisplayName("Delete auth")
    public void deleteAuthSuccess() throws TestException {
        boolean success;
        AuthData result;

        try {
            success = authDAO.delAuth(registeredAuth.authToken());
            result = authDAO.validateAuth(registeredAuth.authToken());
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertTrue(success, "Auth token not deleted");
        Assertions.assertNull(result, "Auth not deleted");
    }

    @Test
    @Order(12)
    @DisplayName("Non-existent auth")
    public void deleteAuthFail() throws TestException {
        boolean result;

        try {
            result = authDAO.delAuth("h384hsd78w3lk");
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertFalse(result, "Deleted non-existent auth token");
    }

    @Test
    @Order(13)
    @DisplayName("Normal clear games")
    public void clearGameSuccess() throws TestException {
        ArrayList<GameData> result;

        try {
            gameDAO.clear();
            result = gameDAO.getGames();
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertTrue(result.isEmpty(), String.format("Games not cleared: %s", result));
    }

    @Test
    @Order(13)
    @DisplayName("Find a game")
    public void findGameSuccess() throws TestException {
        GameData result;

        try {
            result = gameDAO.findGame(loadedGameName);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNotNull(result, "Returned null");
        Assertions.assertEquals(loadedGameName, result.gameName(), "Game name does not match expected");
    }

    @Test
    @Order(14)
    @DisplayName("Game doesn't exist")
    public void findGameFail() throws TestException {
        GameData result;

        try {
            result = gameDAO.findGame(newGameName);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNull(result, "Returned non-existent game");
    }

    @Test
    @Order(15)
    @DisplayName("Make a new game")
    public void createGameSuccess() throws TestException {
        int result;
        GameData insertedGame;

        try {
            result = gameDAO.createGame(newGameName);
            insertedGame = gameDAO.findGame(newGameName);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertTrue(result > 0, String.format("Returned error code: %d", result));
        Assertions.assertEquals(newGameName, insertedGame.gameName(), "Game name does not match expected");
    }

    @Test
    @Order(16)
    @DisplayName("Game already exists")
    public void createGameFail() throws TestException {
        int result;
        GameData insertedGame;

        try {
            if(gameDAO.findGame(loadedGameName) == null) {
                result = gameDAO.createGame(loadedGameName);
            } else {
                result = -2;
            }
            insertedGame = gameDAO.findGame(loadedGameName);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertEquals(-2, result, String.format("Should have returned error code -2. Given: %d", result));
        Assertions.assertEquals(loadedGameName, insertedGame.gameName(), "Game name does not match expected");
    }

    @Test
    @Order(17)
    @DisplayName("Return all games")
    public void listGamesSuccess() throws TestException {
        ArrayList<GameData> result;
        GameData foundGame1;
        GameData foundGame2;

        try {
            gameDAO.createGame(newGameName);
            for(String gameName : newGames){
                gameDAO.createGame(gameName);
            }
            result = gameDAO.getGames();
            foundGame1 = gameDAO.findGame(newGames[0]);
            foundGame2 = gameDAO.findGame(newGames[3]);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertFalse(result.isEmpty(), "Returned empty list");
        Assertions.assertEquals(newGames[0],foundGame1.gameName(), String.format("Inserted game \"%s\" not found",newGames[0]));
        Assertions.assertEquals(newGames[3],foundGame2.gameName(), String.format("Inserted game \"%s\" not found",newGames[3]));
    }

    @Test
    @Order(18)
    @DisplayName("No games")
    public void listGamesFail() throws TestException {
        ArrayList<GameData> result;

        try {
            gameDAO.clear();
            result = gameDAO.getGames();
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertTrue(result.isEmpty(), String.format("Expected null list, received: %s", result));
    }

    @Test
    @Order(19)
    @DisplayName("Inserting a User")
    public void insertUserSuccess() throws TestException {
        GameData result;
        int resultID;

        try {
            resultID = gameDAO.createGame(newGameName);
            result = gameDAO.insertUser(resultID, ChessGame.TeamColor.WHITE,registeredUser.username());
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNotNull(result, "Returned null game");
        Assertions.assertEquals(newGameName, result.gameName(), "Returned different game name");
        Assertions.assertEquals(resultID, result.gameID(), "Returned different ID");
        Assertions.assertEquals(registeredUser.username(), result.whiteUsername(), "Inserted username does not match");
    }

    @Test
    @Order(20)
    @DisplayName("White spot taken")
    public void insertUserFail() throws TestException {
        GameData result;

        try {
            int gameID = gameDAO.createGame(newGameName);
            GameData game = gameDAO.insertUser(gameID, ChessGame.TeamColor.WHITE,registeredUser.username());
            if(game.whiteUsername() == null){
                result = gameDAO.insertUser(gameID, ChessGame.TeamColor.WHITE,newUser.username());
            } else {
                result = null;
            }
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNull(result, "Overwrote a player");
    }
}