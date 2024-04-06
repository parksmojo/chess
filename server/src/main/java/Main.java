import server.Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        int port = server.run(8080);
        System.out.printf("Server started on port %d%n", port);

        Scanner scanner = new Scanner(System.in);
        while(!scanner.nextLine().equals("q")){
            System.out.println("type quit to stop server");
        }

        server.stop();
    }
}