package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class DatabaseGameDAO implements GameDAO{
    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE TABLE game_data;";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to clear table: %s", e.getMessage()));
        }
    }

    @Override
    public int createGame(String gameName) {
        // INSERT INTO game_data (gameID, gameName, game) VALUES (gameID, gameName, game);
        return 0;
    }

    @Override
    public ArrayList<GameData> getGames() {
        // SELECT * FROM game_data;
        return null;
    }

    @Override
    public GameData insertUser(int gameID, ChessGame.TeamColor clientColor, String username) {
        // INSERT INTO game_data (white_username) VALUES (username) WHERE gameID = gameID;
        // OR
        // INSERT INTO game_data (black_username) VALUES (username) WHERE gameID = gameID;
        return null;
    }
}
