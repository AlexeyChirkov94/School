package ua.com.foxminded.school.assignmentation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.entity.CourseEntity;
import ua.com.foxminded.school.entity.GroupEntity;
import ua.com.foxminded.school.entity.StudentEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class SchoolAssignationImplTest {

    private static final Random RANDOM = new Random();

    private static final List<StudentEntity> STUDENTS = Arrays.asList(
            StudentEntity.builder().withId(1).withFirstName("Alexandr").withLastName("Smetanko").build(),
            StudentEntity.builder().withId(2).withFirstName("Dmitriy").withLastName("Shushura").build(),
            StudentEntity.builder().withId(3).withFirstName("Igor").withLastName("Voronova").build(),
            StudentEntity.builder().withId(4).withFirstName("Elizaveta").withLastName("Melnikov").build(),
            StudentEntity.builder().withId(5).withFirstName("Dmitriy").withLastName("Sizov").build(),
            StudentEntity.builder().withId(6).withFirstName("Kira").withLastName("Chirkov").build(),
            StudentEntity.builder().withId(7).withFirstName("Nikita").withLastName("Makarova").build(),
            StudentEntity.builder().withId(8).withFirstName("Pavel").withLastName("Levichev").build(),
            StudentEntity.builder().withId(9).withFirstName("Vladislav").withLastName("Georgiv").build(),
            StudentEntity.builder().withId(10).withFirstName("Alyona").withLastName("Ivanov").build()
    );
    private static final List<StudentEntity> REDUNDANT_STUDENTS = Arrays.asList(
            StudentEntity.builder().withId(11).withFirstName("Alexandr").withLastName("Smetanko").build(),
            StudentEntity.builder().withId(12).withFirstName("Dmitriy").withLastName("Shushura").build()
    );
    private static final List<StudentEntity> REDUNDANT_STUDENTS_1 = Arrays.asList(
            StudentEntity.builder().withId(13).withFirstName("Alexandr").withLastName("Smetanko").build(),
            StudentEntity.builder().withId(14).withFirstName("Dmitriy").withLastName("Shushura").build()
    );
    private static final List<CourseEntity> COURSES = Arrays.asList(
            CourseEntity.builder().withId(1).withCourseName("Math").build(),
            CourseEntity.builder().withId(2).withCourseName("Biology").build(),
            CourseEntity.builder().withId(3).withCourseName("History").build()
    );
    private static final List<GroupEntity> GROUPS = Arrays.asList(
            GroupEntity.builder().withId(1).withGroupName("aa-00").build(),
            GroupEntity.builder().withId(2).withGroupName("aa-01").build(),
            GroupEntity.builder().withId(3).withGroupName("ab-02").build()
    );

    @Mock
    private StudentDao studentDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private Random random;

    @Mock
    private CourseDao courseDao;

    @InjectMocks
    private SchoolAssignationImpl schoolAssignation;

    @Test
    void studentsToGroupsShouldFindAssignateGroupsToStudentsNoArguments(){
        when(studentDao.findAll()).thenReturn(STUDENTS);
        when(groupDao.findAll()).thenReturn(GROUPS);
        when(random.nextInt(3)).thenReturn(1);
        doNothing().when(groupDao).addStudentToGroup(anyInt(), anyInt());
        when(groupDao.countMembersByGroupId(anyInt())).thenReturn(1, 3, 3, 30, 3, 3);
        when(studentDao.findByGroupId(anyInt())).thenReturn(REDUNDANT_STUDENTS, REDUNDANT_STUDENTS_1);
        doNothing().when(groupDao).deleteStudentFromGroup(11);
        doNothing().when(groupDao).deleteStudentFromGroup(12);
        doNothing().when(groupDao).deleteStudentFromGroup(13);
        doNothing().when(groupDao).deleteStudentFromGroup(14);

        schoolAssignation.studentsToGroups(5,2);

        verify(studentDao).findAll();
        verify(groupDao).findAll();
        verify(random, times(10)).nextInt(3);
        verify(groupDao, times(10)).addStudentToGroup(anyInt(), anyInt());
        verify(groupDao, times(6)).countMembersByGroupId(anyInt());
        verify(studentDao, times(2)).findByGroupId(anyInt());
        verify(groupDao).deleteStudentFromGroup(11);
        verify(groupDao).deleteStudentFromGroup(12);
        verify(groupDao).deleteStudentFromGroup(13);
        verify(groupDao).deleteStudentFromGroup(14);
    }

    @Test
    void studentsToCoursesShouldFindAssignateCoursesToStudentsNoArguments(){
        when(studentDao.findAll()).thenReturn(STUDENTS);
        when(courseDao.findAll()).thenReturn(COURSES);
        when(random.nextInt(3)).thenReturn(1);
        doNothing().when(courseDao).addToCourse(anyInt(), anyInt());

        schoolAssignation.studentsToCourses(3,1);

        verify(studentDao).findAll();
        verify(courseDao).findAll();
        verify(random, times(30)).nextInt(3);
        verify(courseDao, times(20)).addToCourse(anyInt(), anyInt());
    }
}
