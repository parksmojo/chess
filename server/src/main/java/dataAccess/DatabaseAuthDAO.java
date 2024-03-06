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
