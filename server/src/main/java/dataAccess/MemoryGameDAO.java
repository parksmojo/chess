package dataAccess;

import model.GameData;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    private HashSet<GameData> games = new HashSet<>();

    public void clear(){
        games.clear();
    }
}
