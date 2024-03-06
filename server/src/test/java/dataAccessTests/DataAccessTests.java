package dataAccessTests;

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
    UserData newUser = new UserData("jon3", "12345", "jon@gmail.com");

    @BeforeEach
    public void setup() throws TestException {
        String username = registeredUser.username();
        String password = registeredUser.password();
        String email = registeredUser.email();

        // Start with empty tables
        try {
            userDAO.clear();
            authDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        // One user already logged in
        registeredAuth = userService.register(username,password,email);

        // Already existing games
        gameDAO.createGame("First Game");
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


// TODO: new auth negative test


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
        boolean result;

        try {
            result = authDAO.delAuth(registeredAuth.authToken());
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertTrue(result, "Auth token not deleted");
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
    @Order(5)
    @DisplayName("Normal clear games")
    public void clearGameSuccess() throws TestException {
        ArrayList<GameData> result;

        try {
            gameDAO.clear();
            result = gameDAO.getGames();
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNull(result, String.format("Games not cleared: %s", result));
    }
}