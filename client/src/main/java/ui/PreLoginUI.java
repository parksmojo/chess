package ui;

import exception.ResponseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PreLoginUI extends UIHelper{
    private static ServerFacade server;

    public static void start(ServerFacade serverFacade){
        server = serverFacade;
        run();
    }

    private static void run(){
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while(running){
            System.out.print("[LOGGED_OUT] >>> ");
            String[] input = scanner.nextLine().split(" ");
            ArrayList<String> args = new ArrayList<>(Arrays.asList(input));
            if(args.isEmpty() && args.getFirst() != null){
                continue;
            }
            String command = args.getFirst();

            switch (command) {
                case "quit":
                    running = false;
                    break;
                case "help":
                    System.out.print("""
                                register <username> <password> <email> - to create an account
                                login <username> <password> - to play chess
                                quit - to quit
                                help - to see possible commands
                                
                            """);
                    break;
                case "login":
                    login(args);
                    break;
                case "register":
                    register(args);
                    break;
                default:
                    System.out.println("\t" + "Command not recognized. Type help to see a list of commands");
                    break;
            }
        }
    }

    private static void register(ArrayList<String> args) {
        String username;
        String password;
        String email;
        if(hasGoodParams(args.size(),3)) {
            username = args.get(1);
            password = args.get(2);
            email = args.get(3);
        } else {
            return;
        }

        try {
            server.register(username, password, email);
            PostLoginUI.start(server, username);
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }

    private static void login(ArrayList<String> args){
        String username;
        String password;

        if(hasGoodParams(args.size(), 2)){
            username = args.get(1);
            password = args.get(2);
        } else {
            return;
        }

        try {
            server.login(username, password);
            System.out.println("\t" + "logged in as: " + username);
            PostLoginUI.start(server, username);
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }
}
