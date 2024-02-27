package service;

import dataAccess.*;
import model.GameData;

import java.util.ArrayList;

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

    public ArrayList<GameData> getGames(String authToken){
        if(!authDAO.validateAuth(authToken)){
            return null;
        }

        return gameDAO.getGames();
    }
}
