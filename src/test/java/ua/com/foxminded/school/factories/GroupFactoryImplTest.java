package ua.com.foxminded.school.factories;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.assertj.core.api.Assertions.assertThat;

class GroupFactoryImplTest {

    private static final Random RANDOM = new Random();
    private static final GroupFactoryImpl GROUP_FACTORY = new GroupFactoryImpl(RANDOM);

    @Test
    void generateGroupsShouldReturnListOfGroupsIfArgumentIsCountOfGroups() {
        int expected = 5;
        int actual  = GROUP_FACTORY.generateGroups(5).size();

        assertThat(actual).isEqualTo(expected);
    }

}
