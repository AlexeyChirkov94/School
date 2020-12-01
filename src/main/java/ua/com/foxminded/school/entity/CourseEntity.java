package ua.com.foxminded.school.entity;

import java.util.Objects;

public final class CourseEntity implements Entity{
    private final Integer courseId;
    private final String courseName;
    private final String courseDescription;

    public CourseEntity(Builder builder) {
        this.courseId = builder.courseId;
        this.courseName = builder.courseName;
        this.courseDescription = builder.courseDescription;
    }

    public static Builder builder(){
        return new Builder();
    }

    @Override
    public Integer getId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        CourseEntity that = (CourseEntity) o;
        return Objects.equals(courseId, that.courseId) &&
                Objects.equals(courseName, that.courseName) &&
                Objects.equals(courseDescription, that.courseDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, courseName, courseDescription/*, studentEntities*/);
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                '}';
    }

    public static class Builder {
        private Integer courseId;
        private String courseName;
        private String courseDescription;

        private Builder() {
        }

        public CourseEntity build(){
            return new CourseEntity(this);
        }

        public Builder withId(Integer id) {
            this.courseId = id;
            return this;
        }

        public Builder withCourseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public Builder withCourseDescription(String courseDescription) {
            this.courseDescription = courseDescription;
            return this;
        }

    }

}
