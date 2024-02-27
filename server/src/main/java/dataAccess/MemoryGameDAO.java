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

    public void setGame(GameData game){
        games.add(game);
    }

    @Override
    public ArrayList<GameData> getGames(){
        return games;
    }
}
