package ua.com.foxminded.school.factories;

import ua.com.foxminded.school.entity.GroupEntity;

import java.util.List;

public interface GroupFactory {

    List<GroupEntity> generateGroups(int count);

}
