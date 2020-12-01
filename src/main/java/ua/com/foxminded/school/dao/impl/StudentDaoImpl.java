package ua.com.foxminded.school.dao.impl;

import com.google.inject.Inject;
import ua.com.foxminded.school.dao.ConnectorDB;
import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.entity.StudentEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StudentDaoImpl extends AbstractPageableCrudDaoImpl<StudentEntity> implements StudentDao {

    private static final String SAVE_QUERY = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final String COUNT_QUERY = "SELECT COUNT(*) as count from students";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM students WHERE student_id=?";
    private static final String FIND_STUDENTS_BY_COURSE_NAME = "SELECT s.student_id, s.group_id, s.first_name, s.last_name " +
            "from students s left join student_courses sc on s.student_id = sc.student_id " +
            "left join courses c on sc.course_id = c.course_id where c.course_name=?";
    private static final String FIND_ALL_NO_PAGES_QUERY = "SELECT * FROM students order by student_id";
    private static final String FIND_ALL_WITH_PAGES_QUERY = "SELECT * FROM students order by student_id offset ? row FETCH NEXT ? ROWS ONLY";
    private static final String UPDATE_QUERY = "UPDATE students SET group_id = ?, first_name = ?, last_name = ? WHERE student_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM students where student_id=?";
    private static final String FIND_BY_GROUP_ID_QUERY = "SELECT * FROM students WHERE group_id=?";
    private static final String FIND_BY_FIRST_NAME_QUERY = "SELECT * FROM students WHERE first_name=?";
    private static final String FIND_BY_LAST_NAME_QUERY = "SELECT * FROM students WHERE last_name=?";

    @Inject
    public StudentDaoImpl(ConnectorDB connector){
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_NO_PAGES_QUERY, UPDATE_QUERY,
                DELETE_QUERY, FIND_ALL_WITH_PAGES_QUERY, COUNT_QUERY);
    }

    @Override
    public List<StudentEntity> findByGroupId(Integer groupId) {
        return findManyByIntegerParam(groupId, FIND_BY_GROUP_ID_QUERY);
    }

    @Override
    public List<StudentEntity> findByFirstName(String firstName) {
        return findManyByStringParam(firstName, FIND_BY_FIRST_NAME_QUERY);
    }

    @Override
    public List<StudentEntity> findByLastName(String lastName) {
        return findManyByStringParam(lastName, FIND_BY_LAST_NAME_QUERY);
    }

    @Override
    public List<StudentEntity> findByCourseName(String courseName) {
        return findManyByStringParam(courseName, FIND_STUDENTS_BY_COURSE_NAME);
    }

    @Override
    protected StudentEntity mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        CourseDao courseDao = new CourseDaoImpl(connector);
            return StudentEntity.builder()
                    .withId(resultSet.getInt("student_id"))
                    .withGroup(resultSet.getInt("group_id"))
                    .withFirstName(resultSet.getString("first_name"))
                    .withLastName(resultSet.getString("last_name"))
                    .withCourses(courseDao.findByStudentId(resultSet.getInt("student_id")))
                    .build();
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, StudentEntity entity) throws SQLException {
        if(entity.getGroupId() != null){
            preparedStatement.setInt(1, entity.getGroupId());
        }else {
            preparedStatement.setNull(1, java.sql.Types.INTEGER);
        }
        preparedStatement.setString(2, entity.getFirstName());
        preparedStatement.setString(3, entity.getLastName());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, StudentEntity entity) throws SQLException {
        insert(preparedStatement, entity);
        preparedStatement.setInt(4, entity.getId());
    }

}
