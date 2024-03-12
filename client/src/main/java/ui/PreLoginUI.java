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
            System.out.printf("received: %s%n", args);
            String command = args.getFirst();

            switch (command) {
                case "quit":
                    running = false;
                    break;
                case "help":
                    System.out.println("helping");
                    break;
                case "login":
                    System.out.println("logging in");
                    break;
                case "register":
                    System.out.println("registering");
                    break;
                default:
                    System.out.println("Command not recognized. Please try again");
                    break;
            }
        }
    }
}
