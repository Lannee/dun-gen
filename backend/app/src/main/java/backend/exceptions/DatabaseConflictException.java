package backend.exceptions;

public class DatabaseConflictException extends Exception {
    public DatabaseConflictException(String message) {
        super(message);
    }
}