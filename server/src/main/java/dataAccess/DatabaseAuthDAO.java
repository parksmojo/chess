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
