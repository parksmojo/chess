package service;

import dataAccess.*;

public class GameService {
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();

    // TODO: Needs actual implementation
    public void clear(){
        gameDAO.clear();
    }

    public int makeGame(String authToken, String gameName){
        if(!authDAO.validateAuth(authToken)){
            return -1;
        }

        return gameDAO.createGame(gameName);
    }
}
