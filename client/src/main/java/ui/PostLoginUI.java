package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class PostLoginUI extends UIHelper{
    private static ServerFacade facade;
    private static String currUser;
    private static boolean running = true;

    public static void start(ServerFacade serverFacade, String user){
        facade = serverFacade;
        currUser = user;
        run();
    }

    private static void run(){
        Scanner scanner = new Scanner(System.in);
        running = true;
        while(running){
            System.out.print("[" + currUser + "] >>> ");
            String[] input = scanner.nextLine().split(" ");
            ArrayList<String> args = new ArrayList<>(Arrays.asList(input));
            if(args.isEmpty() && args.getFirst() != null){
                continue;
            }
            String command = args.getFirst();

            switch (command) {
                case "help":
                    System.out.print("""
                                create <name> - to make a new game
                                list - to see games
                                join <id> [WHITE|BLACK] - to play a game
                                observe <id> - to watch a game
                                logout - to logout
                                help - to see possible commands
                                
                            """);
                    break;
                case "create":
                    create(args);
                    break;
                case "list":
                    list();
                    break;
                case "logout":
                    logout();
                    break;
                case "join":
                    join(args);
                    break;
                case "observe":
                    observe(args);
                    break;
                default:
                    System.out.println("Command not recognized. Type help to see a list of commands");
                    break;
            }
        }
    }

    private static void list(){
        try{
            GameData[] games = facade.listGames();
            for(GameData game : games){
                String white = game.whiteUsername() == null ? "" : game.whiteUsername();
                String black = game.blackUsername() == null ? "" : game.blackUsername();
                System.out.printf("\t[%d] %s - { '%s' | '%s' }%n",game.gameID(), game.gameName(), white, black);
            }
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }

    private static void create(ArrayList<String> args){
        String gameName;
        if(hasGoodParams(args.size(), 1)){
            gameName = args.get(1);
        } else {
            return;
        }

        try{
            int ID = facade.newGame(gameName);
            System.out.println("Game: " + gameName + " created with ID: " + ID);
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }

    private static void join(ArrayList<String> args){
        int gameID;
        String teamIN;
        ChessGame.TeamColor team;

        if(hasGoodParams(args.size(), 2)){
            gameID = Integer.parseInt(args.get(1));
            teamIN = args.get(2);
            if(Objects.equals(teamIN, "WHITE") || Objects.equals(teamIN, "white") || Objects.equals(teamIN, "White")){
                team = ChessGame.TeamColor.WHITE;
            } else if(Objects.equals(teamIN, "BLACK") || Objects.equals(teamIN, "black") || Objects.equals(teamIN, "Black")){
                team = ChessGame.TeamColor.BLACK;
            } else {
                System.out.println("Team color not input correctly. Please try again");
                return;
            }
        } else {
            return;
        }

        try{
            facade.joinGame(team, gameID);
            GameplayUI.start(facade, currUser, findGame(gameID), team);
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }

    private static void observe(ArrayList<String> args){
        int gameID;

        if(hasGoodParams(args.size(), 1)){
            gameID = Integer.parseInt(args.get(1));
        } else {
            return;
        }

        try{
            facade.joinGame(null, gameID);
            GameplayUI.start(facade, currUser,findGame(gameID), null);
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }

    private static GameData findGame(int gameID){
        try {
            GameData[] games = facade.listGames();
            for(GameData game : games){
                if(game.gameID() == gameID){
                    return game;
                }
            }
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
        return null;
    }

    private static void logout(){
        try {
            facade.logout();
            running = false;
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }
}
