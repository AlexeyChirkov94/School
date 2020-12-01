package ua.com.foxminded.school.utility;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static java.util.Collections.emptyList;
import static ua.com.foxminded.school.utility.CollectionUtility.nullSafeListInitialize;

public class CollectionUtilityTest {

    @Test
    void nullSafeListInitializeShouldReturnEmptyListIfArgumentIsNull(){
        List<Integer> expected = emptyList();
        List<Integer> actual = nullSafeListInitialize(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void nullSafeListInitializeShouldReturnInputListIfArgumentIsNotNull(){
        List<Integer> expected = Arrays.asList(1, 2, 3);
        List<Integer> actual = nullSafeListInitialize(expected);

        assertThat(actual).isEqualTo(expected);
    }

}
