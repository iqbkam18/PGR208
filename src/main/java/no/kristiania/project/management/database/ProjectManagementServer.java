package no.kristiania.project.management.database;

import no.kristiania.project.management.database.controllers.http.*;
import no.kristiania.project.management.database.controllers.update.CookieWorkerTaskUpdateController;
import no.kristiania.project.management.database.controllers.update.ProjectUpdateController;
import no.kristiania.project.management.database.controllers.update.TaskUpdateController;
import no.kristiania.project.management.database.controllers.views.*;
import no.kristiania.project.management.database.dao.*;
import no.kristiania.project.management.server.HttpServer;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ProjectManagementServer {

    private HttpServer server;

    public ProjectManagementServer(int port) throws IOException {
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader("task-manager.properties")) {
            properties.load(fileReader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));

        Flyway.configure().dataSource(dataSource).load().migrate();


        server = new HttpServer(port);
        server.setAssetRoot("src/main/resources/project-management");
        server.addController("/projectMembers", new WorkerHttpController(new WorkerDao(dataSource)));
        server.addController("/taskCreator", new TaskHttpController(new TaskDao(dataSource)));
        server.addController("/projectCreator", new ProjectHttpController(new ProjectDao(dataSource)));
        server.addController("/workerToProject", new WorkerToProjectHttpController(new ProjectWorkerDao(dataSource)));
        server.addController("/taskToWorker", new WorkerTaskHttpController(new WorkerTaskDao(dataSource)));
        server.addController("/taskToWorkerView", new AllWorkersTasksViewController(new WorkersTasksViewDao(dataSource)));
        server.addController("/listWorkerTasks", new OneWorkerTasksViewController(new WorkersTasksViewDao(dataSource)));
        server.addController("/updateTask.html", new TaskEditorViewController(new TaskDao(dataSource)));
        server.addController("/saveTaskChanges", new TaskUpdateController(new TaskDao(dataSource)));
        server.addController("/updateProject.html", new ProjectEditorViewController(new ProjectDao(dataSource)));
        server.addController("/saveProjectChanges", new ProjectUpdateController(new ProjectDao(dataSource)));
        server.addController("/updateCookieWorker", new CookieWorkerTaskUpdateController(new CookiesDao(dataSource)));
        server.addController("/cookieWorkerTasks", new CookieBasedWorkerTasksViewController
                (new WorkersTasksViewDao(dataSource), new CookiesDao(dataSource) ));
    }


    public static void main(String[] args) throws IOException {
        new ProjectManagementServer(8080).start();
    }

    private void start() {
        server.startServer();
    }
}
