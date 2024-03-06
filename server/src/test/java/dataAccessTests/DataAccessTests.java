package dataAccessTests;

import dataAccess.*;
import model.*;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;
import service.UserService;

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
    @DisplayName("IDK HOW TO MAKE THIS FAIL")
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

        Assertions.assertNotNull(result, "User not deleted");
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
}
