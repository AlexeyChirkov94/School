package ua.com.foxminded.school;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ua.com.foxminded.school.assignmentation.SchoolAssignation;
import ua.com.foxminded.school.assignmentation.SchoolAssignationImpl;
import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.dao.impl.CourseDaoImpl;
import ua.com.foxminded.school.dao.impl.GroupDaoImpl;
import ua.com.foxminded.school.dao.impl.StudentDaoImpl;
import ua.com.foxminded.school.dao.tables.ScriptsRunner;
import ua.com.foxminded.school.dao.tables.ScriptsRunnerImpl;
import ua.com.foxminded.school.entity.CourseEntity;
import ua.com.foxminded.school.entity.GroupEntity;
import ua.com.foxminded.school.entity.StudentEntity;
import ua.com.foxminded.school.factories.CourseFactory;
import ua.com.foxminded.school.factories.CourseFactoryImpl;
import ua.com.foxminded.school.factories.GroupFactory;
import ua.com.foxminded.school.factories.GroupFactoryImpl;
import ua.com.foxminded.school.factories.StudentFactory;
import ua.com.foxminded.school.factories.StudentFactoryImpl;
import java.io.IOException;
import java.util.List;

public class Application {

    private static final String PATH_TO_CREATE_TABLE_QUERY = "src\\main\\resources\\database\\schema.sql";
    private static final Integer COUNT_OF_STUDENTS = 200;
    private static final Integer COUNT_OF_GROUPS = 10;
    private static final Integer MAX_COUNT_OF_STUDENTS_IN_GROUP = 30;
    private static final Integer MIN_COUNT_OF_STUDENTS_IN_GROUP = 10;
    private static final Integer MAX_COUNT_OF_COURSES_FOR_STUDENT = 3;
    private static final Integer MIN_COUNT_OF_COURSES_FOR_STUDENT = 1;
    private static final Integer ITEMS_PER_PAGE = 10;

    public static void main(String[] args) throws IOException {

        Injector injector = Guice.createInjector(new FrontControllerModule());

        ScriptsRunner scriptsRunner = injector.getInstance(ScriptsRunnerImpl.class);
        StudentDao studentDao = injector.getInstance(StudentDaoImpl.class);
        GroupDao groupDao = injector.getInstance(GroupDaoImpl.class);
        CourseDao courseDao = injector.getInstance(CourseDaoImpl.class);
        StudentFactory studentFactory = injector.getInstance(StudentFactoryImpl.class);
        GroupFactory groupFactory = injector.getInstance(GroupFactoryImpl.class);
        CourseFactory courseFactory = injector.getInstance(CourseFactoryImpl.class);
        SchoolAssignation schoolAssignation = injector.getInstance(SchoolAssignationImpl.class);
        FrontControllerImpl frontController = injector.getInstance(FrontControllerImpl.class);

        scriptsRunner.runScript(PATH_TO_CREATE_TABLE_QUERY);
        List<GroupEntity> groups = groupFactory.generateGroups(COUNT_OF_GROUPS);
        List<CourseEntity> courses = courseFactory.generateCourses();
        List<StudentEntity> students = studentFactory.generateStudents(COUNT_OF_STUDENTS);
        groupDao.saveAll(groups);
        courseDao.saveAll(courses);
        studentDao.saveAll(students);
        schoolAssignation.studentsToGroups(MAX_COUNT_OF_STUDENTS_IN_GROUP, MIN_COUNT_OF_STUDENTS_IN_GROUP);
        schoolAssignation.studentsToCourses(MAX_COUNT_OF_COURSES_FOR_STUDENT, MIN_COUNT_OF_COURSES_FOR_STUDENT);

        frontController.startMenu(ITEMS_PER_PAGE);
    }

}
