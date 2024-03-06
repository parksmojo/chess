package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Statement;
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
    public GameData findGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game_data WHERE game_name = ?;";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, gameName);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var gameID = rs.getInt("game_ID");
                        var white_username = rs.getString("white_username");
                        var black_username = rs.getString("black_username");
                        var gameJSON = rs.getString("game");
                        var game = new Gson().fromJson(gameJSON, ChessGame.class);

                        return new GameData(gameID,white_username,black_username,gameName,game);
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to find game: %s", e.getMessage()));
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO game_data (game_name, game) VALUES (?, ?);";
            try (var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ChessGame blankGame = new ChessGame();
                String gameString = new Gson().toJson(blankGame);

                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, gameString);

                preparedStatement.executeUpdate();

                var resultSet = preparedStatement.getGeneratedKeys();
                var gameID = 0;
                if (resultSet.next()) {
                    gameID = resultSet.getInt(1);
                }

                return gameID;
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to create game: %s", e.getMessage()));
        }
    }

    @Override
    public ArrayList<GameData> getGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game_data;";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var gameID = rs.getInt("game_ID");
                        var white_username = rs.getString("white_username");
                        var black_username = rs.getString("black_username");
                        var gameName = rs.getString("game_name");
                        var gameJSON = rs.getString("game");
                        var game = new Gson().fromJson(gameJSON, ChessGame.class);

                        games.add(new GameData(gameID,white_username,black_username,gameName,game));
                    }
                    return games;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to find games: %s", e.getMessage()));
        }
    }

    @Override
    public GameData insertUser(int gameID, ChessGame.TeamColor clientColor, String username) {
        // INSERT INTO game_data (white_username) VALUES (username) WHERE gameID = gameID;
        // OR
        // INSERT INTO game_data (black_username) VALUES (username) WHERE gameID = gameID;
        return null;
    }
}
