package ubb.scs.map.domain.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String MESSAGE = "User with id %d not found";
    private static final String S_MESSAGE = "User with username \"%s\" not found";

    public UserNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
    public UserNotFoundException(String username) {
        super(String.format(S_MESSAGE, username));
    }
}
