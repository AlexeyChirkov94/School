package ua.com.foxminded.school.dao.impl;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import ua.com.foxminded.school.dao.ConnectorDB;
import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.exception.DataBaseSqlRuntimeException;
import ua.com.foxminded.school.entity.CourseEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static ua.com.foxminded.school.utility.CollectionUtility.nullSafeListInitialize;

public class CourseDaoImpl extends AbstractPageableCrudDaoImpl<CourseEntity> implements CourseDao {

    private static final Logger LOGGER = Logger.getLogger(CourseDaoImpl.class);

    private static final String SAVE_QUERY = "INSERT INTO courses (course_name, course_description) VALUES (?, ?)";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from courses";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM courses WHERE course_id=?";
    private static final String FIND_ALL_NO_PAGES_QUERY = "SELECT * FROM courses order by course_id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = "SELECT * FROM courses order by course_id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String FIND_BY_STUDENT_ID_QUERY = "select c.course_id, c.course_name, c.course_description " +
           "from students s left join student_courses sc on s.student_id = sc.student_id " +
           "left join courses c on sc.course_id = c.course_id where s.student_id=?";
    private static final String UPDATE_QUERY = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM courses where course_id=?";
    private static final String ADD_TO_COURSE_QUERY = "INSERT INTO student_courses (student_id, course_id) VALUES (?, ?)";
    private static final String DELETE_FROM_QUERY = "DELETE FROM student_courses where student_id=? and course_id=?";

    @Inject
    public CourseDaoImpl(ConnectorDB connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY,
                DELETE_QUERY, FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public void addToCourse(Integer studentId, Integer courseId) {
        addOrDelStudentToCourse(studentId, courseId, ADD_TO_COURSE_QUERY);
    }

    @Override
    public void addToCourse(Set<Integer> studentIds, Integer courseId) {
        addOrDelStudentsToCourse(studentIds, courseId, ADD_TO_COURSE_QUERY);
    }

    @Override
    public void deleteFromCourse(Integer studentId, Integer courseId) {
        addOrDelStudentToCourse(studentId, courseId, DELETE_FROM_QUERY);
    }

    @Override
    public void deleteFromCourse(Set<Integer> studentIds, Integer courseId) {
        addOrDelStudentsToCourse(studentIds, courseId, DELETE_FROM_QUERY);
    }

    @Override
    public List<CourseEntity> findByStudentId(Integer studentId) {
        List<CourseEntity> result = findManyByIntegerParam(studentId, FIND_BY_STUDENT_ID_QUERY);

        return result.get(0).getCourseName() == null ? nullSafeListInitialize(null) : result;
    }

    @Override
    protected CourseEntity mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return CourseEntity.builder()
                .withId(resultSet.getInt("course_id"))
                .withCourseName(resultSet.getString("course_name"))
                .withCourseDescription(resultSet.getString("course_description"))
                .build();
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, CourseEntity entity) throws SQLException {
        preparedStatement.setString(1, entity.getCourseName());
        preparedStatement.setString(2, entity.getCourseDescription());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, CourseEntity entity) throws SQLException {
        insert(preparedStatement, entity);
        preparedStatement.setInt(3, entity.getId());
    }

    private void addOrDelStudentToCourse(int studentId, int courseId, String addOrDelQuery){
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addOrDelQuery)){
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.info("Exception of adding/deleting from course: ", e);
            throw new DataBaseSqlRuntimeException("Error of adding/deleting from course: ", e);
        }
    }

    private void addOrDelStudentsToCourse(Set<Integer> studentIds, int courseId, String addOrDelQuery){
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addOrDelQuery)){
            for(Integer studentId : studentIds){
                preparedStatement.setInt(1, studentId);
                preparedStatement.setInt(2, courseId);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            LOGGER.info("Exception of adding/deleting from course: ", e);
            throw new DataBaseSqlRuntimeException("Error of adding/deleting from course: ", e);
        }
    }
}
