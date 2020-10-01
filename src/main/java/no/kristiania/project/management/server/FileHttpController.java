package no.kristiania.project.management.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

class FileHttpController implements HttpController {

    private HttpServer httpServer;

    public FileHttpController(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    @Override
    public void handle(String requestPath, String path, Map<String, String> query, String body, OutputStream outputStream) throws IOException {
        if("/".equals(path))//No need to enter localhost:8080/index.html. We use "/".equals to avoid nullptr exception --> Yoda conditions
            path = "/index.html";
        File file = new File(httpServer.getAssetRoot() + path);
        String cookie = query.get("cookie"); //Set cookie in headers
        if (file.exists() && !file.isDirectory()) {
            outputStream.write(("HTTP/1.1 200 OK\r\n" +
                    "Content-length: " + file.length() + "\r\n" +
                    "Connection: close\r\n" +
                    (cookie.isEmpty() ? "" : cookie + "\r\n") +
                    "\r\n").getBytes());
            try (FileInputStream fileForTransfer = new FileInputStream(file)) {
                fileForTransfer.transferTo(outputStream);
            }
        } else {
            outputStream.write(("HTTP/1.1 404 Not found\r\n" +
                    "Content-type: text/plain \r\n" +
                    "Content-length: 9\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    "Not found").getBytes());
        }

    }
}
