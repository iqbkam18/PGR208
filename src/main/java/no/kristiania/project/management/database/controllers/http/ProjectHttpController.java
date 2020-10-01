package no.kristiania.project.management.database.controllers.http;

import no.kristiania.project.management.database.dao.ProjectDao;
import no.kristiania.project.management.database.tabels.Project;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectHttpController extends AbstractDatabaseHttpController {

    private final ProjectDao projectDao;

    public ProjectHttpController(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    public String getBody() throws SQLException {
        return projectDao.listAll().stream()
                .map(p -> String.format("<option value='%s' id='%s'>%s</option>", p.getId(), p.getId(), p.getName()))
                .collect(Collectors.joining(""));
    }

    @Override
    protected void insertData(Map<String, String> query) throws SQLException {
        Project project = new Project();
        project.setName(query.get("name"));
        projectDao.insert(project);
    }
}
