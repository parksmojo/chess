import server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        int port = server.run(8080);
        System.out.printf("Server started on port %d%n", port);
    }
}