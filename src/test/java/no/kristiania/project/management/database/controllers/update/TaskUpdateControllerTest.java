package no.kristiania.project.management.database.controllers.update;

import no.kristiania.project.management.database.dao.TaskDao;
import no.kristiania.project.management.database.dao.TaskDaoTest;
import no.kristiania.project.management.database.dao.WorkerDaoTest;
import no.kristiania.project.management.database.tabels.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TaskUpdateControllerTest {
    private TaskDao taskDao;
    private TaskUpdateController updaterController;


    @BeforeEach
    void setUp() {
        taskDao = new TaskDao(WorkerDaoTest.createDataSource());
        updaterController = new TaskUpdateController(taskDao);
    }

    @Test
    void shouldUpdateTaskFromDatabase() throws SQLException {

        Task task = TaskDaoTest.randomTask();

        taskDao.insert(task);

        assertThat(updaterController.getBody())
                .contains(String.format("<option name = '%s' value='%s' id='%s'>%s</option>", task.getId(), task.getId(), task.getId(),
                        task.getDescription() + " - " + task.getStatus()));

        Map<String, String> query = new HashMap<>();
        query.put("description", "newDescription");
        query.put("status", "newStatus");
        query.put("ID", String.valueOf(task.getId()));
        updaterController.update(query);

        //We expect that new values in task are going to be equal to parameters of query map
        task.setDescription("newDescription");
        task.setStatus("newStatus");


        assertThat(updaterController.getBody())
                .contains(String.format("<option name = '%s' value='%s' id='%s'>%s</option>", task.getId(), task.getId(), task.getId(),
                        task.getDescription() + " - " + task.getStatus()));
    }

}