package backend.exceptions;

public class ObjectNotFoundException extends DatabaseConflictException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
