package dataAccess;

import model.UserData;

public class DatabaseUserDAO implements UserDAO{
    @Override
    public UserData getUser(String username) {
        // SELECT username, password FROM user_data WHERE username = username;
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO user_data (username, password, email) VALUES (?, ?, ?);";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to create user: %s", e.getMessage()));
        }
    }

    @Override
    public void clear() {
        // TRUNCATE TABLE user_data;
    }
}
