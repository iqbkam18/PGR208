package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.ProjectWorker;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static no.kristiania.project.management.database.dao.WorkerDaoTest.createDataSource;
import static org.assertj.core.api.Assertions.assertThat;

public class ProjectWorkerDaoTest {
    private ProjectWorkerDao projectWorkerDao;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = createDataSource();//Reuse createDataSource from WorkerDaoTest
        projectWorkerDao = new ProjectWorkerDao(dataSource);
    }


    @Test
    public void shouldListSavedProjectWorkers() throws SQLException {
        ProjectWorker projectWorker = randomProjectWorker();
        projectWorkerDao.insert(projectWorker);

        assertThat(projectWorkerDao.listAll())
                .flatExtracting(ProjectWorker::getProjectID,
                        ProjectWorker::getWorkerID)
                .contains(projectWorker.getProjectID(), projectWorker.getWorkerID());
    }

    public static ProjectWorker randomProjectWorker() {
        ProjectWorker projectWorker = new ProjectWorker();
        projectWorker.setWorkerID((long) new Random().nextInt(10));
        projectWorker.setProjectID((long) new Random().nextInt(10));
        return projectWorker;
    }


    @Test
    void shouldRetrieveSavedTasks() throws SQLException {
        ProjectWorker projectWorker = randomProjectWorker();
        projectWorkerDao.insert(projectWorker);
        assertThat(projectWorker).hasNoNullFieldsOrProperties();
        assertThat(projectWorkerDao.retrieve(projectWorker.getId())).isEqualToComparingFieldByField(projectWorker);
    }

}
