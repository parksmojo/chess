package dataAccess;

import java.util.Vector;

public class MemoryGameDAO implements GameDAO {
    private final Vector games = new Vector<>();

    public void clear(){
        games.clear();
    }
}
