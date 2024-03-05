package dataAccess;

import model.AuthData;

public class DatabaseAuthDAO implements AuthDAO{
    @Override
    public AuthData createAuth(String username) {
        // INSERT INTO auth_data (authToken, username) VALUES (authToken, username);
        // SELECT authToken, username FROM auth_data;
        return null;
    }

    @Override
    public AuthData validateAuth(String auth) {
        // SELECT authToken, username FROM auth_data WHERE authToken = auth;
        return null;
    }

    @Override
    public boolean delAuth(String auth) {
        //DELETE FROM auth_data WHERE authToken = auth;
        return false;
    }

    @Override
    public void clear() {
        // TRUNCATE TABLE auth_data;
    }
}
