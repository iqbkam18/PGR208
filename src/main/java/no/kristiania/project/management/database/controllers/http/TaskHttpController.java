package no.kristiania.project.management.database.controllers.http;

import no.kristiania.project.management.database.dao.TaskDao;
import no.kristiania.project.management.database.tabels.Task;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskHttpController extends AbstractDatabaseHttpController {

    private final TaskDao taskDao;

    public TaskHttpController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public String getBody() throws SQLException {
        return taskDao.listAll().stream()
                .map(p -> String.format("<option name = '%s' value='%s' id='%s'>%s</option>", p.getId(), p.getId(), p.getId(),
                        p.getDescription() + " - "  +p.getStatus() ))
                .collect(Collectors.joining(""));
    }

    @Override
    protected void insertData(Map<String, String> query) throws SQLException {
        Task task = new Task();
        task.setDescription(query.get("description"));
        task.setStatus(query.get("status"));
        taskDao.insert(task);
    }
}
