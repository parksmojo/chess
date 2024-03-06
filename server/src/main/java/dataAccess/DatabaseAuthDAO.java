package dataAccess;

import model.AuthData;

import java.util.UUID;

public class DatabaseAuthDAO implements AuthDAO{
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO auth_data (authToken, username) VALUES (?, ?);";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                String token = UUID.randomUUID().toString();
                preparedStatement.setString(1, token);
                preparedStatement.setString(2, username);

                preparedStatement.executeUpdate();

                return new AuthData(token, username);
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to create auth token: %s", e.getMessage()));
        }
    }

    @Override
    public AuthData validateAuth(String auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth_data WHERE authToken = ?;";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, auth);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var authToken = rs.getString("authToken");
                        var username = rs.getString("username");

                        return new AuthData(authToken, username);
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
    public boolean delAuth(String auth) {
        //DELETE FROM auth_data WHERE authToken = auth;
        return false;
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE TABLE auth_data;";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to clear table: %s", e.getMessage()));
        }
    }
}
