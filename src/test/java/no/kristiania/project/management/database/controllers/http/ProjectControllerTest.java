package no.kristiania.project.management.database.controllers.http;

import no.kristiania.project.management.database.dao.ProjectDao;
import no.kristiania.project.management.database.dao.ProjectDaoTest;
import no.kristiania.project.management.database.dao.WorkerDaoTest;
import no.kristiania.project.management.database.tabels.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectControllerTest {

    private ProjectDao projectDao;
    private ProjectHttpController controller;

    @BeforeEach
    void setUp() {
        projectDao = new ProjectDao(WorkerDaoTest.createDataSource());
        controller = new ProjectHttpController(projectDao);
    }

    @Test
    void shouldReturnProjectsFromDatabase() throws SQLException {

        Project project1 = ProjectDaoTest.randomProject();
        Project project2 = ProjectDaoTest.randomProject();

        projectDao.insert(project1);
        projectDao.insert(project2);

        assertThat(controller.getBody())
                .contains(String.format("<option value='%s' id='%s'>%s</option>",project1.getId(),project1.getId(),project1.getName()))
                .contains(String.format("<option value='%s' id='%s'>%s</option>",project2.getId(),project2.getId(),project2.getName()));
    }

    @Test
    void shouldCreateNewProject() throws IOException, SQLException {

        String requestBody = "name=NewTestProject";
        controller.handle("POST", "/projectCreator", null, requestBody, new ByteArrayOutputStream());

        assertThat(projectDao.listAll())
                .extracting(Project::getName)
                .contains("NewTestProject");
    }
}
