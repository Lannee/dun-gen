package backend.exceptions;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(String message) {
        super("Wrong password for user '" + message + "'.");
    }
}
