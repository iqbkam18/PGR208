package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.Task;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TaskDao extends AbstractDao<Task> {

    public TaskDao(DataSource dataSource) {
        super(dataSource);
    }

    public Task retrieve(Long id) throws SQLException {
        return retrieve(id, "select * from tasks where id = ?");
    }

    public void insert(Task task) throws SQLException {
        long id = insert(task, "insert into tasks (description, status) values (?,?)");
        task.setId(id);
    }

    public List<Task> listAll() throws SQLException {
        return listAll("select * from tasks", -1);
    }

    @Override
    protected void mapToStatement(Task task, PreparedStatement statement) throws SQLException {
        statement.setString(1, task.getDescription());
        statement.setString(2, task.getStatus());
    }

    @Override
    protected Task mapFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
        return task;
    }

    public void update(Task taskToInsert) throws SQLException {
        update(taskToInsert,
                "update tasks set description =(?), status = (?) where id = (?)");
    }

    @Override
    protected void mapToUpdateStatement(Task task, PreparedStatement statement) throws SQLException {
        statement.setString(1, task.getDescription());
        statement.setString(2, task.getStatus());
        statement.setLong(3, task.getId());
    }
}
