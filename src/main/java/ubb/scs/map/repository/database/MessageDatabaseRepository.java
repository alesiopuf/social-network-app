package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Message;
import ubb.scs.map.domain.User;
import ubb.scs.map.domain.validators.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageDatabaseRepository extends AbstractDatabaseRepository<Long, Message> {

    public MessageDatabaseRepository(Validator<Message> messageValidator) {
        super(messageValidator);
    }

    @Override
    protected String getTableName() {
        return Message.class.getSimpleName() + "s";
    }

    @Override
    protected String getColumnsForInsert() {
        return "(from_user, to_users, message, date)";
    }

    @Override
    protected String getSQLIdForEntityId(Long id) {
        return "id = " + id;
    }

    @Override
    protected String getSQLValuesForEntity(Message entity) {
        String parsedDestination = "{" + entity.getTo().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ")) + "}";
        return "(" + entity.getFrom() + ", '" +
                parsedDestination + "', '" + entity.getMessage() + "', '" + entity.getDate() + "')";
    }

    @Override
    protected Message getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        Message message = new Message(resultSet.getLong(2), parseUsers(resultSet.getString(3)),
                resultSet.getString(4), resultSet.getTimestamp(5).toLocalDateTime());
        message.setId(resultSet.getLong(1));
        return message;
    }

    private List<Long> parseUsers(String usersString) {
        return Stream.of(usersString.replaceAll("[{}\"]", "").split(",\\s*"))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
