package ubb.scs.map.domain.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String MESSAGE = "User with id %d not found";

    public UserNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
