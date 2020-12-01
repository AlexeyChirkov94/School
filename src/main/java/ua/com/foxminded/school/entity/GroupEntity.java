package ua.com.foxminded.school.entity;

import java.util.Objects;

public final class GroupEntity implements Entity{
    private final Integer groupId;
    private final String groupName;

    public GroupEntity(Builder builder) {
        this.groupId = builder.groupId;
        this.groupName = builder.groupName;
    }

    public static Builder builder(){
        return new Builder();
    }

    @Override
    public Integer getId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        GroupEntity that = (GroupEntity) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(groupName, that.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName);
    }

    @Override
    public String toString() {
        return "GroupEntity{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                '}';
    }

    public static class Builder {
        private Integer groupId;
        private String groupName;

        private Builder() {
        }

        public GroupEntity build(){
            return new GroupEntity(this);
        }

        public GroupEntity.Builder withId(Integer id) {
            this.groupId = id;
            return this;
        }

        public GroupEntity.Builder withGroupName(String groupName) {
            this.groupName = groupName;
            return this;
        }
    }
}
