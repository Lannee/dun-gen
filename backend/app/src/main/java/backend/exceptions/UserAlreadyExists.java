package backend.exceptions;


public class UserAlreadyExists extends DatabaseConflictException {
    public UserAlreadyExists(String username) {
        super("User with login '" + username + "' already exists");
    }
}
