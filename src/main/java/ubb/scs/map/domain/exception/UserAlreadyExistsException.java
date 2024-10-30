package ubb.scs.map.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {
    private static final String MESSAGE = "User already exists: %s";

    public UserAlreadyExistsException(String user) {
        super(String.format(MESSAGE, user));
    }
}
