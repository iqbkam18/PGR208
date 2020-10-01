package no.kristiania.project.management.database.controllers.http;

import no.kristiania.project.management.database.dao.WorkerDao;
import no.kristiania.project.management.database.tabels.Worker;
import no.kristiania.project.management.server.HttpController;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller used for adding and displaying workers in/from table Workers
 */
public class WorkerHttpController extends AbstractDatabaseHttpController implements HttpController {

    private final WorkerDao workerDao;

    public WorkerHttpController(WorkerDao workerDao) {
        this.workerDao = workerDao;
    }


    protected void insertData(Map<String, String> query) throws SQLException {
        Worker worker = new Worker();
        worker.setName(query.get("name"));
        worker.setLastName(query.get("lastName"));
        worker.setAddress(query.get("address"));
        workerDao.insert(worker);
    }

    public String getBody() throws SQLException {
        return workerDao.listAll().stream()
                .map(p -> String.format("<option name = '%s' value = '%s' id='%s'>%s</option>",
                        p.getId(),p.getId(), p.getId(), (p.getName() + " " + p.getLastName() + " - " + p.getAddress())))
                .collect(Collectors.joining(""));
    }
}
