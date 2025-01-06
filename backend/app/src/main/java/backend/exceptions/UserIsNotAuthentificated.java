package backend.exceptions;

public class UserIsNotAuthentificated extends Exception {
    public UserIsNotAuthentificated(String message) {
        super(message);
    }
}
