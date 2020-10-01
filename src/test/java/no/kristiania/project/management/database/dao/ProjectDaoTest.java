package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.Project;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static no.kristiania.project.management.database.dao.WorkerDaoTest.createDataSource;
import static no.kristiania.project.management.database.dao.WorkerDaoTest.pickOne;
import static org.assertj.core.api.Assertions.assertThat;

public class ProjectDaoTest {
    private ProjectDao projectDao;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = createDataSource();//Reuse createDataSource from WorkerDaoTest
        projectDao = new ProjectDao(dataSource);
    }


    @Test
    public void shouldListSavedProjects() throws SQLException {
        Project project = randomProject();
        projectDao.insert(project);

        assertThat(projectDao.listAll())
                .extracting(Project::getName)
                .contains(project.getName());
    }

    public static Project randomProject() {
        Project project = new Project();
        project.setName(pickOne(new String[]{"Building", "Renovation", "Destruction", "Cleaning up"})); //Reuse pickOne from WorkerDaoTest
        return project;
    }

    @Test
     void shouldRetrieveSavedProjects() throws SQLException {
        Project project = randomProject();
        projectDao.insert(project);
        assertThat(project).hasNoNullFieldsOrProperties();
        assertThat(projectDao.retrieve(project.getId())).isEqualToComparingFieldByField(project);
    }


}
