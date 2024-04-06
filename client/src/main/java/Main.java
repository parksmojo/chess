import exception.ResponseException;
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

        System.out.println("♕ 240 Chess Client: Type help to get started");
        try {
            PreLoginUI.start(facade);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
    }
}