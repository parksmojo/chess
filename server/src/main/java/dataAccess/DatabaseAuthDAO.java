package dataAccess;

import model.AuthData;

public class DatabaseAuthDAO implements AuthDAO{
    @Override
    public AuthData createAuth(String username) {
        return null;
    }

    @Override
    public AuthData validateAuth(String auth) {
        return null;
    }

    @Override
    public boolean delAuth(String auth) {
        return false;
    }

    @Override
    public void clear() {

    }
}
