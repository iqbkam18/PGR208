package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.WorkerTask;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This DAO is used for adding to associative entity table
 * that connects Workers and Task tables
 */
public class WorkerTaskDao extends AbstractDao<WorkerTask> {
    public WorkerTaskDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void mapToStatement(WorkerTask projectTask, PreparedStatement statement) throws SQLException {
        statement.setLong(1, projectTask.getWorkerID());
        statement.setLong(2, projectTask.getTaskID());
    }

    @Override
    protected WorkerTask mapFromResultSet(ResultSet rs) throws SQLException {
        WorkerTask workerTask = new WorkerTask();
        workerTask.setId(rs.getLong("id"));
        workerTask.setWorkerID(rs.getLong("workerID"));
        workerTask.setTaskID(rs.getLong("taskID"));
        return workerTask;
    }

    public List<WorkerTask> listAll() throws SQLException {
        return listAll("select * from worker_task", -1);
    }

    public void insert(WorkerTask workerTask) throws SQLException {
        long id = insert(workerTask, "insert into worker_task (workerID, taskID) values (?, ?)");
        workerTask.setId(id);
    }

    public WorkerTask retrieve(Long id) throws SQLException {
        return retrieve(id, "select * from worker_task where id = ?");
    }
}