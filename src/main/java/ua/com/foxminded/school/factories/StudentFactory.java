package ua.com.foxminded.school.factories;

import ua.com.foxminded.school.entity.StudentEntity;
import java.util.List;

public interface StudentFactory {

    List<StudentEntity> generateStudents(int count);

}
