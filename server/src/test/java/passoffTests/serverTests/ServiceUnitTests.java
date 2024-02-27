package passoffTests.serverTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;
import model.*;
import service.UserService;

public class ServiceUnitTests {
    UserService userService = new UserService();
    UserData newUser = new UserData("ryguy", "pass", "ry@gmail.com");
    UserData otherUser = new UserData("jon3", "12345", "jon@gmail.com");

    @Test
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
    @DisplayName("Taken Username")
    public void registerFail() throws TestException {
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();

        userService.register(username,password,email);
        AuthData secondRegister = userService.register(username,password,email);

        Assertions.assertNull(secondRegister, "Duplicate register returned authToken");
    }

    @Test
    @DisplayName("Normal Login")
    public void loginSuccess() throws TestException {
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();

        AuthData registerResult = userService.register(username,password,email);
        AuthData loginResult = userService.login(username,password);

        Assertions.assertNotNull(loginResult, "Login not authorized");
        Assertions.assertEquals(username, loginResult.username(), "Username changed");
        Assertions.assertNotEquals(registerResult,loginResult,"Same authToken returned");
    }

    @Test
    @DisplayName("Incorrect Password")
    public void loginFail() throws TestException {
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();
        String otherPass = otherUser.password();

        userService.register(username,password,email);
        AuthData loginResult = userService.login(username,otherPass);

        Assertions.assertNull(loginResult, "Login authorized with incorrect password");
    }

    @Test
    @DisplayName("Normal Logout")
    public void logoutSuccess() throws TestException {
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();

        AuthData auth = userService.register(username,password,email);
        boolean success = userService.logout(auth.authToken());

        Assertions.assertTrue(success, "Logout failed");
    }

    @Test
    @DisplayName("Double logout")
    public void logoutFail() throws TestException {
        String username = newUser.username();
        String password = newUser.password();
        String email = newUser.email();

        AuthData auth = userService.register(username,password,email);
        userService.logout(auth.authToken());
        boolean success = userService.logout(auth.authToken());

        Assertions.assertFalse(success, "Repeated successful logouts");
    }
}
