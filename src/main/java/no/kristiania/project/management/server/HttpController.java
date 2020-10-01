package no.kristiania.project.management.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface HttpController {
    void handle(String requestPath, String path, Map<String, String> query, String body, OutputStream outputStream) throws IOException;
}
