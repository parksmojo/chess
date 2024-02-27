package service;

import dataAccess.*;

public class GameService {
    private final GameDAO gameDAO = new MemoryGameDAO();

    public void clear(){
        gameDAO.clear();
    }
}
