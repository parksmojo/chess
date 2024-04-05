import exception.ResponseException;
import server.Server;
import ui.PreLoginUI;
import ui.ServerFacade;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        ServerFacade facade;
        try {
            facade = new ServerFacade(port);
        } catch (ResponseException e) {
          throw new RuntimeException(e);
        }

      System.out.println("♕ 240 Chess Client: Type help to get started");
        try {
            PreLoginUI.start(facade);
        } finally {
            server.stop();
        }
    }
}