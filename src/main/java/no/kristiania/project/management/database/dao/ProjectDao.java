package no.kristiania.project.management.database.dao;

import no.kristiania.project.management.database.tabels.Project;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProjectDao extends AbstractDao<Project> {
    public ProjectDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void mapToStatement(Project project, PreparedStatement statement) throws SQLException {
        statement.setString(1, project.getName());
    }

    @Override
    protected Project mapFromResultSet(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getLong("id"));
        project.setName(rs.getString("name"));
        return project;
    }

    public List<Project> listAll() throws SQLException {
        return listAll("select * from projects", -1);
    }

    public void insert(Project project) throws SQLException {
        long id = insert(project, "insert into projects (name) values (?)");
        project.setId(id);
    }

    public Project retrieve(Long id) throws SQLException {
        return retrieve(id, "select * from projects where id = ?");
    }

    public void update(Project projectToInsert) throws SQLException {
        update(projectToInsert,
                "update projects set name =(?) where id = (?)");
    }

    @Override
    protected void mapToUpdateStatement(Project project, PreparedStatement statement) throws SQLException {
        statement.setString(1, project.getName());
        statement.setLong(2, project.getId());
    }
}
