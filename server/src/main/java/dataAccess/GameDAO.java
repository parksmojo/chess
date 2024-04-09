package dataAccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;

public interface GameDAO {
    void clear() throws DataAccessException;

    void setGame(GameData game) throws DataAccessException;

    GameData findGame(int gameID) throws DataAccessException;
    GameData findGame(String gameName) throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;
    ArrayList<GameData> getGames() throws DataAccessException;
    GameData insertUser(int gameID, ChessGame.TeamColor clientColor, String username) throws DataAccessException;
}
