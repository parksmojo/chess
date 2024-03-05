package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class DatabaseGameDAO implements GameDAO{
    @Override
    public void clear() {
        // TRUNCATE TABLE data_data;
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
