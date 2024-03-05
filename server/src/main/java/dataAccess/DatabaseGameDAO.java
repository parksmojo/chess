package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class DatabaseGameDAO implements GameDAO{
    @Override
    public void clear() {

    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public ArrayList<GameData> getGames() {
        return null;
    }

    @Override
    public GameData insertUser(int gameID, ChessGame.TeamColor clientColor, String username) {
        return null;
    }
}
