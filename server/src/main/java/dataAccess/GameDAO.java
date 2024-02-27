package dataAccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;

public interface GameDAO {
    void clear();
    int createGame(String gameName);
    ArrayList<GameData> getGames();
    GameData insertUser(int gameID, ChessGame.TeamColor clientColor, String username);
    GameData getGame(int gameID);
}
