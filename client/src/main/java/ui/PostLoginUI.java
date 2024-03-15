package ui;

import exception.ResponseException;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PostLoginUI extends UIHelper{
    private static ServerFacade server;
    private static String currUser;
    private static boolean running = true;

    public static void start(ServerFacade serverFacade, String user){
        server = serverFacade;
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
                                join <id> [WHITE|BLACK|<empty>] - to play a game
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
                default:
                    System.out.println("Command not recognized. Type help to see a list of commands");
                    break;
            }
        }
    }

    private static void list(){
        try{
            GameData[] games = server.listGames();
            for(GameData game : games){
                System.out.printf("\t[%d] %s - { '%s' | '%s' }%n",game.gameID(), game.gameName(), game.whiteUsername(), game.blackUsername());
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
            server.newGame(gameName);
            System.out.println(gameName + " created successfully");
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }

    private static void logout(){
        try {
            server.logout();
            running = false;
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }
}
