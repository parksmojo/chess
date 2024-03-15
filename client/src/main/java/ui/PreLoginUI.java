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
                    System.out.print("login ");
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
        if(args.size() == 4) {
            username = args.get(1);
            password = args.get(2);
            email = args.get(3);
        } else if (args.size() < 4){
            System.out.println("Not enough arguments entered");
            return;
        } else {
            System.out.println("Too many arguments entered");
            return;
        }

        try {
            server.register(username, password, email);
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
        }
    }

}
