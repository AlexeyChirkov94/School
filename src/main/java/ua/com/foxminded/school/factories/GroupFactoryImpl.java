package ua.com.foxminded.school.factories;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import ua.com.foxminded.school.entity.GroupEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupFactoryImpl implements GroupFactory{

    private static final int COUNT_OF_LETTER_IN_ALPHABET = 26;
    private static final int INDENT_TO_FIRST_CHAR_OF_ALPHABET = 97;
    private static final int COUNT_OF_DIGITS = 10;
    private static final char HYPHEN = '-';
    private Random random;

    @Inject
    public GroupFactoryImpl(@Named("Random") Random random) {
        this.random = random;
    }

    @Override
    public List<GroupEntity> generateGroups(int count){
        List<GroupEntity> groups = new ArrayList<>();
        int alreadyGeneratedGroups = 0;

        while (alreadyGeneratedGroups < count){
            StringBuilder groupName = new StringBuilder();

            groupName.append(randomLetterGenerator(random))
                     .append(randomLetterGenerator(random))
                     .append(HYPHEN)
                     .append(random.nextInt(COUNT_OF_DIGITS))
                     .append(random.nextInt(COUNT_OF_DIGITS));

            groups.add(
                    GroupEntity.builder()
                            .withGroupName(groupName.toString())
                            .build()
            );
            alreadyGeneratedGroups++;
        }

        return groups;
    }

    private char randomLetterGenerator(Random random){
        return (char)(random.nextInt(COUNT_OF_LETTER_IN_ALPHABET)+INDENT_TO_FIRST_CHAR_OF_ALPHABET);
    }


}
