package ua.com.foxminded.school.entity;

import java.util.List;
import java.util.Objects;
import static ua.com.foxminded.school.utility.CollectionUtility.nullSafeListInitialize;

public final class StudentEntity implements Entity{

    private final Integer id;
    private final Integer groupId;
    private final String firstName;
    private final String lastName;
    private final List<CourseEntity> courseEntities;

    public StudentEntity(Builder builder) {
        this.id = builder.id;
        this.groupId = builder.groupId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.courseEntities = nullSafeListInitialize(builder.courseEntities);
    }
    
    public static Builder builder(){
        return new Builder();
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<CourseEntity> getCourseEntities() {
        return courseEntities;
    }

    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        StudentEntity that = (StudentEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(groupId, that.groupId) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(courseEntities, that.courseEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, firstName, lastName, courseEntities);
    }

    @Override
    public String toString() {
        return "StudentEntity{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", courseEntities=" + courseEntities +
                '}';
    }

    public static class Builder {
        private Integer id;
        private Integer groupId;
        private String firstName;
        private String lastName;
        private List<CourseEntity> courseEntities;

        private Builder() {
        }

        public StudentEntity build(){
            return new StudentEntity(this);
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withGroup(Integer groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withCourses(List<CourseEntity> courseEntities) {
            this.courseEntities = courseEntities;
            return this;
        }
    }

}
