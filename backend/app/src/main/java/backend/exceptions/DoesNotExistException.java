package backend.exceptions;

public class DoesNotExistException extends Exception {
    public DoesNotExistException(String message) {
        super("User with name '" + message + "' was not found.");
    }
}
