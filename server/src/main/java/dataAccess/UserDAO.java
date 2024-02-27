package dataAccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    UserData getUser(String username);
    void createUser(String username, String password, String email);
    AuthData createAuth(String username);
    boolean delAuth(String auth);
}
