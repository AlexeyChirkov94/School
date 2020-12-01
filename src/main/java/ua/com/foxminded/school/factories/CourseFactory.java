package ua.com.foxminded.school.factories;

import ua.com.foxminded.school.entity.CourseEntity;
import java.util.List;

public interface CourseFactory {

    List<CourseEntity> generateCourses();

}
