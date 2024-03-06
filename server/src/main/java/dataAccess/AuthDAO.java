package dataAccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username);
    AuthData validateAuth(String auth);
    boolean delAuth(String auth);
    void clear() throws DataAccessException;
}
