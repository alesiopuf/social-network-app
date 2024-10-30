package ubb.scs.map.domain.exception;

import ubb.scs.map.domain.Tuple;

public class FriendshipNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Friendship with id <%d,%d> not found";

    public FriendshipNotFoundException(Tuple<Long, Long> id) {
        super(String.format(MESSAGE, id.getFirst(), id.getSecond()));
    }
}
