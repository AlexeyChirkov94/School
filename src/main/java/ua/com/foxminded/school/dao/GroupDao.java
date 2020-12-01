package ua.com.foxminded.school.dao;

import ua.com.foxminded.school.entity.GroupEntity;
import java.util.List;
import java.util.Optional;

public interface GroupDao extends CrudPageableDao<GroupEntity>{

    Optional<GroupEntity> findByGroupName(String groupName);

    void transferStudent(Integer groupIdWhere, Integer studentId);

    void addStudentToGroup(Integer groupId, Integer studentId);

    void deleteStudentFromGroup(Integer studentId);

    int countMembersByGroupId(Integer groupId);

    List<GroupEntity> findGroupWithEqualOrLessCountOfStudent(int countOfStudent);

}
