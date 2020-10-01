package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.ProjectWorker;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This Dao represents one worker that is working on project thus project --> worker
 * One worker can work on multiple projects
 * One project can have multiple workers
 * This table is not going to have any original data (non foreign keys)
 * All data in table is from other tables ( tables project and worker)
 * Table has ID, ProjectID and WorkerID
 * Although it is possible to have table without ID for simplicity reasons we have also ID for each field (to avoid overriding methods)
 */
public class ProjectWorkerDao extends AbstractDao<ProjectWorker> {

    public ProjectWorkerDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void mapToStatement(ProjectWorker projectWorker, PreparedStatement statement) throws SQLException {
        statement.setLong(1,projectWorker.getProjectID());
        statement.setLong(2,projectWorker.getWorkerID());
    }

    @Override
    protected ProjectWorker mapFromResultSet(ResultSet rs) throws SQLException {
        ProjectWorker projectWorker = new ProjectWorker();
        projectWorker.setId(rs.getLong("id"));
        projectWorker.setProjectID(rs.getLong("projectID"));
        projectWorker.setWorkerID(rs.getLong("workerID"));
        return projectWorker;
    }

    public ProjectWorker retrieve(Long id) throws SQLException {
        return retrieve(id, "select * from project_workers where id = ?");
    }


    public void insert(ProjectWorker projectWorker) throws SQLException {
        long id = insert(projectWorker, "insert into project_workers (projectID, workerID) values (?,?)");
        projectWorker.setId(id);

    }

    public List<ProjectWorker> listAll() throws SQLException {
        return listAll("select * from PROJECT_WORKERS", -1);
    }
}
