package dataAccess;

import model.UserData;

public class DatabaseUserDAO implements UserDAO{
    @Override
    public UserData getUser(String username) {
        // SELECT username, password FROM user_data WHERE username = username;
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {
        // INSERT INTO user_data (username, password, email) VALUES (username, password, email);
    }

    @Override
    public void clear() {
        // TRUNCATE TABLE user_data;
    }
}
