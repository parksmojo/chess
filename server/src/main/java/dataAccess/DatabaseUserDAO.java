package dataAccess;

import model.UserData;

public class DatabaseUserDAO implements UserDAO{
    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user_data WHERE username = ?;";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var password = rs.getString("password");
                        var email = rs.getString("email");

                        return new UserData(username, password, email);
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to find user: %s", e.getMessage()));
        }
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
