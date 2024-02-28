package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public class GameService {
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();

    public void clear(){
        gameDAO.clear();
    }

    public int makeGame(String authToken, String gameName){
        if(authDAO.validateAuth(authToken) == null){
            return -1;
        }

        return gameDAO.createGame(gameName);
    }

    public ArrayList<GameData> getGames(String authToken){
        if(authDAO.validateAuth(authToken) == null){
            return null;
        }

        return gameDAO.getGames();
    }

    public GameData joinGame(String auth, ChessGame.TeamColor clientColor, int gameID){
        AuthData authToken = authDAO.validateAuth(auth);
        if(authToken == null){
            return null;
        }

        return gameDAO.insertUser(gameID,clientColor,authToken.username());
    }
}
