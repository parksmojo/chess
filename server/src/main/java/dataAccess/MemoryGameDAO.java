package dataAccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    private static ArrayList<GameData> games = new ArrayList<>();

    @Override
    public void clear(){
        games.clear();
    }

    @Override
    public int createGame(String gameName) {
        for(GameData game : games){
            if(Objects.equals(game.gameName(), gameName)){
                return -2;
            }
        }

        int gameID = games.size() + 1;
        String whiteUsername = "";
        String blackUsername = "";
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(gameID,whiteUsername,blackUsername,gameName,game);

        games.add(newGame);
        return gameID;
    }

    //temporary function for testing
    public void setGame(GameData game){
        games.add(game);
    }

    @Override
    public GameData getGame(int gameID) {
        for (GameData game : games) {
            if (Objects.equals(game.gameID(), gameID)) {
                return game;
            }
        }
        return null;
    }

    @Override
    public ArrayList<GameData> getGames(){
        return games;
    }

    @Override
    public boolean insertUser(int gameID, ChessGame.TeamColor clientColor, String username) {
        GameData gameSpecified = null;
        int gameIndex = -1;
        for(int i = 0; i < games.size(); i++){
            if(Objects.equals(games.get(i).gameID(), gameID)){
                gameSpecified = games.get(i);
                gameIndex = i;
                break;
            }
        }
        if(gameSpecified == null){
            return false;
        }

        String whiteUsername = gameSpecified.whiteUsername();
        String blackUsername = gameSpecified.blackUsername();
        String gameName = gameSpecified.gameName();
        ChessGame game = gameSpecified.game();

        if(clientColor == ChessGame.TeamColor.WHITE){
            whiteUsername = username;
        } else if (clientColor == ChessGame.TeamColor.BLACK) {
            blackUsername = username;
        }

        games.set(gameIndex,new GameData(gameID,whiteUsername,blackUsername,gameName,game));
        return true;
    }
}
