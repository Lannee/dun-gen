package backend.exceptions;

public class FailedRequest extends Exception {
    public FailedRequest(String message) {
        super(message);
    }
}
