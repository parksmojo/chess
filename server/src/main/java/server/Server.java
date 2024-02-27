package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private final GameService gameService = new GameService();
    private final UserService userService = new UserService();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res){
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        AuthData auth = userService.register(user.username(), user.password(), user.email());
        if(auth == null){
            res.status(403);
            return new Gson().toJson(new FailureResponse("already taken"));
        }
        else{
            res.status(200);
            return new Gson().toJson(auth);
        }
    }

    private Object clear(Request req, Response res){
        gameService.clear();
        res.status(200);
        return "{}";
    }
}
