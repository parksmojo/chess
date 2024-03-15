package ui;

import exception.ResponseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PreLoginUI {
    private static ServerFacade server;

    public PreLoginUI(ServerFacade server){
        PreLoginUI.server = server;
        run();
    }

    public void run(){
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
                    System.out.println("Command not recognized. Type help to see a list of commands");
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
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }

    private static boolean hasGoodParams(int actual, int expected){
        expected++;
        if(actual == expected) {
            return true;
        } else if (actual < expected){
            System.out.println("Not enough arguments entered");
            return false;
        } else {
            System.out.println("Too many arguments entered");
            return false;
        }
    }

    private static void printError(int code){
        switch (code){
            case 400:
                System.out.println("Error: bad request");
                break;
            case 401:
                System.out.println("Error: unauthorized");
                break;
            case 403:
                System.out.println("Username already taken");
                break;
            default:
                System.out.println("Unknown error occurred");
                break;
        }
    }
}
