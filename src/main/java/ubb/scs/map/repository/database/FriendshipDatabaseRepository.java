package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Friendship;
import ubb.scs.map.domain.Tuple;
import ubb.scs.map.domain.validators.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<Tuple<Long, Long>, Friendship> {

    public FriendshipDatabaseRepository(Validator<Friendship> validator) {
        super(validator);
    }

    @Override
    protected String getTableName() {
        return Friendship.class.getSimpleName()+"s";
    }

    @Override
    protected String getSQLIdForEntityId(Tuple<Long, Long> id) {
        return "(userId1,userId2) = (" + id.getFirst() + ", " + id.getSecond() + ")";
    }

    @Override
    protected String getSQLValuesForEntity(Friendship entity) {
        return "(" + entity.getId().getFirst() + ", " + entity.getId().getSecond() + ", '" +
                Timestamp.valueOf(entity.getDate()) + "')";
    }

    @Override
    protected Friendship getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        return new Friendship(new Tuple<>(resultSet.getLong(1), resultSet.getLong(2)),
                resultSet.getTimestamp(3).toLocalDateTime());
    }
}
