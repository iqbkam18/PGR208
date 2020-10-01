package no.kristiania.project.management.database.controllers.http;

import no.kristiania.project.management.database.dao.WorkerTaskDao;
import no.kristiania.project.management.database.tabels.WorkerTask;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkerTaskHttpController extends AbstractDatabaseHttpController {
    private final WorkerTaskDao workerTaskDao;

    public WorkerTaskHttpController(WorkerTaskDao workerTaskDao) {
        this.workerTaskDao = workerTaskDao;
    }

    @Override
    public String getBody() throws SQLException {
        return workerTaskDao.listAll().stream()
                .map(p -> String.format("<option value='%s'>%s</option>", p.getId(), (p.getWorkerID() + " " + p.getTaskID())))
                .collect(Collectors.joining(""));
    }

    @Override
    protected void insertData(Map<String, String> query) throws SQLException {
        WorkerTask workerTask = new WorkerTask();
        workerTask.setWorkerID(Long.parseLong(query.get("workerID")));
        workerTask.setTaskID(Long.parseLong(query.get("taskID")));
        workerTaskDao.insert(workerTask);
    }
}
