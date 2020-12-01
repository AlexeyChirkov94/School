package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.entity.CourseEntity;
import java.util.List;
import java.util.Set;

public interface CourseDao extends CrudPageableDao<CourseEntity>{

    void addToCourse(Integer studentId, Integer courseId);

    void addToCourse(Set<Integer> studentIds, Integer courseId);

    void deleteFromCourse(Integer studentId, Integer courseId);

    void deleteFromCourse(Set<Integer> studentIds, Integer courseId);

    List<CourseEntity> findByStudentId(Integer studentId);

}
