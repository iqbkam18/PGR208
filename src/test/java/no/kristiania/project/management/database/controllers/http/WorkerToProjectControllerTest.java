package no.kristiania.project.management.database.controllers.http;

import no.kristiania.project.management.database.dao.ProjectWorkerDao;
import no.kristiania.project.management.database.dao.ProjectWorkerDaoTest;
import no.kristiania.project.management.database.dao.WorkerDaoTest;
import no.kristiania.project.management.database.tabels.ProjectWorker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkerToProjectControllerTest {

    private ProjectWorkerDao projectWorkerDao;
    private WorkerToProjectHttpController controller;

    @BeforeEach
    void setUp() {
        projectWorkerDao = new ProjectWorkerDao(WorkerDaoTest.createDataSource());
        controller = new WorkerToProjectHttpController(projectWorkerDao);
    }

    @Test
    void shouldReturnWorkerToProjectFromDatabase() throws SQLException {

        ProjectWorker projectWorker1 = ProjectWorkerDaoTest.randomProjectWorker();
        ProjectWorker projectWorker2 = ProjectWorkerDaoTest.randomProjectWorker();

        projectWorkerDao.insert(projectWorker1);
        projectWorkerDao.insert(projectWorker2);

        assertThat(controller.getBody())
                .contains(String.format("<option value='%s'>%s</option>", projectWorker1.getId(), (projectWorker1.getProjectID() + " " + projectWorker1.getWorkerID())))
                .contains(String.format("<option value='%s'>%s</option>", projectWorker2.getId(), (projectWorker2.getProjectID() + " " + projectWorker2.getWorkerID())));
    }

    @Test
    void ShouldAddWorkerToProject() throws IOException, SQLException {

        String requestBody = "projectID=4&workerID=3";
        controller.handle("POST", "/workerToProject", null, requestBody, new ByteArrayOutputStream());

        assertThat(projectWorkerDao.listAll())
                .flatExtracting(ProjectWorker::getWorkerID, ProjectWorker::getProjectID)
                .contains(3L, 4L);
    }
}
