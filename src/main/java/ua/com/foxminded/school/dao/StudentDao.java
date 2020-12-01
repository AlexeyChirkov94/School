package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.entity.StudentEntity;

import java.util.List;

public interface StudentDao extends CrudPageableDao<StudentEntity> {

    Optional<List<StudentEntity>> findByGroupId(Integer groupId);
    Optional<List<StudentEntity>> findByFirstName(String firstName);
    Optional<List<StudentEntity>> findByLastName(String lastName);

    List<StudentEntity> findByCourseName(String courseName);

}
