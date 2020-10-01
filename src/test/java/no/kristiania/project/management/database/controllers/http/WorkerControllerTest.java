package no.kristiania.project.management.database.controllers.http;

import no.kristiania.project.management.database.dao.WorkerDao;
import no.kristiania.project.management.database.dao.WorkerDaoTest;
import no.kristiania.project.management.database.tabels.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkerControllerTest {

    private WorkerDao workerDao;
    private WorkerHttpController controller;

    @BeforeEach
    void setUp() {
        workerDao = new WorkerDao(WorkerDaoTest.createDataSource());
        controller = new WorkerHttpController(workerDao);
    }

    @Test
    void shouldReturnWorkerFromDatabase() throws SQLException {

        Worker worker1 = WorkerDaoTest.randomWorker();
        Worker worker2 = WorkerDaoTest.randomWorker();

        workerDao.insert(worker1);
        workerDao.insert(worker2);

        assertThat(controller.getBody())
                .contains(String.format("<option name = '%s' value = '%s' id='%s'>%s</option>",
                        worker1.getId(),worker1.getId(), worker1.getId(), (worker1.getName() + " " + worker1.getLastName() + " - " + worker1.getAddress())))
                .contains(String.format("<option name = '%s' value = '%s' id='%s'>%s</option>",
                        worker2.getId(),worker2.getId(), worker2.getId(), (worker2.getName() + " " + worker2.getLastName() + " - " + worker2.getAddress())));
    }

    @Test
    void shouldCreateNewWorker() throws IOException, SQLException {

        String requestBody = "name=Jack&lastName=Sparrow&address=world";
        controller.handle("POST", "/projectMembers", null, requestBody, new ByteArrayOutputStream());

        assertThat(workerDao.listAll())
                .flatExtracting(Worker::getName, Worker::getLastName, Worker::getAddress)
                .contains("Jack", "Sparrow", "world");
    }
}
