package ua.com.foxminded.school.dao.impl;

import org.apache.log4j.Logger;
import ua.com.foxminded.school.dao.ConnectorDB;
import ua.com.foxminded.school.dao.CrudDao;
import ua.com.foxminded.school.dao.exception.DataBaseSqlRuntimeException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E> {

    private static final Logger LOGGER = Logger.getLogger(AbstractCrudDaoImpl.class);
    private static final String MESSAGE_AT_SAVE_ERROR = "Error of save: ";
    private static final String MESSAGE_AT_UPDATE_ERROR = "Error of updating: ";
    private static final String MESSAGE_AT_DELETE_ERROR = "Error of deleting: ";
    private static final String MESSAGE_AT_FINDING_ERROR = "Error of finding: ";


    protected static final BiConsumer<PreparedStatement, Integer> INT_PARAM_SETTER = (preparedStatement, integer) -> {
        try{
            preparedStatement.setInt(1, integer);
        } catch (SQLException e){
            LOGGER.info("Exception of INT_PARAM_SETTER: ", e);
        }
    };

    protected static final BiConsumer<PreparedStatement, String> STRING_PARAM_SETTER = (preparedStatement, string) -> {
        try {
            preparedStatement.setString(1, string);
        } catch (SQLException e) {
            LOGGER.info("Exception of STRING_PARAM_SETTER: ", e);
        }
    };

    protected final ConnectorDB connector;
    private final String saveQuery;
    private final String findByIdQuery;
    protected final String findAllNoPagesQuery;
    private final String updateQuery;
    private final String deleteQuery;

    protected AbstractCrudDaoImpl(ConnectorDB connector, String saveQuery, String findByIdQuery,
                                  String findAllNoPagesQuery, String updateQuery, String deleteQuery) {
        this.connector = connector;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllNoPagesQuery = findAllNoPagesQuery;
        this.updateQuery = updateQuery;
        this.deleteQuery = deleteQuery;
    }

    @Override
    public void save(E entity){
        try (Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)){
            insert(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.info(MESSAGE_AT_SAVE_ERROR, e);
            throw new DataBaseSqlRuntimeException(MESSAGE_AT_SAVE_ERROR, e);
        }
    }

    @Override
    public void saveAll(List<E> entities){
        try(Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)){
            for(E entity : entities){
                insert(preparedStatement, entity);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            LOGGER.info(MESSAGE_AT_SAVE_ERROR, e);
            throw new DataBaseSqlRuntimeException(MESSAGE_AT_SAVE_ERROR, e);
        }
    }

    @Override
    public Optional<E> findById(Integer id) {
        return findByIntegerParam(id, findByIdQuery);
    }

    @Override
    public List<E> findAll(){
        try(Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllNoPagesQuery)){
            try (final ResultSet resultSet = preparedStatement.executeQuery()){
                List<E> entities = new ArrayList<>();
                while (resultSet.next()){
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        }catch (SQLException e) {
            LOGGER.info("Error of findAll: ", e);
            throw new DataBaseSqlRuntimeException("Error of findAll: ", e);
        }
    }

    @Override
    public void update(E entity){
        try(Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)){
            updateValues(preparedStatement, entity);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            LOGGER.info(MESSAGE_AT_UPDATE_ERROR, e);
            throw new DataBaseSqlRuntimeException(MESSAGE_AT_UPDATE_ERROR, e);
        }
    }

    @Override
    public void updateAll(List<E> entities){
        try(Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)){
            for(E entity : entities){
                updateValues(preparedStatement, entity);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            LOGGER.info(MESSAGE_AT_UPDATE_ERROR, e);
            throw new DataBaseSqlRuntimeException(MESSAGE_AT_UPDATE_ERROR, e);
        }
    }

    @Override
    public void deleteById(Integer id){
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)){
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.info(MESSAGE_AT_DELETE_ERROR, e);
            throw new DataBaseSqlRuntimeException(MESSAGE_AT_DELETE_ERROR, e);
        }
    }

    @Override
    public void deleteByIds(Set<Integer> ids){
        try(Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)){
            for(Integer id : ids){
                preparedStatement.setInt(1, id);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            LOGGER.info(MESSAGE_AT_DELETE_ERROR, e);
            throw new DataBaseSqlRuntimeException(MESSAGE_AT_DELETE_ERROR, e);
        }
    }

    protected  <P> Optional<E> findByParam(P param, String findByParamQuery,
                                           BiConsumer<PreparedStatement, P> designatedParamSetter){
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findByParamQuery)) {
            designatedParamSetter.accept(preparedStatement, param);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.ofNullable(mapResultSetToEntity(resultSet)) : Optional.empty();
            }

        } catch (SQLException e) {
            LOGGER.info(MESSAGE_AT_FINDING_ERROR, e);
            throw new DataBaseSqlRuntimeException(MESSAGE_AT_FINDING_ERROR, e);
        }
    }

    protected Optional<E> findByIntegerParam(Integer id, String query) {
        return findByParam(id, query, INT_PARAM_SETTER);
    }

    protected Optional<E> findByStringParam(String param, String query) {
        return findByParam(param, query, STRING_PARAM_SETTER);
    }

    protected  <P> List<E> findManyByParam(P param, String findByParamQuery,
                                           BiConsumer<PreparedStatement, P> designatedParamSetter) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findByParamQuery)) {
            designatedParamSetter.accept(preparedStatement, param);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                List<E> entities = new ArrayList<>();
                while (resultSet.next()){
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }

        } catch (SQLException e) {
            LOGGER.info(MESSAGE_AT_FINDING_ERROR, e);
            throw new DataBaseSqlRuntimeException(MESSAGE_AT_FINDING_ERROR, e);
        }
    }

    protected List<E> findManyByIntegerParam(Integer id, String query) {
        return findManyByParam(id, query, INT_PARAM_SETTER);
    }

    protected List<E> findManyByStringParam(String param, String query) {
        return findManyByParam(param, query, STRING_PARAM_SETTER);
    }

    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void insert(PreparedStatement preparedStatement, E entity) throws SQLException;

    protected abstract void updateValues(PreparedStatement preparedStatement, E entity) throws SQLException;

}
