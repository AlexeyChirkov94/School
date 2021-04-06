package ua.com.foxminded.school.factories;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.school.entity.CourseEntity;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class CourseFactoryImplTest {

    private static final CourseFactoryImpl COURSE_FACTORY = new CourseFactoryImpl();

    @Test
    void generateCoursesShouldReturnListOfCoursesNoArguments() {
        List<CourseEntity> expected = Arrays.asList(
                CourseEntity.builder()
                        .withCourseName("Mathematics")
                        .withCourseDescription("Queen of the science")
                        .build(),
                CourseEntity.builder()
                        .withCourseName("Algebra")
                        .withCourseDescription("One of the broad parts of mathematics")
                        .build(),
                CourseEntity.builder()
                        .withCourseName("Geometry")
                        .withCourseDescription("One of the oldest branches of mathematics")
                        .build(),
                CourseEntity.builder()
                        .withCourseName("Economics")
                        .withCourseDescription("Social science that studies how people interact with value")
                        .build(),
                CourseEntity.builder()
                        .withCourseName("History")
                        .withCourseDescription("The study of the past")
                        .build(),
                CourseEntity.builder()
                        .withCourseName("Biology")
                        .withCourseDescription("The natural science that studies life and living organisms")
                        .build(),
                CourseEntity.builder()
                        .withCourseName("Chemistry")
                        .withCourseDescription("The scientific discipline involved with elements and compounds")
                        .build(),
                CourseEntity.builder()
                        .withCourseName("Physics")
                        .withCourseDescription("Knowledge of nature")
                        .build(),
                CourseEntity.builder()
                        .withCourseName("Foreign Languages")
                        .withCourseDescription("Language not commonly spoken in the country of the speaker")
                        .build(),
                CourseEntity.builder()
                        .withCourseName("Trigonometry")
                        .withCourseDescription("Branch of mathematics that studies relationships between side lengths and angles of triangles")
                        .build()
        );

        List<CourseEntity> actual = COURSE_FACTORY.generateCourses();

        assertThat(actual).isEqualTo(expected);
    }

}
