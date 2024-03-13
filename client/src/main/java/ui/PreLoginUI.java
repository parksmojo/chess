package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PreLoginUI {
    public static void run(){
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
                    if(login()) {
                        System.out.println("successful");
                    } else {
                        System.out.println("unsuccessful");
                    }
                    break;
                case "register":
                    System.out.print("register ");
                    if(register()) {
                        System.out.println("successful");
                    } else {
                        System.out.println("unsuccessful");
                    }
                    break;
                default:
                    System.out.println("Command not recognized. Type help to see a list of commands");
                    break;
            }
        }
    }

    private static boolean login(){
        return false;
    }

    private static boolean register(){
        return false;
    }
}
