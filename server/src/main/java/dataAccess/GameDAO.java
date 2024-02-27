package dataAccess;

import model.GameData;
import java.util.ArrayList;

public interface GameDAO {
    void clear();
    int createGame(String gameName);
    ArrayList<GameData> getGames();
}
