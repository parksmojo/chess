package dataAccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String username) throws DataAccessException;
    AuthData validateAuth(String auth) throws DataAccessException;
    boolean delAuth(String auth);
    void clear() throws DataAccessException;
}
