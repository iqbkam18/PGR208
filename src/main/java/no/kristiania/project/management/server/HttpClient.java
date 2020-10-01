package no.kristiania.project.management.server;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpClient {

    private String host;
    private int port;
    private String requestTarget;
    private String body;
    private Map<String, String> headers = new HashMap<>();

    public HttpClient(String host, String requestTarget, int port) {
        this.host = host;
        this.port = port;
        this.requestTarget = requestTarget;
        setRequestHeader("Host", host);
        setRequestHeader("Connection", "close");
    }


    HttpClientResponse getResponse() throws IOException {
        return getResponse("GET");
    }

    HttpClientResponse getResponse(final String httpMethod) throws IOException {
        Socket socket = new Socket(host, port);

        if (body != null) {
                setRequestHeader("Content-length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length));
        }

        String headersString = headers.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\r\n"));

        socket.getOutputStream().write((httpMethod + " " + requestTarget + " HTTP/1.1\r\n" +
                headersString +
                "\r\n\r\n" +
                body).getBytes());
        return new HttpClientResponse(socket.getInputStream());
    }

    public void setRequestHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    public void setBody(String body) {
        this.body = body;
    }
}
