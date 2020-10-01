package no.kristiania.project.management.database.controllers.update;

import no.kristiania.project.management.database.dao.ProjectDao;
import no.kristiania.project.management.database.tabels.Project;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectUpdateController extends AbstractDatabaseUpdateController {
    private final ProjectDao projectDao;

    public ProjectUpdateController(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    protected void update(Map<String, String> query) throws SQLException {
        Project project = new Project();
        project.setName(query.get("name"));
        project.setId(Long.parseLong(query.get("ID")));
        projectDao.update(project);
    }

    /**
     * Needed only for testing of class
     *
     * @return Returns html as string representation
     * @throws SQLException sql exception in case of wrong sql expression
     */
    public String getBody() throws SQLException {
        return projectDao.listAll().stream()
                .map(p -> String.format("<option name = '%s' value='%s' id='%s'>%s</option>", p.getId(), p.getId(), p.getId(),
                        p.getName()))
                .collect(Collectors.joining(""));
    }
}
