drop table if exists "STUDENT_COURSES";
drop table if exists "COURSES";
drop table if exists "STUDENTS";
drop table if exists "GROUPS";

create table groups
(
    group_id serial primary key,
    group_name varchar(30)
);

create table students
(
    student_id serial primary key,
    group_id int references groups(group_id),
    first_name varchar(30),
    last_name varchar(30)
);

create table courses
(
    course_id serial primary key,
    course_name varchar(30),
    course_description text
);

create table student_courses
(
    student_id int references students(student_id),
    course_id int references courses(course_id)
);