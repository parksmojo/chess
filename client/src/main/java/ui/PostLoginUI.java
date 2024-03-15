package ui;

import exception.ResponseException;

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
                case "logout":
                    logout();
                    break;
                default:
                    System.out.println("Command not recognized. Type help to see a list of commands");
                    break;
            }
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
