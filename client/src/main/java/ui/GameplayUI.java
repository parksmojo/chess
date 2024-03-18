package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameplayUI extends UIHelper{
    private static ServerFacade server;
    private static String currUser;
    private static GameData game;

    public static void start(ServerFacade serverFacade, String user, GameData gameModel){
        server = serverFacade;
        currUser = user;
        game = gameModel;
        run();
    }

    private static void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.print("[" + currUser + "] >>> ");
            String[] input = scanner.nextLine().split(" ");
            ArrayList<String> args = new ArrayList<>(Arrays.asList(input));
            if (args.isEmpty() && args.getFirst() != null) {
                continue;
            }
            String command = args.getFirst();

            switch (command) {
                case "help":
                    System.out.print("""
                                quit - to quit game
                                help - to see possible commands
                                
                            """);
                    break;
                case "quit":
                    running = false;
                    break;
                default:
                    System.out.println("Command not recognized. Type help to see a list of commands");
                    break;
            }
        }
    }
}
