package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.exception.DatabaseConnectionException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.util.DatabaseConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDatabaseRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final Connection connection;
    private final Validator<E> validator;

    protected AbstractDatabaseRepository(Validator<E> validator) {
        this.validator = validator;
        try {
            this.connection = DatabaseConnectionUtil.getConnection();
        } catch (SQLException e) {
            throw new DatabaseConnectionException();
        }
    }

    @Override
    public Optional<E> findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        String query = "SELECT * FROM " + getTableName() + " WHERE " + getSQLIdForEntityId(id);
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Optional.of(getEntityFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<E> findAll() {
        String query = "SELECT * FROM " + getTableName();
        List<E> entities = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                E entity = getEntityFromResultSet(resultSet);
                entities.add(entity);
            }
        } catch (SQLException e) {
            return entities;
        }
        return entities;
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("ENTITY must not be null");
        }
        validator.validate(entity);
        String query = "INSERT INTO " + getTableName() + getColumnsForInsert() + " VALUES " + getSQLValuesForEntity(entity);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<E> delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        Optional<E> entity = findOne(id);
        String query = "DELETE FROM " + getTableName() + " WHERE " + getSQLIdForEntityId(id);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            return Optional.empty();
        }
        return entity;
    }

    @Override
    public Optional<E> update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("ENTITY must not be null");
        }
        validator.validate(entity);
        String query = "UPDATE " + getTableName() + " SET " + getSQLValuesForEntity(entity) +
                " WHERE " + getSQLIdForEntityId(entity.getId());
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    protected abstract String getTableName();

    protected abstract String getColumnsForInsert();

    protected abstract String getSQLIdForEntityId(ID id);

    protected abstract String getSQLValuesForEntity(E entity);

    protected abstract E getEntityFromResultSet(ResultSet resultSet) throws SQLException;
}
