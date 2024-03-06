package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.*;
import org.junit.jupiter.api.*;
import passoffTests.testClasses.TestException;

public class DataAccessTests {
    private final GameDAO gameDAO = new DatabaseGameDAO();
    private final UserDAO userDAO = new DatabaseUserDAO();
    private final AuthDAO authDAO = new DatabaseAuthDAO();

    UserData registeredUser = new UserData("ryguy", "pass", "ry@gmail.com");
    AuthData registeredAuth;
    UserData newUser = new UserData("jon3", "12345", "jon@gmail.com");
    GameData premadeGame = new GameData(1234,"ryguy","jon3","First Game", new ChessGame());

//    @BeforeEach
//    public void setup() throws TestException {
//
//        String username = registeredUser.username();
//        String password = registeredUser.password();
//        String email = registeredUser.email();
//
//        //one user already logged in
//        registeredAuth = userService.register(username,password,email);
//
//        //already existing games
//        gameDAO.createGame("First Game");
//    }

    @Test
    @Order(1)
    @DisplayName("Normal New User")
    public void newUserSuccess() throws TestException {
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();

        try {
            userDAO.createUser(username, password, email);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("IDK HOW TO MAKE THIS FAIL")
    public void newUserFail() throws TestException {
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();

        try {
            userDAO.createUser(username, password, email);
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }
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
//            result = userDAO.getUser(newUser.username());
            result = userDAO.getUser("gamer");
        } catch (Exception e) {
            throw new TestException(e.getMessage());
        }

        Assertions.assertNull(result, String.format("Expected null, received: %s", result));
    }
}
