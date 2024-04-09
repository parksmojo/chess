import exception.ResponseException;
import ui.EscapeSequences;
import ui.PreLoginUI;
import ui.ServerFacade;

public class Main {
    public static void main(String[] args) {
        ServerFacade facade;
        try {
            facade = new ServerFacade(8080);
        } catch (ResponseException e) {
          throw new RuntimeException(e);
        }

        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.println("♕ 240 Chess Client: Type help to get started");
        try {
            PreLoginUI.start(facade);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
    }
}