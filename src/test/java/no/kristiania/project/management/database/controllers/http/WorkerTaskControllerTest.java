package no.kristiania.project.management.database.controllers.http;

import no.kristiania.project.management.database.dao.WorkerDaoTest;
import no.kristiania.project.management.database.dao.WorkerTaskDao;
import no.kristiania.project.management.database.dao.WorkerTaskDaoTest;
import no.kristiania.project.management.database.tabels.WorkerTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkerTaskControllerTest {

    private WorkerTaskDao workerTaskDao;
    private WorkerTaskHttpController controller;

    @BeforeEach
    void setUp() {
        workerTaskDao = new WorkerTaskDao(WorkerDaoTest.createDataSource());
        controller = new WorkerTaskHttpController(workerTaskDao);
    }

    @Test
    void shouldReturnWorkerTasksFromDatabase() throws SQLException {

        WorkerTask workerTask1 = WorkerTaskDaoTest.randomWorkerTask();
        WorkerTask workerTask2= WorkerTaskDaoTest.randomWorkerTask();

        workerTaskDao.insert(workerTask1);
        workerTaskDao.insert(workerTask2);

        assertThat(controller.getBody())
                .contains(String.format("<option value='%s'>%s</option>", workerTask1.getId(), (workerTask1.getWorkerID() + " " + workerTask1.getTaskID())))
                .contains(String.format("<option value='%s'>%s</option>", workerTask2.getId(), (workerTask2.getWorkerID() + " " + workerTask2.getTaskID())));
    }

    @Test
    void shouldConnectWorkerAndTask() throws IOException, SQLException {

        String requestBody = "taskID=3&workerID=1";
        controller.handle("POST", "/taskToWorker", null, requestBody, new ByteArrayOutputStream());

        assertThat(workerTaskDao.listAll())
                .flatExtracting(WorkerTask::getWorkerID,WorkerTask::getTaskID)
                .contains(1L,3L);
    }
}
