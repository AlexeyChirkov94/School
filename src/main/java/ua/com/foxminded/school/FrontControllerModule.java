package ua.com.foxminded.school;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ua.com.foxminded.school.assignmentation.SchoolAssignation;
import ua.com.foxminded.school.assignmentation.SchoolAssignationImpl;
import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.dao.impl.CourseDaoImpl;
import ua.com.foxminded.school.dao.impl.GroupDaoImpl;
import ua.com.foxminded.school.dao.impl.StudentDaoImpl;
import ua.com.foxminded.school.dao.tables.ScriptsRunner;
import ua.com.foxminded.school.dao.tables.ScriptsRunnerImpl;
import ua.com.foxminded.school.factories.CourseFactory;
import ua.com.foxminded.school.factories.CourseFactoryImpl;
import ua.com.foxminded.school.factories.GroupFactory;
import ua.com.foxminded.school.factories.GroupFactoryImpl;
import ua.com.foxminded.school.factories.StudentFactory;
import ua.com.foxminded.school.factories.StudentFactoryImpl;
import ua.com.foxminded.school.providers.ViewProvider;
import ua.com.foxminded.school.providers.ViewProviderImpl;

import java.util.Random;

public class FrontControllerModule extends AbstractModule {

    @Override
    protected void configure(){

        bind(String.class)
                .annotatedWith(Names.named("PathToDatabaseProperty"))
                .toInstance("property/database/school");
        bind(Random.class)
                .annotatedWith(Names.named("Random"))
                .toInstance(new Random());

        bind(ScriptsRunner.class).to(ScriptsRunnerImpl.class);

        bind(StudentDao.class).to(StudentDaoImpl.class);
        bind(GroupDao.class).to(GroupDaoImpl.class);
        bind(CourseDao.class).to(CourseDaoImpl.class);
        bind(ViewProvider.class).to(ViewProviderImpl.class);
        bind(StudentFactory.class).to(StudentFactoryImpl.class);
        bind(GroupFactory.class).to(GroupFactoryImpl.class);
        bind(CourseFactory.class).to(CourseFactoryImpl.class);
        bind(SchoolAssignation.class).to(SchoolAssignationImpl.class);

    }
}
