package no.kristiania.project.management.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class HttpClientResponse extends HttpMessage {
    private String statusLine;
    private Map<String, String> headers;
    private String body;

    public HttpClientResponse(InputStream inputStream) throws IOException {
        this.statusLine = readLine(inputStream);

        headers = readHeaders(inputStream);

        body = readBody(headers, inputStream);
    }

    public String getHeader(String key) {
        return headers.get(key.toLowerCase());
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public int getStatusCode() {
        String[] splitResponseData = statusLine.split(" ");
        return Integer.parseInt(splitResponseData[1]);
    }

}
