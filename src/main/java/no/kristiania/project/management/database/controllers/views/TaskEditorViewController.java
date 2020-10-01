package no.kristiania.project.management.database.controllers.views;

import no.kristiania.project.management.database.dao.TaskDao;
import no.kristiania.project.management.database.tabels.Task;

import java.sql.SQLException;

public class TaskEditorViewController extends AbstractDatabaseViewController {

    private final TaskDao taskDao;

    public TaskEditorViewController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    /**
     * Dinamicly generated html POST form, because it is dependent on input
     * @param id id of task in table
     * @return HTML POST form in string format
     * @throws SQLException
     */
    @Override
    protected String getBody(long id) throws SQLException {
        Task retrievedTask = taskDao.retrieve(id);
        String textFields = String.format("<link rel=\"stylesheet\" href=\"style.css\">" +
                        "<input type='text' hidden name='ID' value='%s'>" +
                        "<input type='text' hidden name='goTo' value='/listTasks.html'>" +
                        "%s - <input name='description' type='text' value = '%s'> <input name='status' type='text'  value = '%s'>",
                retrievedTask.getId(), retrievedTask.getId(),
                retrievedTask.getDescription(), retrievedTask.getStatus());
        return "<form method=\"POST\" action=\"/saveTaskChanges\">\n" +
                textFields +
                "<button>Save changes</button>\n" +
                "</form>" +
                "<a href=\".\">Return to front page</a>";
    }
}
