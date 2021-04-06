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
import ua.com.foxminded.school.entity.StudentEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static ua.com.foxminded.school.utility.TestUtility.compareStudents;

@ExtendWith( MockitoExtension.class)
class StudentDaoImplTest {

    private static final Logger LOGGER = Logger.getLogger(StudentDaoImplTest.class);

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
    void createAndReadShouldAddNewStudentToDatabaseIfArgumentIsStudentEntityAndReadIt(){
        StudentEntity addingStudent = StudentEntity.builder().withFirstName("Grigoriy").withLastName("Sibircev")
                .withGroup(2).build();
        studentDao.save(addingStudent);
        StudentEntity readingStudent = studentDao.findById(12).get();

        compareStudents(readingStudent, addingStudent);
    }

    @Test
    void updateShouldUpdateDataOfStudentIfArgumentIsStudentEntity(){
        StudentEntity expected = StudentEntity.builder().withId(3).withFirstName("Olga").withLastName("Azimova").withGroup(1).build();
        studentDao.update(expected);
        StudentEntity actual = studentDao.findById(3).get();

        compareStudents(expected, actual);
    }

    @Test
    void deleteShouldDeleteDataOfStudentIfArgumentIsIsIdOfStudent(){
        Optional<StudentEntity> expected = Optional.empty();
        studentDao.deleteById(3);
        Optional<StudentEntity> actual = studentDao.findById(3);

        assertThat(expected).isEqualTo(actual);
    }



    @Test
    void findByFirstNameShouldReturnStudentEntityIfArgumentIsStudentName(){
        StudentEntity actual = studentDao.findByFirstName("Alexey").get(0);
        StudentEntity expected = StudentEntity.builder().withFirstName("Alexey").withLastName("Chirkov")
                .withGroup(1).build();

        compareStudents(actual, expected);
    }

    @Test
    void findByLastNameShouldReturnStudentEntityIfArgumentIsStudentLastName(){
        StudentEntity actual = studentDao.findByLastName("Ivanov").get(0);
        StudentEntity expected = StudentEntity.builder().withFirstName("Petr").withLastName("Ivanov")
                .withGroup(1).build();

        compareStudents(actual, expected);
    }

    @Test
    void findByGroupIdShouldReturnListOfStudentEntityIfArgumentIsGroupId(){
        List<StudentEntity> actual = studentDao.findByGroupId(2);
        List<StudentEntity> expected= new ArrayList<>();
        expected.add(StudentEntity.builder().withFirstName("Alexandr").withLastName("Semenov").withGroup(2).build());
        expected.add(StudentEntity.builder().withFirstName("Alyona").withLastName("Osipova").withGroup(2).build());

        compareStudents(actual, expected);
    }

    @Test
    void findByCourseNameShouldReturnListOfStudentEntityIfArgumentIsNameOfCourse(){
        Set<Integer> idsStudentsLearningMath = new HashSet<>();
        idsStudentsLearningMath.add(9);
        idsStudentsLearningMath.add(10);
        courseDao.addToCourse(idsStudentsLearningMath, 1);

        List<StudentEntity> actual = studentDao.findByCourseName("Mathematics");
        List<StudentEntity> expected = new ArrayList<>();
        expected.add(StudentEntity.builder().withFirstName("Mihail").withLastName("Zamyatin")
                .withCourses(Arrays.asList(courseDao.findById(1).get())).withGroup(3).build());
        expected.add(StudentEntity.builder().withFirstName("Nikita").withLastName("Melnikov")
                .withCourses(Arrays.asList(courseDao.findById(1).get())).withGroup(3).build());

        compareStudents(actual, expected);
    }

    @Test
    void findByStringParamShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of finding: ")).when(mockConnection)
                .prepareStatement("SELECT * FROM students WHERE first_name=?");

        StudentDao studentDaoFromMock = new StudentDaoImpl(mockConnectorDB);


        assertThatThrownBy(() -> studentDaoFromMock.findByFirstName("s"))
                .hasMessage("Error of finding: ");
    }

}
