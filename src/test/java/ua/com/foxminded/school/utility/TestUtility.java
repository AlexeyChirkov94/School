package ua.com.foxminded.school.utility;

import ua.com.foxminded.school.entity.CourseEntity;
import ua.com.foxminded.school.entity.GroupEntity;
import ua.com.foxminded.school.entity.StudentEntity;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class TestUtility {

    public static void compareStudents(StudentEntity actual, StudentEntity expected) {
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getGroupId()).isEqualTo(expected.getGroupId());
        assertThat(actual.getCourseEntities()).isEqualTo(expected.getCourseEntities());
    }

    public static void compareStudents(List<StudentEntity> actual, List<StudentEntity> expected){
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            compareStudents(actual.get(i), expected.get(i));
        }
    }

    public static void compareCourses(CourseEntity actual, CourseEntity expected) {
        assertThat(actual.getCourseName()).isEqualTo(expected.getCourseName());
        assertThat(actual.getCourseDescription()).isEqualTo(expected.getCourseDescription());
    }

    public static void compareCourses(List<CourseEntity> actual, List<CourseEntity> expected){
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            compareCourses(actual.get(i), expected.get(i));
        }
    }

    public static void compareGroups(GroupEntity actual, GroupEntity expected) {
        assertThat(actual.getGroupName()).isEqualTo(expected.getGroupName());
    }

    public static void compareGroups(List<GroupEntity> actual, List<GroupEntity> expected){
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            compareGroups(actual.get(i), expected.get(i));
        }
    }

}
