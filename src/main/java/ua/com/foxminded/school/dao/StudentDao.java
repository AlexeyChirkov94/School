package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.entity.StudentEntity;

import java.util.List;

public interface StudentDao extends CrudPageableDao<StudentEntity> {

    List<StudentEntity> findByGroupId(Integer groupId);

    List<StudentEntity> findByFirstName(String firstName);

    List<StudentEntity> findByLastName(String lastName);

    List<StudentEntity> findByCourseName(String courseName);

}
