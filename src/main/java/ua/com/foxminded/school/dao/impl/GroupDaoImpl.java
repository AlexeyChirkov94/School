package ua.com.foxminded.school.dao.impl;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import ua.com.foxminded.school.dao.ConnectorDB;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.exception.DataBaseSqlRuntimeException;
import ua.com.foxminded.school.entity.GroupEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class GroupDaoImpl extends AbstractPageableCrudDaoImpl<GroupEntity> implements GroupDao {

    private static final Logger LOGGER = Logger.getLogger(GroupDaoImpl.class);

    private static final String SAVE_QUERY = "INSERT INTO groups (group_name) VALUES (?)";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from groups";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM groups WHERE group_id=?";
    private static final String FIND_ALL_NO_PAGES_QUERY = "SELECT * FROM groups order by group_id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = "SELECT * FROM groups order by group_id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE groups SET group_name = ? WHERE group_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM groups where group_id=?";
    private static final String FIND_BY_GROUP_NAME_QUERY = "SELECT * FROM groups WHERE group_name=?";
    private static final String COUNT_MEMBERS_OF_GROUP_QUERY = "SELECT COUNT(*) as count from students s where s.group_id=?";
    private static final String FIND_BY_COUNT_OF_GROUP_MEMBERS = "SELECT g.group_id as group_id, g.group_name as group_name, " +
            "COUNT(s.group_id) AS count_of_group_members FROM groups g LEFT JOIN students s ON g.group_id = s.group_id " +
            "GROUP BY g.group_id HAVING COUNT(s.group_id) <= ? order by g.group_id";

    private static final String CHANGE_STUDENT_GROUP_QUERY = "UPDATE students SET group_id = ? WHERE student_id = ?";
    private static final String DELETE_STUDENT_FROM_GROUP_QUERY = "UPDATE students SET group_id = null WHERE student_id = ?";

    @Inject
    public GroupDaoImpl(ConnectorDB connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY,
                DELETE_QUERY, FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public Optional<GroupEntity> findByGroupName(String groupName) {
        return findByStringParam(groupName, FIND_BY_GROUP_NAME_QUERY);
    }

    @Override
    public void transferStudent(Integer groupIdWhere, Integer studentId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_STUDENT_GROUP_QUERY)){
            preparedStatement.setInt(1, groupIdWhere);
            preparedStatement.setInt(2, studentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.info("Error at transferring: ", e);
            throw new DataBaseSqlRuntimeException("Error at transferring: ", e);
        }
    }

    @Override
    public void addStudentToGroup(Integer groupId, Integer studentId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_STUDENT_GROUP_QUERY)){
            preparedStatement.setInt(1, groupId);
            preparedStatement.setInt(2, studentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.info("Error at adding to group: ", e);
            throw new DataBaseSqlRuntimeException("Error at adding to group: ", e);
        }
    }

    @Override
    public void deleteStudentFromGroup(Integer studentId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STUDENT_FROM_GROUP_QUERY)){
            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.info("Error at deleting from group: ", e);
            throw new DataBaseSqlRuntimeException("Error at deleting from group: ", e);
        }
    }

    @Override
    public int countMembersByGroupId(Integer groupId) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(COUNT_MEMBERS_OF_GROUP_QUERY)){
            preparedStatement.setInt(1, groupId);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            LOGGER.info("Error at finding: ", e);
            throw new DataBaseSqlRuntimeException("Error at finding: ", e);
        }
    }

    @Override
    public List<GroupEntity> findGroupWithEqualOrLessCountOfStudent(int countOfStudent) {
        return findManyByIntegerParam(countOfStudent, FIND_BY_COUNT_OF_GROUP_MEMBERS);
    }

    @Override
    protected GroupEntity mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return GroupEntity.builder()
                .withId(resultSet.getInt("group_id"))
                .withGroupName(resultSet.getString("group_name"))
                .build();
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, GroupEntity entity) throws SQLException {
        preparedStatement.setString(1, entity.getGroupName());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, GroupEntity entity) throws SQLException {
        insert(preparedStatement, entity);
        preparedStatement.setInt(2, entity.getId());
    }

}
