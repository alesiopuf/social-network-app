package ubb.scs.map.domain.exception;

public class FriendshipAlreadyExistsException extends RuntimeException {
    private static final String MESSAGE = "Friendship already exists: %s";

    public FriendshipAlreadyExistsException(String friendship) {
        super(String.format(MESSAGE, friendship));
    }
}
