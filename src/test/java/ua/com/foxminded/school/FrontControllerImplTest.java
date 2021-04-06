package ua.com.foxminded.school;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.StudentDao;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.school.entity.CourseEntity;
import ua.com.foxminded.school.entity.GroupEntity;
import ua.com.foxminded.school.entity.StudentEntity;
import ua.com.foxminded.school.providers.ViewProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
public class FrontControllerImplTest {

    private static final String START_MENU = "\nPress 0 to exit; \n" +
            "Press 1 to find student by firstName; \n" +
            "Press 2 find all students; \n" +
            "Press 3 find all courses; \n" +
            "Press 4 find all groups; \n" +
            "Press 5 to add new student; \n" +
            "Press 6 to add student to group; \n" +
            "Press 7 to add student to course; \n" +
            "Press 8 to delete student; \n" +
            "Press 9 to remove student from group; \n" +
            "Press 10 to remove student from course; \n" +
            "Press 11 to find groups with less/equal count of students; \n" +
            "Press 12 to find all student related to course; \n";

    private static final List<StudentEntity> STUDENTS = Arrays.asList(StudentEntity.builder().withFirstName("Alex").withLastName("Chirkov").build(),
            StudentEntity.builder().withFirstName("Alex").withLastName("Ivanov").build());

    private static final List<CourseEntity> COURSES = Arrays.asList( CourseEntity.builder().withCourseName("Math").build(),
            CourseEntity.builder().withCourseName("History").build());

    private static final List<GroupEntity> GROUPS = Arrays.asList( GroupEntity.builder().withGroupName("aa-00").build(),
            GroupEntity.builder().withGroupName("ab-01").build());

    private static final int ITEMS_PER_PAGE = 3;

    @Mock
    private StudentDao studentDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private CourseDao courseDao;

    @Mock
    private ViewProvider viewProvider;

    @InjectMocks
    private FrontControllerImpl frontController;


    @Test
    void findByNameFromStartMenuShouldFindStudentByNameFromStartMenuNoArguments() {
        String name = "Alex";

        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(1, 0);
        doNothing().when(viewProvider).printMessage("Input name");
        when(viewProvider.read()).thenReturn(name);
        doNothing().when(viewProvider).printMessage("Search result for name: " + name + ":");
        when(studentDao.findByFirstName(name)).thenReturn(STUDENTS);
        doNothing().when(viewProvider).printMessage(STUDENTS.get(0).toString());
        doNothing().when(viewProvider).printMessage(STUDENTS.get(1).toString());

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).printMessage("Input name");
        verify(viewProvider).read();
        verify(viewProvider).printMessage("Search result for name: " + name + ":");
        verify(studentDao).findByFirstName(name);
        verify(viewProvider).printMessage(STUDENTS.get(0).toString());
        verify(viewProvider).printMessage(STUDENTS.get(1).toString());
    }

    @Test
    void findAllStudentsFromStartMenuShouldFindAllStudentsFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(2,1, 0);
        doNothing().when(viewProvider).printMessage("Input number of page");
        when(studentDao.findAll(1, ITEMS_PER_PAGE)).thenReturn(STUDENTS);
        doNothing().when(viewProvider).printMessage(STUDENTS.get(0).toString());
        doNothing().when(viewProvider).printMessage(STUDENTS.get(1).toString());

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(3)).readInt();
        verify(viewProvider).printMessage("Input number of page");
        verify(studentDao).findAll(1, ITEMS_PER_PAGE);
        verify(viewProvider).printMessage(STUDENTS.get(0).toString());
        verify(viewProvider).printMessage(STUDENTS.get(1).toString());
    }

    @Test
    void findAllCoursesFromStartMenuShouldFindAllCoursesFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(3, 1, 0);
        doNothing().when(viewProvider).printMessage("Input number of page");
        when(courseDao.findAll(1, ITEMS_PER_PAGE)).thenReturn(COURSES);
        doNothing().when(viewProvider).printMessage(COURSES.get(0).toString());
        doNothing().when(viewProvider).printMessage(COURSES.get(1).toString());

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(3)).readInt();
        verify(viewProvider).printMessage("Input number of page");
        verify(courseDao).findAll(1, ITEMS_PER_PAGE);
        verify(viewProvider).printMessage(COURSES.get(0).toString());
        verify(viewProvider).printMessage(COURSES.get(1).toString());
    }

    @Test
    void findAllGroupsFromStartMenuShouldFindAllGroupsFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(4, 1, 0);
        doNothing().when(viewProvider).printMessage("Input number of page");
        when(groupDao.findAll(1, ITEMS_PER_PAGE)).thenReturn(GROUPS);
        doNothing().when(viewProvider).printMessage(GROUPS.get(0).toString());
        doNothing().when(viewProvider).printMessage(GROUPS.get(1).toString());

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(3)).readInt();
        verify(viewProvider).printMessage("Input number of page");
        verify(groupDao).findAll(1, ITEMS_PER_PAGE);
        verify(viewProvider).printMessage(GROUPS.get(0).toString());
        verify(viewProvider).printMessage(GROUPS.get(1).toString());
    }

    @Test
    void addNewStudentFromStartMenuShouldAddNewStudentFromStartMenuNoArguments() {
        StudentEntity student = StudentEntity.builder().withFirstName("Alex").withLastName("Chirkov").build();

        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(5, 0);
        doNothing().when(viewProvider).printMessage("Enter the first name of student");
        when(viewProvider.read()).thenReturn(student.getFirstName(), student.getLastName());
        doNothing().when(viewProvider).printMessage("Enter the last name of student");
        doNothing().when(studentDao).save(student);

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider, times(2)).read();
        verify(viewProvider).printMessage("Enter the first name of student");
        verify(viewProvider).printMessage("Enter the last name of student");
        verify(studentDao).save(student);
    }

    @Test
    void addStudentToGroupFromStartMenuShouldAddStudentToGroupFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(6, 1, 1, 0);
        doNothing().when(viewProvider).printMessage("Enter Id of the student");
        doNothing().when(viewProvider).printMessage("Enter Id of the group");
        doNothing().when(groupDao).addStudentToGroup(1, 1);

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(4)).readInt();
        verify(viewProvider).printMessage("Enter Id of the student");
        verify(viewProvider).printMessage("Enter Id of the group");
        verify(groupDao).addStudentToGroup(1, 1);
    }

    @Test
    void addStudentsToCourseFromStartMenuShouldAddStudentsToCourseFromStartMenuNoArguments() {
        int idFirstStudent = 1;
        int idSecondStudent = 2;
        int idOfCourse = 1;
        int intToExitFromMenu = 0;
        int intToContinueAddStudents = 1;

        Set<Integer> ids = new HashSet<>();
        ids.add(idFirstStudent);
        ids.add(idSecondStudent);


        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(7, idFirstStudent, intToContinueAddStudents,
                idSecondStudent, intToExitFromMenu, idOfCourse, intToExitFromMenu);
        doNothing().when(viewProvider).printMessage("Enter Id of the student");
        doNothing().when(viewProvider).printMessage("To add student press 1; \n "  +
                                    "To end adding of student press 0");
        doNothing().when(viewProvider).printMessage("Enter Id of the course");
        doNothing().when(courseDao).addToCourse(ids, idOfCourse);

        frontController.startMenu(ITEMS_PER_PAGE);


        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(7)).readInt();
        verify(viewProvider, times(2)).printMessage("Enter Id of the student");
        verify(viewProvider, times(2)).printMessage("To add student press 1; \n "  +
                "To end adding of student press 0");
        verify(viewProvider).printMessage("Enter Id of the course");
        verify(courseDao).addToCourse(ids, idOfCourse);
    }

    @Test
    void deleteStudentToGroupFromStartMenuShouldDeleteStudentToGroupFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(8, 1, 0);
        doNothing().when(viewProvider).printMessage("Enter Id of the student");
        doNothing().when(studentDao).deleteById(1);

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(3)).readInt();
        verify(viewProvider).printMessage("Enter Id of the student");
        verify(studentDao).deleteById(1);
    }

    @Test
    void removeStudentFromGroupFromStartMenuShouldRemoveStudentFromGroupFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(9, 1, 0);
        doNothing().when(viewProvider).printMessage("Enter Id of the student");
        doNothing().when(groupDao).deleteStudentFromGroup(1);

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(3)).readInt();
        verify(viewProvider).printMessage("Enter Id of the student");
        verify(groupDao).deleteStudentFromGroup(1);
    }

    @Test
    void removeStudentFromCourseFromStartMenuShouldRemoveStudentFromCourseFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(10, 1, 1, 0);
        doNothing().when(viewProvider).printMessage("Enter Id of the student");
        doNothing().when(viewProvider).printMessage("Enter Id of the course");
        doNothing().when(courseDao).deleteFromCourse(1, 1);

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(4)).readInt();
        verify(viewProvider).printMessage("Enter Id of the student");
        verify(viewProvider).printMessage("Enter Id of the course");
        verify(courseDao).deleteFromCourse(1, 1);
    }

    @Test
    void findGroupsWithLessOrEqualCountOfStudentFromStartMenuShouldDoSameNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(11, 5, 0);
        doNothing().when(viewProvider).printMessage("Enter count of student");
        when(groupDao.findGroupWithEqualOrLessCountOfStudent(5)).thenReturn(GROUPS);
        doNothing().when(viewProvider).printMessage(GROUPS.get(0).toString());
        doNothing().when(viewProvider).printMessage(GROUPS.get(1).toString());

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(3)).readInt();
        verify(viewProvider).printMessage("Enter count of student");
        verify(groupDao).findGroupWithEqualOrLessCountOfStudent(5);
        verify(viewProvider).printMessage(GROUPS.get(0).toString());
        verify(viewProvider).printMessage(GROUPS.get(1).toString());
    }

    @Test
    void findAllStudentRelatedToCourseFromStartMenuShouldDoSameNoArguments() { ;
        String courseName = "Math";

        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(12,  0);
        doNothing().when(viewProvider).printMessage("Enter the course name");
        when(viewProvider.read()).thenReturn(courseName);
        when(studentDao.findByCourseName(courseName)).thenReturn(STUDENTS);
        doNothing().when(viewProvider).printMessage(STUDENTS.get(0).toString());
        doNothing().when(viewProvider).printMessage(STUDENTS.get(1).toString());

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).read();
        verify(viewProvider).printMessage("Enter the course name");
        verify(studentDao).findByCourseName(courseName);
        verify(viewProvider).printMessage(STUDENTS.get(0).toString());
        verify(viewProvider).printMessage(STUDENTS.get(1).toString());
    }

    @Test
    void incorrectInputValueFromStartMenuShouldWriteAboutIncorrectInputValueAboutNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(20, 0);
        doNothing().when(viewProvider).printMessage("Incorrect number, try again");

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).printMessage("Incorrect number, try again");
    }

}
