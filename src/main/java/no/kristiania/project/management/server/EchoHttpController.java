package no.kristiania.project.management.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

class EchoHttpController implements HttpController {
    @Override
    public void handle(String requestAction, String path, Map<String, String> query, String body, OutputStream outputStream) throws IOException {
        if(requestAction.equals("POST")){
            query = HttpServer.parseQueryString(body);
        }

        String statusCode = query.getOrDefault("status", "200 OK");
        String contentType = query.getOrDefault("content-type", "text/plain");
        String responseBody = query.getOrDefault("body", "Hello world");
        responseBody = URLDecoder.decode(responseBody,UTF_8);
        String location = query.getOrDefault("Location", "");
        outputStream.write(("HTTP/1.1 " + statusCode + " \r\n" +
                "Content-type: " + contentType + "; charset=utf-8" + "\r\n" + //Charset is used to read norwegian characters
                "Content-length: " + responseBody.getBytes().length + "\r\n" +
                "Connection: close\r\n" +
                (location.isEmpty() ? "" : "Location: " + location + "\r\n") +
                "\r\n" +
                responseBody).getBytes());
    }
}
