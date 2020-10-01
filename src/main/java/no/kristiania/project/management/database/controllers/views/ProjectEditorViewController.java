package no.kristiania.project.management.database.controllers.views;

import no.kristiania.project.management.database.dao.ProjectDao;
import no.kristiania.project.management.database.tabels.Project;

import java.sql.SQLException;

public class ProjectEditorViewController extends AbstractDatabaseViewController {

    private final ProjectDao projectDao;

    public ProjectEditorViewController(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    /**
     * Dinamicly generated html POST form, because it is dependent on input
     * @param id id of task in table
     * @return HTML POST form in string format
     * @throws SQLException
     */
    @Override
    protected String getBody(long id) throws SQLException {
        Project retrievedProject = projectDao.retrieve(id);
        String textFields = String.format("<link rel=\"stylesheet\" href=\"style.css\">" +
                        "<input type='text' hidden name='ID' value='%s'>" +
                        "<input type='text' hidden name='goTo' value='/listProjects.html'>" +
                        "%s - <input name='name' type='text' value = '%s'>",
                retrievedProject.getId(), retrievedProject.getId(),
                retrievedProject.getName());
        return "<form method=\"POST\" action=\"/saveProjectChanges\">\n" +
                textFields +
                "<button>Save changes</button>\n" +
                "</form>" +
                "<a href=\".\">Return to front page</a>";
    }
}
