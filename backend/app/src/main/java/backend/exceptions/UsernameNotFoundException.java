package backend.exceptions;

public class UsernameNotFoundException extends DatabaseConflictException {

    public UsernameNotFoundException(String message) {
        super(message);
    }    
}
