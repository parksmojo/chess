package server;

import com.google.gson.Gson;
import model.*;
import service.*;
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
        try {
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            if(user.username() == null || user.password() == null || user.email() == null){
                res.status(400);
                return new Gson().toJson(new FailureResponse("bad request"));
            }

            AuthData auth = userService.register(user.username(), user.password(), user.email());
            if(auth == null){
                res.status(403);
                return new Gson().toJson(new FailureResponse("already taken"));
            }

            res.status(200);
            return new Gson().toJson(auth);

        } catch (Exception e){
            res.status(500);
            return new Gson().toJson(new FailureResponse("Error: " + e));
        }
    }

    private Object clear(Request req, Response res){
        try {
            gameService.clear();
            res.status(200);
            return "{}";
        } catch (Exception e){
            res.status(500);
            return new Gson().toJson(new FailureResponse("Error: " + e));
        }
    }
}
