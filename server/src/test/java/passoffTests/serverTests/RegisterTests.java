package passoffTests.serverTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;
import model.*;
import service.UserService;

public class RegisterTests {
    UserService userService = new UserService();
    UserData newUser = new UserData("ryguy", "pass", "ry@gmail.com");

    @Test
    @DisplayName("Register User Success")
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
}
