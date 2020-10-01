package no.kristiania.project.management.database.controllers.views;

import no.kristiania.project.management.database.controllers.http.AbstractDatabaseHttpController;
import no.kristiania.project.management.server.HttpController;
import no.kristiania.project.management.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.project.management.database.controllers.http.AbstractDatabaseHttpController.configureCookie;

/**
 * This abstract controller is used for creating views from database (joining multiple tables)
 * getBody method is using id to retrieve row from table
 * It could have been part of AbstractDatabaseHttpController
 * but for better code management and debugging it is class for it self
 */
public abstract class AbstractDatabaseViewController implements HttpController {
    private static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    @Override
    public void handle(String requestAction, String requestPath, Map<String, String> query, String requestBody, OutputStream outputStream) throws IOException {
        try {
            int statusCode = 200;
            String contentType = "text/html";
            String body = getBody(Long.parseLong(query.getOrDefault("ID","-1")));
            int contentLength = body.getBytes(StandardCharsets.UTF_8).length;
            configureCookie(query.get("cookie"));

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

    /**
     * @param id id from database to update
     * @return string formatted HTML expression
     * @throws SQLException in case of wrong sql expression
     */
    protected abstract String getBody(long id) throws SQLException;
}
