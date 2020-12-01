package ua.com.foxminded.school.dao.impl;

import org.apache.log4j.Logger;
import ua.com.foxminded.school.dao.ConnectorDB;
import ua.com.foxminded.school.dao.CrudPageableDao;
import ua.com.foxminded.school.dao.exception.DataBaseSqlRuntimeException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPageableCrudDaoImpl<E> extends AbstractCrudDaoImpl<E> implements CrudPageableDao<E> {

    private static final Logger LOGGER = Logger.getLogger(AbstractPageableCrudDaoImpl.class);

    private final String findAllWithPagesQuery;
    private final String countQuery;

    protected AbstractPageableCrudDaoImpl(ConnectorDB connector, String saveQuery, String findByIdQuery,
                                          String findAllNoPagesQuery, String updateQuery, String deleteQuery,
                                          String findAllWithPagesQuery, String countQuery) {
        super(connector, saveQuery, findByIdQuery, findAllNoPagesQuery, updateQuery, deleteQuery);
        this.findAllWithPagesQuery = findAllWithPagesQuery;
        this.countQuery = countQuery;
    }

    public List<E> findAll(int page, int itemsPerPage){
        int offset = itemsPerPage * (page - 1);

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllWithPagesQuery)){
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, itemsPerPage);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                List<E> entities = new ArrayList<>();
                while (resultSet.next()){
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }

        } catch (SQLException e) {
            LOGGER.info("Exception of finding: ", e);
            throw new DataBaseSqlRuntimeException("Error at finding: ", e);
        }
    }

    public long count(){
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(countQuery)){
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            LOGGER.info("Error at counting: ", e);
            throw new DataBaseSqlRuntimeException("Error at counting: ", e);
        }
    }

}
