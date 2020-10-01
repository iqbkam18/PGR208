package no.kristiania.project.management.database.controllers.views;

import no.kristiania.project.management.database.dao.CookiesDao;
import no.kristiania.project.management.database.dao.WorkersTasksViewDao;
import no.kristiania.project.management.database.tabels.Cookie;
import no.kristiania.project.management.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

import static no.kristiania.project.management.database.controllers.http.AbstractDatabaseHttpController.configureCookie;

/**
 * This Controller is connecting tables Workers and Tasks
 * and displays Workers and projects they participate in
 */
public class CookieBasedWorkerTasksViewController extends AbstractDatabaseViewController {
    private final WorkersTasksViewDao workerTaskViewDao;
    private final CookiesDao cookiesDao;

    public CookieBasedWorkerTasksViewController(WorkersTasksViewDao workerTaskViewDao, CookiesDao cookiesDao) {
        this.workerTaskViewDao = workerTaskViewDao;
        this.cookiesDao = cookiesDao;
    }

    private static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    @Override
    public void handle(String requestAction, String requestPath, Map<String, String> query, String requestBody, OutputStream outputStream) throws IOException {
        try {
            int statusCode = 200;
            String contentType = "text/html";
            configureCookie(query.get("cookie"));
            Cookie cookie = cookiesDao.retrieve(query.get("cookie"));
            String body = getBody(cookie.getWorkerID());
            int contentLength = body.getBytes(StandardCharsets.UTF_8).length;

            outputStream.write(("HTTP/1.1 " + statusCode + " \r\n" +
                    "Content-type:" + contentType + "; charset=utf-8\r\n" +
                    "Content-length: " + contentLength + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    body).getBytes());

        } catch (IOException | SQLException e) {
            logger.error("While handling request {}", requestPath, e);
            int status = 500;
            String message = e.toString();
            outputStream.write(("HTTP/1.1 " + status + " \r\n" +
                    "Content-type:" + "text/html" + "; charset=utf-8\r\n" +
                    "Content-length: " + message.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    message).getBytes());
        }
    }

    @Override
    protected String getBody(long id) throws SQLException {
        return "<link rel=\"stylesheet\" href=\"style.css\">" +
                "<table border='solid 1px'>" +
                "<tr>" +
                "<th>Name</th>" +
                "<th>LastName</th>" +
                "<th>Address</th>" +
                "<th>Description</th>" +
                "<th>Status</th>" +
                "</tr>" + workerTaskViewDao.listTasksForOneWorker(id).stream()
                .map(p -> String.format("<tr>" +
                        "<td> %s</td>" +
                        "<td> %s</td>" +
                        "<td> %s</td>" +
                        "<td> %s</td>" +
                        "<td> %s</td>" +
                        "" +
                        "</tr>", p.getName(), p.getLastName(), p.getAddress(), p.getDescription(), p.getStatus()))
                .collect(Collectors.joining("")) + "</table>" +
                "<a href=\".\">Return to front page</a>";
    }
}
