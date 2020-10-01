package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.Worker;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkerDaoTest {
    private WorkerDao workerDao;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = createDataSource();
        workerDao = new WorkerDao(dataSource);
    }

    public static JdbcDataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:workerTest;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    @Test
    public void shouldListSavedWorkers() throws SQLException {
        Worker worker = randomWorker();
        workerDao.insert(worker);

        assertThat(workerDao.listAll())
                .flatExtracting(Worker::getName,//https://github.com/joel-costigliola/assertj-core/issues/644  to check multiple values we used this solution
                        Worker::getAddress,
                        Worker::getLastName)
                .contains(worker.getName(), worker.getAddress(), worker.getLastName());
    }

    @Test
    void shouldRetrieveSavedWorkers() throws SQLException {
        Worker worker = randomWorker();
        workerDao.insert(worker);
        assertThat(worker).hasNoNullFieldsOrProperties();
        assertThat(workerDao.retrieve(worker.getId())).isEqualToComparingFieldByField(worker);
    }

    public static Worker randomWorker() {
        Worker worker = new Worker();
        String firstName = pickOne(new String[]{"Molly", "Ria", "Aleksander", "Ross", "Travis"});
        String lastName = pickOne(new String[]{"James", "Sha", "Osborne", "Yates", "Griffin"});
        String address = pickOne(new String[]{"Enggravlia 209", "Hovedgårdsveien 13", "Innerdammen 173", "Vågebyveien 141", "Torbjørn Blindes veg 130"});
        worker.setName(firstName);
        worker.setLastName(lastName);
        worker.setAddress(address);
        return worker;
    }

    public static String pickOne(String[] inputArray) {
        return inputArray[new Random().nextInt(inputArray.length)];
    }
}
