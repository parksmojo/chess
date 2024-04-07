package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameplayUI extends UIHelper implements GameHandler {
    private static ServerFacade facade;
    private static String currUser;
    private static ChessGame.TeamColor userTeam;
    private static GameData game;

    public static void start(ServerFacade serverFacade, String user, GameData gameModel, ChessGame.TeamColor team){
        facade = serverFacade;
        currUser = user;
        game = gameModel;
        userTeam = team;
        run();
    }

    @Override
    public void updateGame(GameData newGame) {
        game = newGame;
        displayBoard();
        System.out.print("[" + currUser + "] PLAYING >>> ");
    }

    @Override
    public void printMessage(String message) {

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
                                help - to see possible commands
                                draw - to redraw the chess board
                                see <piece> - to highlight legal moves
                                move <piece> <destination> - to make a chess move
                                resign - to forfeit the game
                                leave - to leave the game
                                
                            """);
                    break;
                case "leave":
                    leaveGame();
                    running = false;
                    break;
                case "draw":
                    displayBoard();
                    break;
                default:
                    System.out.println("Command not recognized. Type help to see a list of commands");
                    break;
            }
        }
    }

    private static void leaveGame(){
        try {
            facade.leaveGame(game.gameID());
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }

    private static void displayBoard(){
        ChessBoard board = game.game().getBoard();
        String space = EscapeSequences.QUARTER_SPACE;

        System.out.println();
        if(userTeam == ChessGame.TeamColor.BLACK) {
            printLetters(false);
            for (int i = 1; i <= 8; i++) {
                System.out.print(space + i + space + "|");
                for (int j = 8; j >= 1; j--) {
                    System.out.print(printPiece(board,i,j));
                }
                System.out.print(space + i + "\n");
            }
            printLetters(false);
        } else {
            printLetters(true);
            for (int i = 8; i >= 1; i--) {
                System.out.print(space + i + space + "|");
                for (int j = 1; j <= 8; j++) {
                    System.out.print(printPiece(board,i,j));
                }
                System.out.print(space + i + "\n");
            }
            printLetters(true);
        }
    }
    private static String printPiece(ChessBoard board, int row, int col){
        String output = "";
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            output += EscapeSequences.EMPTY;
        } else {
            output += piece.toString();
        }
        output += "|";
        return output;
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
