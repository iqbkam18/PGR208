package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.Task;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static no.kristiania.project.management.database.dao.WorkerDaoTest.createDataSource;
import static no.kristiania.project.management.database.dao.WorkerDaoTest.pickOne;
import static org.assertj.core.api.Assertions.assertThat;

public class TaskDaoTest {
    private TaskDao taskDao;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = createDataSource();//Reuse createDataSource from WorkerDaoTest
        taskDao = new TaskDao(dataSource);
    }


    @Test
    public void shouldListSavedTasks() throws SQLException {
        Task task = randomTask();
        taskDao.insert(task);

        assertThat(taskDao.listAll())
                .flatExtracting(Task::getDescription, Task::getStatus)
                .contains(task.getDescription(), task.getStatus());
    }

    public static Task randomTask() {
        Task task = new Task();
        task.setDescription(pickOne(new String[]{"Rush it", "Easy", "Expensive", "Fun to do"}));
        task.setStatus(pickOne(new String[]{"Done", "Active", "To do", "On pause"}));
        task.setId((new Random().nextInt(10)));
        return task;
    }


    @Test
    void shouldRetrieveSavedTasks() throws SQLException {
        Task task = randomTask();
        taskDao.insert(task);
        assertThat(task).hasNoNullFieldsOrProperties();
        assertThat(taskDao.retrieve(task.getId())).isEqualToComparingFieldByField(task);
    }


}
