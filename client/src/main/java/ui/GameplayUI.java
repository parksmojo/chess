package ui;

import chess.*;
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
        System.out.println();
        System.out.println(message);
        System.out.print("[" + currUser + "] PLAYING >>> ");
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
                case "move":
                    movePiece(args);
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

    private static void movePiece(ArrayList<String> args){
        if(game.game().getTeamTurn() != userTeam){
            System.out.println("Not your turn");
            return;
        }

        String startStr;
        String endStr;
        String promoStr;
        if(hasGoodParams(args.size(), 3)){
            startStr = args.get(1);
            endStr = args.get(2);
            promoStr = args.get(3);
        } else {
            return;
        }

        ChessPosition startPos = convertPos(startStr);
        ChessPosition endPos = convertPos(endStr);
        ChessPiece.PieceType promoPiece = convertPiece(promoStr);
        if(startPos == null || endPos == null){
            System.out.println("Error: invalid positions");
            return;
        }

        ChessMove userMove = new ChessMove(startPos, endPos, promoPiece);
        try {
            facade.makeMove(game.gameID(), userMove);
        } catch (ResponseException e) {
            printError(e.StatusCode());
        }
    }
    private static ChessPosition convertPos(String input){
        int row;
        int col;
        if(input.length() == 2){
            row = Character.getNumericValue(input.charAt(1));
            col = switch (input.charAt(0)){
                case 'a' -> 1;
                case 'b' -> 2;
                case 'c' -> 3;
                case 'd' -> 4;
                case 'e' -> 5;
                case 'f' -> 6;
                case 'g' -> 7;
                case 'h' -> 8;
                default -> 0;
            };
            if(col == 0 || row < 1 || row > 8) {
                return null;
            }
            return new ChessPosition(row, col);
        }
        return null;
    }
    private static ChessPiece.PieceType convertPiece(String input){
        return switch (input.toLowerCase()){
            case "king" -> ChessPiece.PieceType.KING;
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> null;
        };
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
