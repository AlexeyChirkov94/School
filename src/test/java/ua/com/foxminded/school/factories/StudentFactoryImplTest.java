package ua.com.foxminded.school.factories;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.assertj.core.api.Assertions.assertThat;

class StudentFactoryImplTest {

    private static final Random RANDOM = new Random();
    private static final StudentFactoryImpl STUDENT_FACTORY = new StudentFactoryImpl(RANDOM);

    @Test
    void generateStudentsShouldReturnListOfStudentsIfArgumentIsCountOfStudent() {
        int expected = 5;
        int actual  = STUDENT_FACTORY.generateStudents(5).size();

        assertThat(actual).isEqualTo(expected);
    }

}
