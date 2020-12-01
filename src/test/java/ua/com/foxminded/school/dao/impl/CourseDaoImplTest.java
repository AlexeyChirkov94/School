package ua.com.foxminded.school.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.school.dao.ConnectorDB;
import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.dao.exception.DataBaseSqlRuntimeException;
import ua.com.foxminded.school.dao.tables.ScriptsRunner;
import ua.com.foxminded.school.dao.tables.ScriptsRunnerImpl;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.school.entity.CourseEntity;
import ua.com.foxminded.school.entity.StudentEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static ua.com.foxminded.school.utility.TestUtility.compareStudents;
import static ua.com.foxminded.school.utility.TestUtility.compareCourses;

@ExtendWith( MockitoExtension.class)
class CourseDaoImplTest {

    private static final Logger LOGGER = Logger.getLogger(CourseDaoImplTest.class);

    private static final String PATH_TO_DATABASE_PROPERTY = "property/database/schoolTest";
    private static final String PATH_TO_CREATE_TABLE_QUERY = "src\\test\\resources\\database\\schema.sql";
    private static final String PATH_TO_FILL_GROUPS_QUERY = "src\\test\\resources\\database\\groupFiller.sql";
    private static final String PATH_TO_FILL_STUDENTS_QUERY = "src\\test\\resources\\database\\studentFiller.sql";
    private static final String PATH_TO_FILL_COURSES_QUERY = "src\\test\\resources\\database\\courseFiller.sql";
    private static ConnectorDB connectorDB;
    private static ScriptsRunner scriptsRunner;
    private static StudentDao studentDao;
    private static CourseDao courseDao;

    @Mock
    private ConnectorDB mockConnectorDB;

    @Mock
    private Connection mockConnection;


    {
        try{
            connectorDB = new ConnectorDB(PATH_TO_DATABASE_PROPERTY);
            scriptsRunner = new ScriptsRunnerImpl(connectorDB);
            scriptsRunner.runScript(PATH_TO_CREATE_TABLE_QUERY);
            scriptsRunner.runScript(PATH_TO_FILL_GROUPS_QUERY);
            scriptsRunner.runScript(PATH_TO_FILL_STUDENTS_QUERY);
            scriptsRunner.runScript(PATH_TO_FILL_COURSES_QUERY);
            studentDao = new StudentDaoImpl(connectorDB);
            courseDao = new CourseDaoImpl(connectorDB);
        }catch (Exception e) {
            LOGGER.info("Exception of creating test data: ", e);
            throw new DataBaseSqlRuntimeException("Exception of creating test data: ", e);
        }
    }

    @Test
    void createAndReadShouldAddNewCourseToDatabaseIfArgumentIsCourseEntityAndReadIt(){
        CourseEntity addingCourse = CourseEntity.builder().withCourseName("NewDiscipline").withCourseDescription("Description").build();
        courseDao.save(addingCourse);
        CourseEntity readingCourse = courseDao.findById(11).get();

        compareCourses(readingCourse, addingCourse);
    }

    @Test
    void updateShouldUpdateDataOfCourseIfArgumentIsCourseEntity(){
        CourseEntity expected = CourseEntity.builder().withId(10).withCourseName("Trigonometry")
                .withCourseDescription("Pythagoras pants").build();
        courseDao.update(expected);
        CourseEntity actual = courseDao.findById(10).get();

        compareCourses(expected, actual);
    }

    @Test
    void deleteShouldDeleteDataOfCourseIfArgumentIsIdOfCourse(){
        Optional<CourseEntity> expected = Optional.empty();
        courseDao.deleteById(10);
        Optional<CourseEntity> actual = courseDao.findById(10);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void addAndDeleteFromCourseShouldAddOrDeleteStudentFromCourseIfArgumentsIsIdOfCourseAndIdOfStudent(){
        StudentEntity expected = StudentEntity.builder().withFirstName("Alexey").withLastName("Chirkov")
                .withGroup(1).withCourses(Arrays.asList(courseDao.findById(1).get())).build();
        courseDao.addToCourse(1, 1);
        StudentEntity actual = studentDao.findById(1).get();
        compareStudents(actual, expected);

        courseDao.deleteFromCourse(1, 1);
        expected = StudentEntity.builder().withFirstName("Alexey").withLastName("Chirkov")
                .withGroup(1).build();
        actual = studentDao.findById(1).get();
        compareStudents(actual, expected);
    }

    @Test
    void addAndDeleteFromCourseShouldAddAndDeleteSetOfStudentToCourseIfArgumentsIsIdOfCourseAndSetOfIdsOfStudent(){
        Set<Integer> studentIds = new HashSet<>();
        studentIds.add(1);
        studentIds.add(2);
        studentIds.add(3);

        List <StudentEntity> expected = Arrays.asList(
                StudentEntity.builder().withFirstName("Alexey").withLastName("Chirkov")
                        .withGroup(1).withCourses(Arrays.asList(courseDao.findById(1).get())).build(),
                StudentEntity.builder().withFirstName("Petr").withLastName("Ivanov").withGroup(1)
                        .withCourses(Arrays.asList(courseDao.findById(1).get())).build(),
                StudentEntity.builder().withFirstName("Olga").withLastName("Voronova")
                        .withCourses(Arrays.asList(courseDao.findById(1).get())).withGroup(1).build());
        courseDao.addToCourse(studentIds, 1);
        List<StudentEntity> actual = Arrays.asList(studentDao.findById(1).get(),
                studentDao.findById(2).get(), studentDao.findById(3).get());
        compareStudents(actual, expected);

        courseDao.deleteFromCourse(studentIds, 1);
        expected = Arrays.asList(
                StudentEntity.builder().withFirstName("Alexey").withLastName("Chirkov").withGroup(1).build(),
                StudentEntity.builder().withFirstName("Petr").withLastName("Ivanov").withGroup(1).build(),
                StudentEntity.builder().withFirstName("Olga").withLastName("Voronova").withGroup(1).build());
        actual = Arrays.asList(studentDao.findById(1).get(),
                studentDao.findById(2).get(), studentDao.findById(3).get());
        compareStudents(actual, expected);
    }

    @Test
    void addToCourseShouldThrowDataBaseSqlRuntimeExceptionIfStudentIdOutOfRange() {
        assertThatThrownBy(() -> courseDao.addToCourse(15, 1)).hasMessage("Error of adding/deleting from course: ");
    }

    @Test
    void addToCourseShouldThrowDataBaseSqlRuntimeExceptionIfOneOfStudentIdsOutOfRange() {
        Set<Integer> studentIds = new HashSet<>();
        studentIds.add(1);
        studentIds.add(15);

        assertThatThrownBy(() -> courseDao.addToCourse(studentIds, 1)).hasMessage("Error of adding/deleting from course: ");
    }

    @Test
    void addToCourseShouldThrowDataBaseSqlRuntimeExceptionIfCourseIdOutOfRange() {
        assertThatThrownBy(() -> courseDao.addToCourse(1, 15)).hasMessage("Error of adding/deleting from course: ");
    }

    @Test
    void deleteFromCourseShouldThrowDataBaseSqlRuntimeExceptionIfStudentIdOutOfRange() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of adding/deleting from course: ")).when(mockConnection)
                .prepareStatement("DELETE FROM student_courses where student_id=? and course_id=?");
        CourseDao courseDaoFromMock = new CourseDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> courseDaoFromMock.deleteFromCourse(15, 1))
                .hasMessage("Error of adding/deleting from course: ");
    }

    @Test
    void deleteFromCourseShouldThrowDataBaseSqlRuntimeExceptionIfOneOfStudentIdsOutOfRange() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of adding/deleting from course: ")).when(mockConnection)
                .prepareStatement("DELETE FROM student_courses where student_id=? and course_id=?");
        CourseDao courseDaoFromMock = new CourseDaoImpl(mockConnectorDB);
        Set<Integer> studentIds = new HashSet<>();
        studentIds.add(1);
        studentIds.add(15);

        assertThatThrownBy(() -> courseDaoFromMock.deleteFromCourse(studentIds, 1))
                .hasMessage("Error of adding/deleting from course: ");
    }

    @Test
    void deleteFromCourseShouldThrowDataBaseSqlRuntimeExceptionIfCourseIdOutOfRange() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of adding/deleting from course: ")).when(mockConnection)
                .prepareStatement("DELETE FROM student_courses where student_id=? and course_id=?");
        CourseDao courseDaoFromMock = new CourseDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> courseDaoFromMock.deleteFromCourse(1, 15))
                .hasMessage("Error of adding/deleting from course: ");
    }

}
