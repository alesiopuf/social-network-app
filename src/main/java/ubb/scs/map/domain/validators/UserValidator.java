package ubb.scs.map.domain.validators;


import ubb.scs.map.domain.User;
import ubb.scs.map.domain.exception.ValidationException;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        if (entity.getFirstName().equals(""))
            throw new ValidationException("User is not valid");
    }
}
