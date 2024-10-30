package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.exception.ValidationException;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if (entity.getId().getFirst() == null || entity.getId().getSecond() == null)
            throw new ValidationException("Friendship is not valid");
    }
}
