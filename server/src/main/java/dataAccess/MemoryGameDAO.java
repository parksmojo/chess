package dataAccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    private static final ArrayList<GameData> games = new ArrayList<>();

    @Override
    public void clear(){
        games.clear();
    }

    @Override
    public int createGame(String gameName) {
        int gameID = games.size() + 1;
        String whiteUsername = null;
        String blackUsername = null;
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(gameID,whiteUsername,blackUsername,gameName,game);

        games.add(newGame);
        return gameID;
    }

    @Override
    public GameData findGame(String gameName) {
        for(GameData game : games){
            if(Objects.equals(game.gameName(), gameName)){
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
    public GameData insertUser(int gameID, ChessGame.TeamColor clientColor, String username) {
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
            return null;
        }

        String whiteUsername = gameSpecified.whiteUsername();
        String blackUsername = gameSpecified.blackUsername();
        String gameName = gameSpecified.gameName();
        ChessGame game = gameSpecified.game();

        if(clientColor == ChessGame.TeamColor.WHITE && Objects.equals(whiteUsername, null)){
            whiteUsername = username;
        } else if (clientColor == ChessGame.TeamColor.BLACK && Objects.equals(blackUsername, null)) {
            blackUsername = username;
        } else if(clientColor == null){
            return gameSpecified;
        } else {
            return null;
        }

        GameData newGame = new GameData(gameID,whiteUsername,blackUsername,gameName,game);
        games.set(gameIndex, newGame);
        return newGame;
    }
}
