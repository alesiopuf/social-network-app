package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Message;
import ubb.scs.map.domain.exception.ValidationException;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        if (entity.getFrom() == null || entity.getTo() == null || entity.getMessage().equals(""))
            throw new ValidationException("Message is not valid");
    }
}
