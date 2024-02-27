package passoffTests.serverTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;
import model.*;
import service.UserService;

public class ServiceUnitTests {
    UserService userService = new UserService();
    UserData registeredUser = new UserData("ryguy", "pass", "ry@gmail.com");
    UserData otherUser = new UserData("jon3", "12345", "jon@gmail.com");

    @Test
    @Order(1)
    @DisplayName("Normal Register")
    public void registerSuccess() throws TestException {
        String username = registeredUser.username();
        String password = registeredUser.password();
        String email = registeredUser.email();

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
        String otherPass = otherUser.password();

        AuthData loginResult = userService.login(username,otherPass);

        Assertions.assertNull(loginResult, "Login authorized with incorrect password");
    }

    @Test
    @Order(5)
    @DisplayName("Normal Logout")
    public void logoutSuccess() throws TestException {
        String username = registeredUser.username();
        String password = registeredUser.password();

        AuthData auth = userService.login(username,password);
        boolean success = userService.logout(auth.authToken());

        Assertions.assertTrue(success, "Logout failed");
    }

    @Test
    @Order(6)
    @DisplayName("Double logout")
    public void logoutFail() throws TestException {
        String username = registeredUser.username();
        String password = registeredUser.password();

        AuthData auth = userService.login(username,password);
        userService.logout(auth.authToken());
        boolean success = userService.logout(auth.authToken());

        Assertions.assertFalse(success, "Repeated successful logouts");
    }
}
