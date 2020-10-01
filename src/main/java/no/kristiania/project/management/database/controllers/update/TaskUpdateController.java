package no.kristiania.project.management.database.controllers.update;

import no.kristiania.project.management.database.dao.TaskDao;
import no.kristiania.project.management.database.tabels.Task;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskUpdateController extends AbstractDatabaseUpdateController {
    private final TaskDao taskDao;

    public TaskUpdateController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    protected void update(Map<String, String> query) throws SQLException {
        Task task = new Task();
        task.setDescription(query.get("description"));
        task.setStatus(query.get("status"));
        task.setId(Long.parseLong(query.get("ID")));
        taskDao.update(task);
    }

    /**
     * Needed only for testing
     * @return Returns html as string representation
     * @throws SQLException sql exception in case of wrong sql expression
     */
    public String getBody() throws SQLException {
        return taskDao.listAll().stream()
                .map(p -> String.format("<option name = '%s' value='%s' id='%s'>%s</option>", p.getId(), p.getId(), p.getId(),
                        p.getDescription() + " - "  +p.getStatus() ))
                .collect(Collectors.joining(""));
    }
}
