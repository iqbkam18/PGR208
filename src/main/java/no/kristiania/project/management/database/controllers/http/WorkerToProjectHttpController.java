package no.kristiania.project.management.database.controllers.http;

import no.kristiania.project.management.database.dao.ProjectWorkerDao;
import no.kristiania.project.management.database.tabels.ProjectWorker;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This controller is used for connecting Workers and Projects
 */
public class WorkerToProjectHttpController extends AbstractDatabaseHttpController {
    private final ProjectWorkerDao projectWorkerDao;

    public WorkerToProjectHttpController(ProjectWorkerDao projectWorkerDao) {
        this.projectWorkerDao = projectWorkerDao;
    }

    @Override
    public String getBody() throws SQLException {
        return projectWorkerDao.listAll().stream()
                .map(p -> String.format("<option value='%s'>%s</option>", p.getId(), (p.getProjectID() + " " + p.getWorkerID())))
                .collect(Collectors.joining(""));
    }

    @Override
    protected void insertData(Map<String, String> query) throws SQLException {
        ProjectWorker projectWorker = new ProjectWorker();
        projectWorker.setProjectID(Long.parseLong(query.get("projectID")));
        projectWorker.setWorkerID(Long.parseLong(query.get("workerID")));
        projectWorkerDao.insert(projectWorker);
    }
}
