package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.Worker;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WorkerDao extends AbstractDao<Worker> {
    public WorkerDao(DataSource dataSource) {
        super(dataSource);
    }

    public Worker retrieve(Long id) throws SQLException {
        return retrieve(id, "select * from workers where id = ?");
    }

    public void insert(Worker worker) throws SQLException {
        long id = insert(worker, "insert into workers (name, lastName, address) values (?,?,?)");
        worker.setId(id);

    }

    public List<Worker> listAll() throws SQLException {
        return listAll("select * from workers", -1);
    }



    @Override
    protected void mapToStatement(Worker worker, PreparedStatement statement) throws SQLException {
        statement.setString(1, worker.getName());
        statement.setString(2, worker.getLastName());
        statement.setString(3, worker.getAddress());
    }

    @Override
    protected Worker mapFromResultSet(ResultSet rs) throws SQLException {
        Worker worker = new Worker();
        worker.setId(rs.getLong("id"));
        worker.setName(rs.getString("name"));
        worker.setLastName(rs.getString("lastName"));
        worker.setAddress(rs.getString("address"));
        return worker;
    }

}
