package no.kristiania.project.management.database.controllers.update;

import no.kristiania.project.management.server.HttpController;
import no.kristiania.project.management.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

/**
 * This UpdateController has responsibility for editing(updating) data in database
 * It could have been part of AbstractDatabaseHttpController
 * but for better code management and debugging it is class for it self
 */
public abstract class AbstractDatabaseUpdateController implements HttpController {
    private static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    @Override
    public void handle(String requestAction, String requestPath, Map<String, String> query, String requestBody, OutputStream outputStream) throws IOException {
        try {
            if (requestAction.equals("POST")) {
                String currentCookie = query.get("cookie");
                query = HttpServer.parseQueryString(requestBody);
                query.put("cookie",currentCookie);//Send current cookie value to update correct cookie
                update(query);

                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: http://localhost:8080" + query.get("goTo") + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes());
            }
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
     * Based on query and dao for given update controller update data
     * @param query data to used in update statement
     * @throws SQLException in case of wrong sql expression
     */
    protected abstract void update(Map<String, String> query) throws SQLException;
}
