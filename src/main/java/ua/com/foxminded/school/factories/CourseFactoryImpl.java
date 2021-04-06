package ua.com.foxminded.school.factories;

import com.google.inject.Inject;
import ua.com.foxminded.school.entity.CourseEntity;
import java.util.Arrays;
import java.util.List;

public class CourseFactoryImpl implements CourseFactory {

    @Inject
    public CourseFactoryImpl() {
    }

    private static final List<CourseEntity> COURSES = Arrays.asList(
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

    @Override
    public List<CourseEntity> generateCourses(){
        return COURSES;
    }

}
