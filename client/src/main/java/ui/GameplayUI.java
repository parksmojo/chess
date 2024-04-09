package ui;

import chess.*;
import exception.ResponseException;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
        System.out.print(displayBoard(null));
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
                                move <piece> <destination> <promotion piece | 'none'> - to make a chess move
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
                    System.out.print(displayBoard(null));
                    break;
                case "see":
                    seePaths(args);
                    break;
                case "resign":
                    resign();
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

        String moveStr;
        ChessPosition startPos;
        ChessPosition endPos;
        ChessPiece.PieceType promoPiece;
        if(hasGoodParams(args.size(), 3)){
            moveStr = args.get(1) + "->" + args.get(2);
            startPos = convertPos(args.get(1));
            endPos = convertPos(args.get(2));
            promoPiece = convertPiece(args.get(3));
        } else {
            return;
        }

        if(startPos == null || endPos == null){
            System.out.println("Error: invalid positions");
            return;
        }

        ChessMove userMove = new ChessMove(startPos, endPos, promoPiece);
        try {
            facade.makeMove(game.gameID(), userMove, moveStr);
        } catch (ResponseException e) {
            printError(e.statusCode());
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
            printError(e.statusCode());
        }
    }

    private static void resign(){
        try {
            facade.resign(game.gameID());
        } catch (ResponseException e) {
            printError(e.statusCode());
        }
    }

    private static void seePaths(ArrayList<String> args){
        ChessPosition pos;
        if(hasGoodParams(args.size(), 1)){
            pos = convertPos(args.get(1));
        } else {
            return;
        }

        Collection<ChessMove> moves = game.game().validMoves(pos);
        Collection<ChessPosition> options = new ArrayList<>();
        options.add(pos);
        for(ChessMove move : moves){
            options.add(move.getEndPosition());
        }
        System.out.print(displayBoard(options));
    }

    private static String displayBoard(Collection<ChessPosition> paths){
        StringBuilder output = new StringBuilder();
        ChessBoard board = game.game().getBoard();
        String space = EscapeSequences.QUARTER_SPACE;

        output.append("\n"); //System.out.println();
        if(userTeam == ChessGame.TeamColor.BLACK) {
            output.append(printLetters(false));
            for (int i = 1; i <= 8; i++) {
                output.append(String.format("%s%d%s|",space,i,space)); //System.out.print(space + i + space + "|");
                for (int j = 8; j >= 1; j--) {
                    output.append(printPiece(board,i,j,paths)); //System.out.print(printPiece(board,i,j,paths));
                }
                output.append(String.format("%s%d\n",space,i)); //System.out.print(space + i + "\n");
            }
            output.append(printLetters(false));
        } else {
            output.append(printLetters(true));
            for (int i = 8; i >= 1; i--) {
                output.append(String.format("%s%d%s|",space,i,space)); //System.out.print(space + i + space + "|");
                for (int j = 1; j <= 8; j++) {
                    output.append(printPiece(board,i,j,paths)); //System.out.print(printPiece(board,i,j,paths));
                }
                output.append(String.format("%s%d\n",space,i)); //System.out.print(space + i + "\n");
            }
            output.append(printLetters(true));
        }
        return output.toString();
    }
    private static String printPiece(ChessBoard board, int row, int col,Collection<ChessPosition> paths){
        String output = "";
        String highlight = "";
        String dehighlight = "";
        ChessPosition pos = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(pos);
        if(paths != null){
            if(paths.contains(pos)){
                highlight = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
                dehighlight = EscapeSequences.SET_BG_COLOR_DARK_GREY;
            }
        }

        output += highlight;
        if (piece == null) {
            output += EscapeSequences.EMPTY;
        } else {
            output += piece.toString();
        }
        output += dehighlight;

        output += "|";
        return output;
    }
    private static String printLetters(boolean forward){
        StringBuilder output = new StringBuilder();
        output.append(EscapeSequences.EMPTY + EscapeSequences.N_SPACE); //System.out.print(EscapeSequences.EMPTY + EscapeSequences.N_SPACE);
        String space = EscapeSequences.QUARTER_SPACE;
        if(forward){
            for(char c = 'a'; c <= 'h'; ++c){
                output.append(String.format("%s%s%s%s",space, c, space, EscapeSequences.N_SPACE)); //System.out.print(space + c + space + EscapeSequences.N_SPACE);
            }
        } else{
            for(char c = 'h'; c >= 'a'; --c){
                output.append(String.format("%s%s%s%s",space, c, space, EscapeSequences.N_SPACE)); //System.out.print(space + c + space + EscapeSequences.N_SPACE);
            }
        }
        output.append("\n"); //System.out.print("\n");
        return output.toString();
    }
}
