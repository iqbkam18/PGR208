package no.kristiania.project.management.database.controllers.views;

import no.kristiania.project.management.database.dao.WorkersTasksViewDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * This Controller is connecting tables Workers and Tasks
 * and displays Workers and projects they participate in
 */
public class AllWorkersTasksViewController extends AbstractDatabaseViewController {
    private final WorkersTasksViewDao workerTaskViewDao;

    public AllWorkersTasksViewController(WorkersTasksViewDao workerTaskViewDao) {
        this.workerTaskViewDao = workerTaskViewDao;
    }

    @Override
    protected String getBody(long id) throws SQLException {
        return "<link rel=\"stylesheet\" href=\"style.css\">" +
                "<table>" +
                "<tr>" +
                "<th>Name</th>" +
                "<th>LastName</th>" +
                "<th>Address</th>" +
                "<th>Description</th>" +
                "<th>Status</th>" +
                "</tr>" + workerTaskViewDao.listTasksForWorkers().stream()
                .map(p -> String.format("<tr>" +
                        "<td> %s</td>" +
                        "<td> %s</td>" +
                        "<td> %s</td>" +
                        "<td> %s</td>" +
                        "<td> %s</td>" +
                        "" +
                        "</tr>",p.getName(), p.getLastName(), p.getAddress(),p.getDescription(),p.getStatus()))
                .collect(Collectors.joining("")) + "</table>";
    }


}
