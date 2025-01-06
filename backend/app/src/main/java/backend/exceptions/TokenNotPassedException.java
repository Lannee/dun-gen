package backend.exceptions;

public class TokenNotPassedException extends Exception {
    public TokenNotPassedException(String message) {
        super(message);
    }
}
