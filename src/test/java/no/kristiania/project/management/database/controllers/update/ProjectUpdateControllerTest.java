package no.kristiania.project.management.database.controllers.update;

import no.kristiania.project.management.database.dao.ProjectDao;
import no.kristiania.project.management.database.dao.ProjectDaoTest;
import no.kristiania.project.management.database.dao.WorkerDaoTest;
import no.kristiania.project.management.database.tabels.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectUpdateControllerTest {
    private ProjectDao projectDao;
    private ProjectUpdateController projectUpdateController;


    @BeforeEach
    void setUp() {
        projectDao = new ProjectDao(WorkerDaoTest.createDataSource());
        projectUpdateController = new ProjectUpdateController(projectDao);
    }

    @Test
    void shouldUpdateProjectFromDatabase() throws SQLException {

        Project project = ProjectDaoTest.randomProject();

        projectDao.insert(project);

        assertThat(projectUpdateController.getBody())
                .contains(String.format("<option name = '%s' value='%s' id='%s'>%s</option>", project.getId(), project.getId(), project.getId(),
                        project.getName()));

        Map<String, String> query = new HashMap<>();
        query.put("name", "newName");
        query.put("ID", String.valueOf(project.getId()));
        projectUpdateController.update(query);

        //We expect that new values in task are going to be equal to parameters of query map
        project.setName("newName");


        assertThat(projectUpdateController.getBody())
                .contains(String.format("<option name = '%s' value='%s' id='%s'>%s</option>", project.getId(), project.getId(), project.getId(),
                        project.getName()));
    }

}