package dataAccess;

public interface GameDAO {
    void clear();
    int createGame(String gameName);
}
