package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    private final GameDAO gameDAO = new DatabaseGameDAO();
    private final AuthDAO authDAO = new DatabaseAuthDAO();

    public void clear(){
        try {
            gameDAO.clear();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public int makeGame(String authToken, String gameName){
        try {
            if (authDAO.validateAuth(authToken) == null) {
                return -1;
            }
            if (gameDAO.findGame(gameName) != null){
                return -2;
            }

            return gameDAO.createGame(gameName);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public void setGame(GameData game){
        try {
            gameDAO.setGame(game);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public GameData findGame(int gameID){
        try{
            return gameDAO.findGame(gameID);

        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ArrayList<GameData> getGames(String authToken){
        try {
            if (authDAO.validateAuth(authToken) == null) {
                return null;
            }

            return gameDAO.getGames();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public GameData joinGame(String auth, ChessGame.TeamColor clientColor, int gameID){
        try {
            AuthData authToken = authDAO.validateAuth(auth);
            if (authToken == null) {
                return null;
            }

            GameData gameSpecified = gameDAO.findGame(gameID);
            boolean canInsertWhite = false;
            boolean canInsertBlack = false;
            if(clientColor == ChessGame.TeamColor.WHITE){
                if(Objects.equals(gameSpecified.whiteUsername(), null)
                        || Objects.equals(gameSpecified.whiteUsername(), authToken.username())){
                    canInsertWhite = true;
                }
            } else if (clientColor == ChessGame.TeamColor.BLACK) {
                if(Objects.equals(gameSpecified.blackUsername(), null)
                        || Objects.equals(gameSpecified.blackUsername(), authToken.username())){
                    canInsertBlack = true;
                }
            }
            if(canInsertWhite || canInsertBlack){
                return gameDAO.insertUser(gameID, clientColor, authToken.username());
            } else if(clientColor == null){
                return gameSpecified;
            } else {
                return null;
            }

        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
