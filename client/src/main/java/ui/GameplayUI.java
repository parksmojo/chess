package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameplayUI extends UIHelper{
    private static ServerFacade server;

    public static void start(ServerFacade serverFacade){
        server = serverFacade;
        run();
    }

    private static void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.print("[LOGGED_OUT] >>> ");
            String[] input = scanner.nextLine().split(" ");
            ArrayList<String> args = new ArrayList<>(Arrays.asList(input));
            if (args.isEmpty() && args.getFirst() != null) {
                continue;
            }
            String command = args.getFirst();

            switch (command) {
                case "help":
                    System.out.print("""
                                register <username> <password> <email> - to create an account
                                login <username> <password> - to play chess
                                quit - to quit
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
