package ubb.scs.map.repository.database;

import ubb.scs.map.domain.User;
import ubb.scs.map.domain.validators.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabaseRepository extends AbstractDatabaseRepository<Long, User> {

    public UserDatabaseRepository(Validator<User> validator) {
        super(validator);
    }

    @Override
    protected String getTableName() {
        return User.class.getSimpleName() + "s";
    }

    @Override
    protected String getColumnsForInsert() {
        return "";
    }

    @Override
    protected String getSQLIdForEntityId(Long id) {
        return "id = " + id;
    }

    @Override
    protected String getSQLValuesForEntity(User entity) {
        return "(" + entity.getId() + ", '" + entity.getFirstName() + "', '" +
                entity.getLastName() + "', '" + entity.getUsername() + "', '" + entity.getPassword() + "')";
    }

    @Override
    protected User getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User(resultSet.getString(2), resultSet.getString(3),
                resultSet.getString(4), resultSet.getString(5));
        user.setId(resultSet.getLong(1));
        return user;
    }
}
