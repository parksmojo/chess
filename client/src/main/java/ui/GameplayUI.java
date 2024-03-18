package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameplayUI extends UIHelper{
    private static String currUser;
    private static GameData game;

    public static void start(String user, GameData gameModel){
        currUser = user;
        game = gameModel;
        displayBoard();
        run();
    }

    private static void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.print("[" + currUser + "] PLAYING >>> ");
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

    private static void displayBoard(){
        ChessBoard board = game.game().getBoard();
        String space = EscapeSequences.QUARTER_SPACE;

        printLetters(false);
        for(int i = 1; i <= 8; i++){
            System.out.print(space + i + space + "|");
            for(int j = 1; j <= 8; j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if(piece == null){
                    System.out.print(EscapeSequences.EMPTY);
                } else {
                    System.out.print(piece);
                }
                System.out.print("|");
            }
            System.out.print(space + i + "\n");
        }
        printLetters(false);
        System.out.print("\n");
        printLetters(true);
        for(int i = 8; i >= 1; i--){
            System.out.print(space + i + space + "|");
            for(int j = 8; j >= 1; j--){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if(piece == null){
                    System.out.print(EscapeSequences.EMPTY);
                } else {
                    System.out.print(piece);
                }
                System.out.print("|");
            }
            System.out.print(space + i + "\n");
        }
        printLetters(true);
    }
    private static void printLetters(boolean forward){
        System.out.print(EscapeSequences.EMPTY + EscapeSequences.N_SPACE);
        String space = EscapeSequences.QUARTER_SPACE;
        if(forward){
            for(char c = 'a'; c <= 'h'; ++c){
                System.out.print(space + c + space + EscapeSequences.N_SPACE);
            }
        } else{
            for(char c = 'h'; c >= 'a'; --c){
                System.out.print(space + c + space + EscapeSequences.N_SPACE);
            }
        }
        System.out.print("\n");
    }
}
