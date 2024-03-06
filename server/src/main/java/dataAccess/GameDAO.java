package dataAccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    ArrayList<GameData> getGames();
    GameData insertUser(int gameID, ChessGame.TeamColor clientColor, String username);
    GameData findGame(String gameName) throws DataAccessException;
}
