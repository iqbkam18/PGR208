package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.WorkersTasksView;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WorkersTasksViewDao extends AbstractDao<WorkersTasksView> {
    public WorkersTasksViewDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void mapToStatement(WorkersTasksView workerTaskView, PreparedStatement statement) {
        throw new IllegalAccessError("Not implemented as this DAO is just for viewing data not editing");
    }

    @Override
    protected WorkersTasksView mapFromResultSet(ResultSet rs) throws SQLException {
        WorkersTasksView workerTaskView = new WorkersTasksView();
        workerTaskView.setLastName(rs.getString("lastName"));
        workerTaskView.setName(rs.getString("name"));
        workerTaskView.setAddress(rs.getString("address"));
        workerTaskView.setDescription(rs.getString("description"));
        workerTaskView.setStatus(rs.getString("status"));
        return workerTaskView;
    }

    /**
     * Using listAll to list users with tasks
     * (fra eksamens text: Liste opp prosjektoppgaver, inkludert status og tildelte prosjektmedlemmer)
     *
     * @return returns list of WorkerTaskView. WorkerTaskView is holding infomration about Worker (Name, LastName, address) and Task (description and status)
     */
    public List<WorkersTasksView> listTasksForWorkers() throws SQLException {
        return listAll("select*\n" +
                "from workers workersTable\n" +
                "inner join worker_task workerTaskTable on workersTable.id = workerTaskTable.workerid\n" +
                "inner join tasks  taskTable on taskTable.id = workerTaskTable.id;", -1);
    }

    public List<WorkersTasksView> listTasksForOneWorker(long id) throws SQLException {
        return listAll("select name, lastname, address, description, status\n" +
                "from workers workersTable\n" +
                "inner join worker_task workerTaskTable on workersTable.id = workerTaskTable.workerid\n" +
                "inner join tasks  taskTable on taskTable.id = workerTaskTable.id\n" +
                "where workersTable.id = (?)", id);
    }
}
