package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.WorkerTask;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static no.kristiania.project.management.database.dao.WorkerDaoTest.createDataSource;
import static org.assertj.core.api.Assertions.assertThat;

public class WorkerTaskDaoTest {
    private WorkerTaskDao workerTaskDao;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = createDataSource();//Reuse createDataSource from WorkerDaoTest
        workerTaskDao = new WorkerTaskDao(dataSource);
    }


    @Test
    public void shouldListSavedWorkerTasks() throws SQLException {
        WorkerTask workerTask = randomWorkerTask();
        workerTaskDao.insert(workerTask);

        assertThat(workerTaskDao.listAll())
                .flatExtracting(WorkerTask::getWorkerID, WorkerTask::getTaskID)
                .contains(workerTask.getWorkerID(), workerTask.getTaskID());
    }

    public static WorkerTask randomWorkerTask() {
        WorkerTask workerTask = new WorkerTask();
        workerTask.setWorkerID(new Random().nextInt(10));
        workerTask.setTaskID(new Random().nextInt(10));
        return workerTask;
    }

    @Test
    void shouldRetrieveSavedWorkerTasks() throws SQLException {
        WorkerTask workerTask = randomWorkerTask();
        workerTaskDao.insert(workerTask);
        assertThat(workerTask).hasNoNullFieldsOrProperties();
        assertThat(workerTaskDao.retrieve(workerTask.getId())).isEqualToComparingFieldByField(workerTask);
    }


}
