package server;

public class FailureResponse {
    private String message;

    public FailureResponse(String message) {
        this.message = "Error: " + message;
    }
}
