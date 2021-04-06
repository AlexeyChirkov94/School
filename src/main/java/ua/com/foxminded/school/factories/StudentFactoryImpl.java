package ua.com.foxminded.school.factories;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import ua.com.foxminded.school.entity.StudentEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StudentFactoryImpl implements StudentFactory{

    private static final List<String> FIRST_NAMES = Arrays.asList("Alexey","Dmitriy","Alexandr",
            "Igor","Pert","Vladislav","Vsevolod","Olga","Anastasia","Daria","Maria",
            "Alyona","Kira","Roman","Oleg","Georgiy","Elizaveta","Nikita","Pavel","Vasiliy");

    private static final List<String> LAST_NAMES = Arrays.asList("Chikrov","Martinenko","Semenov",
            "Voinov","Andreev","Sizov","Petrishev","Ivanova","Ivanova","Shushura","Makarova",
            "Osokina","Voronova","Grigorev","Smetanko","Georgiv","Zarkova","Melnikov","Onohov","Levichev");

    private Random random;

    @Inject
    public StudentFactoryImpl(@Named("Random") Random random) {
        this.random = random;
    }

    @Override
    public List<StudentEntity> generateStudents(int count){
        List<StudentEntity> students = new ArrayList<>();
        int alreadyGeneratedStudents = 0;

        while (alreadyGeneratedStudents < count){
            students.add(
                    StudentEntity.builder()
                                 .withFirstName(FIRST_NAMES.get(random.nextInt(20)))
                                 .withLastName(LAST_NAMES.get(random.nextInt(20)))
                                 .build()
            );
            alreadyGeneratedStudents++;
        }

        return students;
    }

}
