package no.kristiania.project.management.database.controllers.http;

import no.kristiania.project.management.database.dao.TaskDao;
import no.kristiania.project.management.database.dao.TaskDaoTest;
import no.kristiania.project.management.database.dao.WorkerDaoTest;
import no.kristiania.project.management.database.tabels.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskControllerTest {

    private TaskDao taskDao;
    private TaskHttpController controller;

    @BeforeEach
    void setUp() {
        taskDao = new TaskDao(WorkerDaoTest.createDataSource());
        controller = new TaskHttpController(taskDao);
    }

    @Test
    void shouldReturnTaskFromDatabase() throws SQLException {

        Task task1 = TaskDaoTest.randomTask();
        Task task2 = TaskDaoTest.randomTask();

        taskDao.insert(task1);
        taskDao.insert(task2);

        assertThat(controller.getBody())
                .contains(String.format("<option name = '%s' value='%s' id='%s'>%s</option>", task1.getId(), task1.getId(), task1.getId(),
                        task1.getDescription() + " - "  +task1.getStatus()))
                .contains(String.format("<option name = '%s' value='%s' id='%s'>%s</option>", task2.getId(), task2.getId(), task2.getId(),
                        task2.getDescription() + " - "  +task2.getStatus()));
    }

    @Test
    void shouldCreateNewTask() throws IOException, SQLException {

        String requestBody = "description=DESC&status=STS";
        controller.handle("POST", "/taskCreator", null, requestBody, new ByteArrayOutputStream());

        assertThat(taskDao.listAll())
                .flatExtracting(Task::getDescription,Task::getStatus)
                .contains("DESC", "STS");
    }
}
