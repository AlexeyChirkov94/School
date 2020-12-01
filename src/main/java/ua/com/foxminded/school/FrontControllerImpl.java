package ua.com.foxminded.school;

import com.google.inject.Inject;
import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.entity.CourseEntity;
import ua.com.foxminded.school.entity.GroupEntity;
import ua.com.foxminded.school.entity.StudentEntity;
import ua.com.foxminded.school.providers.ViewProvider;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FrontControllerImpl implements FrontController{

    private static final String REQUEST_PAGE_NUMBER = "Input number of page";
    private static final String REQUEST_STUDENT_ID = "Enter Id of the student";

    private final StudentDao studentDao;
    private final GroupDao groupDao;
    private final CourseDao courseDao;
    private final ViewProvider viewProvider;

    @Inject
    public FrontControllerImpl(StudentDao studentDao, GroupDao groupDao, CourseDao courseDao, ViewProvider viewProvider) {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        this.viewProvider = viewProvider;
    }

    @Override
    public void startMenu(int itemsPerPage) {
        boolean cycleBreaker = false;

        while (!cycleBreaker){
            viewProvider.printMessage("\nPress 0 to exit; \n" +
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
                    "Press 12 to find all student related to course; \n"
            );

            int choose = viewProvider.readInt();
            switch (choose) {
                case 0: cycleBreaker = true; break;
                case 1: findStudentByFirstName(); break;
                case 2: findAllStudents(itemsPerPage); break;
                case 3: findAllCourses(itemsPerPage); break;
                case 4: findAllGroups(itemsPerPage); break;
                case 5: addNewStudent(); break;
                case 6: addStudentToGroup(); break;
                case 7: addStudentsToCourse(); break;
                case 8: deleteStudent(); break;
                case 9: removeStudentFromGroup(); break;
                case 10: removeStudentFromCourse(); break;
                case 11: findGroupsWithLessOrEqualCountOfStudent(); break;
                case 12: findAllStudentRelatedToCourse(); break;
                default: viewProvider.printMessage("Incorrect number, try again");
            }
        }
    }

    private void findStudentByFirstName(){
        viewProvider.printMessage("Input name");
        String name = viewProvider.read();
        viewProvider.printMessage("Search result for name: " + name + ":");
        List<StudentEntity> students = studentDao.findByFirstName(name);
        for(StudentEntity student : students){
            viewProvider.printMessage(student.toString());
        }
    }

    private void findAllStudents(int itemsPerPage){
        viewProvider.printMessage(REQUEST_PAGE_NUMBER);
        int numberOfPage = viewProvider.readInt();
        List<StudentEntity> students = studentDao.findAll(numberOfPage, itemsPerPage);
        for(StudentEntity student : students){
            viewProvider.printMessage(student.toString());
        }
    }

    private void findAllCourses(int itemsPerPage){
        viewProvider.printMessage(REQUEST_PAGE_NUMBER);
        int numberOfPage = viewProvider.readInt();
        List<CourseEntity> courses = courseDao.findAll(numberOfPage, itemsPerPage);
        for(CourseEntity course  : courses){
            viewProvider.printMessage(course.toString());
        }
    }

    private void findAllGroups(int itemsPerPage){
        viewProvider.printMessage(REQUEST_PAGE_NUMBER);
        int numberOfPage = viewProvider.readInt();
        List<GroupEntity> groups = groupDao.findAll(numberOfPage, itemsPerPage);
        for(GroupEntity group  : groups){
            viewProvider.printMessage(group.toString());
        }
    }

    private void addNewStudent(){
        viewProvider.printMessage("Enter the first name of student");
        String firstName = viewProvider.read();
        viewProvider.printMessage("Enter the last name of student");
        String lastName = viewProvider.read();
        studentDao.save(StudentEntity.builder().withFirstName(firstName).withLastName(lastName).build());
    }

    private void addStudentToGroup(){
        viewProvider.printMessage(REQUEST_STUDENT_ID);
        int studentId = viewProvider.readInt();
        viewProvider.printMessage("Enter Id of the group");
        int groupId = viewProvider.readInt();
        groupDao.addStudentToGroup(groupId, studentId);
    }

    private void addStudentsToCourse(){
        boolean cycleBreaker = false;
        Set<Integer> ids = new HashSet<>();

        while (!cycleBreaker){
            viewProvider.printMessage(REQUEST_STUDENT_ID);
            int studentId = viewProvider.readInt();
            ids.add(studentId);
            viewProvider.printMessage("To add student press 1; \n " +
                    "To end adding of student press 0");
            int choose = viewProvider.readInt();

            if (choose == 0){
                cycleBreaker = true;
            }
        }
        viewProvider.printMessage("Enter Id of the course");
        int courseId = viewProvider.readInt();

        courseDao.addToCourse(ids, courseId);
    }

    private void deleteStudent(){
        viewProvider.printMessage(REQUEST_STUDENT_ID);
        int studentId = viewProvider.readInt();
        studentDao.deleteById(studentId);
    }

    private void removeStudentFromGroup(){
        viewProvider.printMessage(REQUEST_STUDENT_ID);
        int studentId = viewProvider.readInt();
        groupDao.deleteStudentFromGroup(studentId);
    }

    private void removeStudentFromCourse(){
        viewProvider.printMessage(REQUEST_STUDENT_ID);
        int studentId = viewProvider.readInt();
        viewProvider.printMessage("Enter Id of the course");
        int courseId = viewProvider.readInt();
        courseDao.deleteFromCourse(studentId, courseId);
    }

    private void findGroupsWithLessOrEqualCountOfStudent(){
        viewProvider.printMessage("Enter count of student");
        int count = viewProvider.readInt();
        List<GroupEntity> groups = groupDao.findGroupWithEqualOrLessCountOfStudent(count);
        for(GroupEntity group  : groups){
            viewProvider.printMessage(group.toString());
        }
    }

    private void findAllStudentRelatedToCourse(){
        viewProvider.printMessage("Enter the course name");
        String courseName = viewProvider.read();
        List<StudentEntity> students = studentDao.findByCourseName(courseName);
        for(StudentEntity student : students){
            viewProvider.printMessage(student.toString());
        }
    }

}
