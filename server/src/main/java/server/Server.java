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
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::create);

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
            return new Gson().toJson(new FailureResponse(e.toString()));
        }
    }

    private Object login(Request req, Response res){
        try {
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            if(user == null){
                res.status(401);
                return new Gson().toJson(new FailureResponse("unauthorized"));
            }

            AuthData auth = userService.login(user.username(), user.password());
            if(auth == null){
                res.status(401);
                return new Gson().toJson(new FailureResponse("unauthorized"));
            }

            res.status(200);
            return new Gson().toJson(auth);
        } catch (Exception e){
            res.status(500);
            return new Gson().toJson(new FailureResponse(e.toString()));
        }
    }

    private Object logout(Request req, Response res){
        try {
            String authToken = req.headers("authorization");
            if(userService.logout(authToken)){
                res.status(200);
                return "{}";
            } else {
                res.status(401);
                return new Gson().toJson(new FailureResponse("unauthorized"));
            }
        } catch (Exception e){
            res.status(500);
            return new Gson().toJson(new FailureResponse(e.toString()));
        }
    }

    private Object create(Request req, Response res){
        try {
            String authToken = req.headers("authorization");
            System.out.println(authToken);
            if(authToken == null){
                res.status(400);
                return new Gson().toJson(new FailureResponse("bad request"));
            }

            GameData game = new Gson().fromJson(req.body(), GameData.class);
            if(game == null || game.gameName() == null){
                res.status(400);
                return new Gson().toJson(new FailureResponse("bad request"));
            }

            String gameName = game.gameName();
            int gameID = gameService.makeGame(authToken, gameName);
            System.out.println(gameID);

            if(gameID == -1){
                res.status(401);
                return new Gson().toJson(new FailureResponse("unauthorized"));
            } else if (gameID <= -2 || gameID == 0) {
                res.status(400);
                return new Gson().toJson(new FailureResponse("bad request"));
            } else {
                res.status(200);
                return new Gson().toJson(new GameIDResponse(gameID));
            }
        } catch (Exception e){
            res.status(500);
            return new Gson().toJson(new FailureResponse(e.toString()));
        }
    }

    private Object clear(Request req, Response res){
        try {
            gameService.clear();
            res.status(200);
            return "{}";
        } catch (Exception e){
            res.status(500);
            return new Gson().toJson(new FailureResponse(e.toString()));
        }
    }
}
