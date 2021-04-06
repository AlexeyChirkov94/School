package ua.com.foxminded.school.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.school.dao.ConnectorDB;
import ua.com.foxminded.school.dao.GroupDao;
import ua.com.foxminded.school.dao.StudentDao;
import ua.com.foxminded.school.dao.domain.Page;
import ua.com.foxminded.school.dao.domain.Pageable;
import ua.com.foxminded.school.dao.exception.DataBaseSqlRuntimeException;
import ua.com.foxminded.school.dao.tables.ScriptsRunner;
import ua.com.foxminded.school.dao.tables.ScriptsRunnerImpl;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.school.entity.GroupEntity;
import ua.com.foxminded.school.entity.StudentEntity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static ua.com.foxminded.school.utility.TestUtility.compareGroups;
import static ua.com.foxminded.school.utility.TestUtility.compareStudents;

@ExtendWith( MockitoExtension.class)
class GroupDaoImplTest {

    private static final Logger LOGGER = Logger.getLogger(GroupDaoImplTest.class);

    private static final String PATH_TO_DATABASE_PROPERTY = "property/database/schoolTest";
    private static final String PATH_TO_CREATE_TABLE_QUERY = "src\\test\\resources\\database\\schema.sql";
    private static final String PATH_TO_FILL_GROUPS_QUERY = "src\\test\\resources\\database\\groupFiller.sql";
    private static final String PATH_TO_FILL_STUDENTS_QUERY = "src\\test\\resources\\database\\studentFiller.sql";
    private static final String PATH_TO_FILL_COURSES_QUERY = "src\\test\\resources\\database\\courseFiller.sql";
    private static ConnectorDB connectorDB;
    private static ScriptsRunner scriptsRunner;
    private static StudentDao studentDao;
    private static GroupDao groupDao;

    @Mock
    private ConnectorDB mockConnectorDB;

    @Mock
    private Connection mockConnection;

    {
        try{
            connectorDB = new ConnectorDB(PATH_TO_DATABASE_PROPERTY);
            scriptsRunner = new ScriptsRunnerImpl(connectorDB);
            scriptsRunner.runScript(PATH_TO_CREATE_TABLE_QUERY);
            scriptsRunner.runScript(PATH_TO_FILL_GROUPS_QUERY);
            scriptsRunner.runScript(PATH_TO_FILL_STUDENTS_QUERY);
            scriptsRunner.runScript(PATH_TO_FILL_COURSES_QUERY);
            studentDao = new StudentDaoImpl(connectorDB);
            groupDao = new GroupDaoImpl(connectorDB);
        }catch (Exception e) {
            LOGGER.info("Exception of creating test data: ", e);
            throw new DataBaseSqlRuntimeException("Exception of creating test data: ", e);
        }
    }

    @Test
    void createAndReadShouldAddNewCourseToDatabaseIfArgumentIsCourseEntityAndReadIt(){
        GroupEntity addingGroup = GroupEntity.builder().withGroupName("rf-13").build();
        groupDao.save(addingGroup);
        GroupEntity readingGroup = groupDao.findById(5).get();

        compareGroups(readingGroup, addingGroup);
    }

    @Test
    void createAndReadShouldAddNewListOfCoursesToDatabaseIfArgumentIsListOfCourseEntityAndReadIt(){
        List<GroupEntity> addingGroups = Arrays.asList(GroupEntity.builder().withId(5).withGroupName("aa-00").build(),
                GroupEntity.builder().withId(6).withGroupName("aa-01").build());
        groupDao.saveAll(addingGroups);
        List<GroupEntity> readingGroups =  Arrays.asList(groupDao.findById(5).get(), groupDao.findById(6).get());

        compareGroups(readingGroups, addingGroups);
    }

    @Test
    void updateShouldUpdateDataOfCourseIfArgumentIsCourseEntity(){
        GroupEntity expected = GroupEntity.builder().withId(1).withGroupName("aa-00").build();
        groupDao.update(expected);
        GroupEntity actual = groupDao.findById(1).get();

        compareGroups(expected, actual);
    }

    @Test
    void updateListShouldUpdateDataOfCourseIfArgumentIsCourseEntity(){
        List<GroupEntity> expected = Arrays.asList(GroupEntity.builder().withId(1).withGroupName("aa-00").build(),
                GroupEntity.builder().withId(2).withGroupName("aa-01").build()
        );
        groupDao.updateAll(expected);
        List<GroupEntity> actual = Arrays.asList(groupDao.findById(1).get(), groupDao.findById(2).get());

        compareGroups(expected, actual);
    }

    @Test
    void deleteShouldDeleteDataOfCourseIfArgumentIsIdOfCourse(){
        Optional<GroupEntity> expected = Optional.empty();
        groupDao.deleteById(4);
        Optional<GroupEntity> actual = groupDao.findById(4);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void deleteSetShouldDeleteDataOfCourseIfArgumentIsIdOfCourse(){
        List<Optional<GroupEntity>> expected = Arrays.asList(Optional.empty(), Optional.empty());
        Set<Integer> idDeletingStudents = new HashSet<>();
        idDeletingStudents.add(4);
        idDeletingStudents.add(10);

        groupDao.deleteByIds(idDeletingStudents);
        List<Optional<GroupEntity>> actual = Arrays.asList(groupDao.findById(4), groupDao.findById(10));

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByGroupNameShouldReturnGroupEntityIfArgumentIsGroupName(){
        GroupEntity actual = groupDao.findByGroupName("as-23").get();
        GroupEntity expected = GroupEntity.builder().withGroupName("as-23").build();

        compareGroups(actual, expected);
    }

    @Test
    void transferStudentShouldEditGroupIdIfStudentIfArgumentIsTwoInteger(){
        groupDao.transferStudent(3, 7);

        StudentEntity actual = studentDao.findById(7).get();
        StudentEntity expected = StudentEntity.builder().withFirstName("Alyona").withLastName("Osipova").withGroup(3).build();

        compareStudents(actual, expected);
    }

    @Test
    void addStudentToGroupShouldAddStudentToGroupIfArgumentIsTwoInteger(){
        groupDao.addStudentToGroup(3, 5);
        StudentEntity actual = studentDao.findById(5).get();
        StudentEntity expected = StudentEntity.builder().withFirstName("Dmitriy").withLastName("Marin").withGroup(3).build();

        compareStudents(actual, expected);
    }

    @Test
    void deleteStudentFromGroupShouldDeleteStudentFromGroupIfArgumentIsStudentId(){
        groupDao.deleteStudentFromGroup(1);
        StudentEntity actual = studentDao.findById(1).get();
        StudentEntity expected = StudentEntity.builder().withFirstName("Alexey").withLastName("Chirkov").withGroup(0).build();

        compareStudents(actual, expected);
    }

    @Test
    void findAllNoPagesShouldReturnAllGroupEntitiesInDataBaseNoArgument(){
        List<GroupEntity> actual = groupDao.findAll();
        List<GroupEntity> expected = new ArrayList<>();
        expected.add(GroupEntity.builder().withGroupName("gi-93").build());
        expected.add(GroupEntity.builder().withGroupName("as-23").build());
        expected.add(GroupEntity.builder().withGroupName("fr-19").build());
        expected.add(GroupEntity.builder().withGroupName("de-53").build());

        compareGroups(actual, expected);
    }

    @Test
    void findAllWithPagesShouldReturnAllGroupEntitiesInCurrentPageIfArgumentNumberOfPage(){
        List<GroupEntity> items = new ArrayList<>();
        items.add(GroupEntity.builder().withId(1).withGroupName("gi-93").build());
        items.add(GroupEntity.builder().withId(2).withGroupName("as-23").build());

        Pageable<GroupEntity> actual = groupDao.findAll(new Page(1,2),2);
        Pageable<GroupEntity> expected = new Pageable<>(items, 1, 2, 2);

        assertThat(actual.getItems()).isEqualTo(expected.getItems());
        assertThat(actual.getPageNumber()).isEqualTo(expected.getPageNumber());
        assertThat(actual.getItemsNumberPerPage()).isEqualTo(expected.getItemsNumberPerPage());
        assertThat(actual.getMaxPageNumber()).isEqualTo(expected.getMaxPageNumber());
    }

    @Test
    void countMembersByGroupIdShouldReturnCountMembersByGroupIdIfArgumentIsGroupId(){
        int actual = groupDao.countMembersByGroupId(1);
        int expected = 4;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findGroupWithEqualOrLessCountOfStudentShouldReturnListOfGroupsIfArgumentIsMinCountOfStudent(){
            List<GroupEntity> actual = groupDao.findGroupWithEqualOrLessCountOfStudent(3);
            List<GroupEntity> expected = Arrays.asList(GroupEntity.builder().withGroupName("as-23").build(),
                    GroupEntity.builder().withGroupName("fr-19").build(),
                    GroupEntity.builder().withGroupName("de-53").build());

        compareGroups(actual, expected);
    }

    @Test
    void countShouldReturnCountOfGroupsWithNoArguments(){
        long expected = 4;
        long actual = groupDao.count();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllShouldReturnGroupsOfCurrentPageIfArgumentIsNumberOfPageAndCountOfElementsPerPage(){
        List<GroupEntity> expected = Arrays.asList(GroupEntity.builder().withGroupName("fr-19").build(),
                GroupEntity.builder().withGroupName("de-53").build());
        List<GroupEntity> actual = groupDao.findAll(2, 2);

        compareGroups(actual, expected);
    }


    @Test
    void transferStudentShouldThrowDataBaseSqlRuntimeExceptionIfStudentIdOutOfRange() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error at transferring: ")).when(mockConnection)
                .prepareStatement("UPDATE students SET group_id = ? WHERE student_id = ?");
        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.transferStudent(1, 15)).hasMessage("Error at transferring: ");
    }

    @Test
    void addStudentAtGroupShouldThrowDataBaseSqlRuntimeExceptionIfStudentIdOutOfRange() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error at adding to group: ")).when(mockConnection)
                .prepareStatement("UPDATE students SET group_id = ? WHERE student_id = ?");
        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.addStudentToGroup(1, 15)).hasMessage("Error at adding to group: ");
    }

    @Test
    void deleteStudentAtGroupShouldThrowDataBaseSqlRuntimeExceptionIfStudentIdOutOfRange() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error at deleting from group: ")).when(mockConnection)
                .prepareStatement("UPDATE students SET group_id = null WHERE student_id = ?");
        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.deleteStudentFromGroup(15)).hasMessage("Error at deleting from group: ");
    }

    @Test
    void countMembersByGroupIdShouldThrowDataBaseSqlRuntimeExceptionIfGroupDoesNotExist() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error at finding: ")).when(mockConnection)
                .prepareStatement("SELECT COUNT(*) as count from students s where s.group_id=?");
        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.countMembersByGroupId(15)).hasMessage("Error at finding: ");
    }

    @Test
    void findGroupWithEqualOrLessCountOfStudentShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of finding: ")).when(mockConnection)
                .prepareStatement("SELECT g.group_id as group_id, g.group_name as group_name, " +
                        "COUNT(s.group_id) AS count_of_group_members FROM groups g LEFT JOIN students s ON g.group_id = s.group_id " +
                        "GROUP BY g.group_id HAVING COUNT(s.group_id) <= ? order by g.group_id");

        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.findGroupWithEqualOrLessCountOfStudent(15)).hasMessage("Error of finding: ");
    }

    @Test
    void saveShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of save: ")).when(mockConnection)
                .prepareStatement("INSERT INTO groups (group_name) VALUES (?)");

        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.save(GroupEntity.builder().withGroupName("aa-00").build()))
                .hasMessage("Error of save: ");
    }

    @Test
    void saveListShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of save: ")).when(mockConnection)
                .prepareStatement("INSERT INTO groups (group_name) VALUES (?)");

        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.saveAll( Arrays.asList(GroupEntity.builder().withGroupName("aa-00").build(),
                GroupEntity.builder().withGroupName("aa-01").build())))
                .hasMessage("Error of save: ");
    }

    @Test
    void findAllNoPagesShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of findAll: ")).when(mockConnection)
                .prepareStatement("SELECT * FROM groups order by group_id");

        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.findAll())
                .hasMessage("Error of findAll: ");
    }

    @Test
    void updateShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of updating: ")).when(mockConnection)
                .prepareStatement("UPDATE groups SET group_name = ? WHERE group_id = ?");

        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.update(GroupEntity.builder().withGroupName("aa-00").build()))
                .hasMessage("Error of updating: ");
    }

    @Test
    void updateListShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of updating: ")).when(mockConnection)
                .prepareStatement("UPDATE groups SET group_name = ? WHERE group_id = ?");

        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.updateAll(Arrays.asList(GroupEntity.builder().withGroupName("aa-00").build(),
                GroupEntity.builder().withGroupName("aa-01").build())))
                .hasMessage("Error of updating: ");
    }

    @Test
    void deleteShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of deleting: ")).when(mockConnection)
                .prepareStatement("DELETE FROM groups where group_id=?");

        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.deleteById(1))
                .hasMessage("Error of deleting: ");
    }

    @Test
    void deleteSetShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of deleting: ")).when(mockConnection)
                .prepareStatement("DELETE FROM groups where group_id=?");

        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);
        Set<Integer> idsDeletingStudents = new HashSet<>();
        idsDeletingStudents.add(1);
        idsDeletingStudents.add(2);

        assertThatThrownBy(() -> groupDaoFromMock.deleteByIds(idsDeletingStudents))
                .hasMessage("Error of deleting: ");
    }

    @Test
    void findByStringParamShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error of finding: ")).when(mockConnection)
                .prepareStatement("SELECT * FROM groups WHERE group_name=?");

        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);


        assertThatThrownBy(() -> groupDaoFromMock.findByGroupName("s"))
                .hasMessage("Error of finding: ");
    }

    @Test
    void countShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error at counting: ")).when(mockConnection)
                .prepareStatement("SELECT COUNT(*) as count from groups");

        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.count())
                .hasMessage("Error at counting: ");
    }

    @Test
    void findAllShouldThrowDataBaseSqlRuntimeExceptionIfCanNotConnect() throws SQLException {
        doReturn(mockConnection).when(mockConnectorDB).getConnection();
        doThrow( new SQLException("Error at finding: ")).when(mockConnection)
                .prepareStatement("SELECT * FROM groups order by group_id offset ? row FETCH NEXT ? ROWS ONLY");

        GroupDao groupDaoFromMock = new GroupDaoImpl(mockConnectorDB);

        assertThatThrownBy(() -> groupDaoFromMock.findAll(1, 1))
                .hasMessage("Error at finding: ");
    }

}
