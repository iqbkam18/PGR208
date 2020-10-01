package no.kristiania.project.management.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpMessage {
    private static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static String readLine(InputStream inputStream) throws IOException {
        int c;
        StringBuilder statusLine = new StringBuilder();

        while ((c = inputStream.read()) != -1) {
            if (c == '\r') {
                inputStream.read();
                break;
            }

            statusLine.append((char) c);
        }
        logger.debug(statusLine.toString());

        return statusLine.toString(); //Support for norwegian characters
    }

    static Map<String, String> readHeaders(InputStream inputStream) throws IOException {
        String headerLine;
        Map<String,String> headers = new HashMap<>();
        while(!(headerLine = readLine(inputStream)).isBlank()){
            int colPosition = headerLine.indexOf(":");
            headers.put(headerLine.substring(0, colPosition).trim().toLowerCase(),
                    headerLine.substring(colPosition + 1).trim());
        }
        return headers;
    }

    static String readBody(Map<String, String> headers, InputStream inputStream) throws IOException {
        if (headers.containsKey("content-length")) {
            StringBuilder body = new StringBuilder();
            for (int i = 0; i < Integer.parseInt(headers.get("content-length")); i++) {
                body.append((char) inputStream.read());
            }
            return URLDecoder.decode(body.toString(),UTF_8);
        } else {
            return null;
        }
    }
}
