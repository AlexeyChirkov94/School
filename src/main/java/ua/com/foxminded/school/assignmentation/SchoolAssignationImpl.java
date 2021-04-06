package ua.com.foxminded.school.assignmentation;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import ua.com.foxminded.school.dao.CourseDao;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.entity.CourseEntity;
import ua.com.foxminded.school.entity.Entity;
import ua.com.foxminded.school.entity.GroupEntity;
import ua.com.foxminded.school.entity.StudentEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SchoolAssignationImpl implements SchoolAssignation {

    private StudentDao studentDao;
    private GroupDao groupDao;
    private CourseDao courseDao;
    private Random random;

    @Inject
    public SchoolAssignationImpl(StudentDao studentDao, GroupDao groupDao, CourseDao courseDao,
                                 @Named("Random") Random random) {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        this.random = random;
    }

    @Override
    public void studentsToGroups(int maxSizeOfGroup, int minSizeOfGroup) {
        List<StudentEntity> students = studentDao.findAll();
        List<GroupEntity> groups = groupDao.findAll();
        List<Integer> studentGroupsIds = getIds(groups);

        for(StudentEntity student : students){
            groupDao.addStudentToGroup(studentGroupsIds.get(random.nextInt(studentGroupsIds.size())),student.getId());
        }

        List<StudentEntity> studentInUnCorrectGroup = new ArrayList<>();
        for(GroupEntity group : groups){
            boolean isGroupMoreMinimum= groupDao.countMembersByGroupId(group.getId()) > minSizeOfGroup;
            boolean isGroupLessMaximum = groupDao.countMembersByGroupId(group.getId()) < maxSizeOfGroup;
            if(!(isGroupMoreMinimum && isGroupLessMaximum)){
                studentInUnCorrectGroup.addAll(studentDao.findByGroupId(group.getId()));
            }
        }

        for(StudentEntity student : studentInUnCorrectGroup){
            groupDao.deleteStudentFromGroup(student.getId());
        }
    }

    @Override
    public void studentsToCourses(int maxCountOfCourseForStudent, int minCountOfCourseForStudent) {
        List<StudentEntity> students = studentDao.findAll();
        List<CourseEntity> courses = courseDao.findAll();
        int countOfCourses = courses.size();

        for(StudentEntity student : students) {
            int countOfCoursesOfCurrentStudent = random.nextInt(maxCountOfCourseForStudent) + minCountOfCourseForStudent;
            for(int i = 1; i <= countOfCoursesOfCurrentStudent; i++ ){
                courseDao.addToCourse(student.getId(), random.nextInt(countOfCourses) + 1);
            }
        }
    }

    private <E extends Entity> List<Integer>  getIds (List<E> entities){
        return entities.stream().map(e -> e.getId()).collect(Collectors.toList());
    }

}
