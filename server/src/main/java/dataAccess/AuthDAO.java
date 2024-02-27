package dataAccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username);
    boolean validateAuth(String auth);
    boolean delAuth(String auth);
}
