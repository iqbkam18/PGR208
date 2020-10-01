package no.kristiania.project.management.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpServerTest {

    private HttpServer httpServer;

    @BeforeEach
    void setUp() throws IOException {
        httpServer = new HttpServer(0);
        httpServer.startServer();
    }

    @Test
    void shouldReturn200() throws IOException {
        HttpClient httpClient = new HttpClient("localhost", "/echo", httpServer.getPort());
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertEquals(200, httpClientResponse.getStatusCode());
    }

    @Test
    void shouldReturn401() throws IOException {
        HttpClient httpClient = new HttpClient("localhost", "/echo?status=401 ", httpServer.getPort());
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertEquals(401, httpClientResponse.getStatusCode());
    }

    @Test
    void shouldParseRequestParameters() throws IOException {
        HttpClient httpClient = new HttpClient("localhost", "/echo?status=302&Location=http://www.example.com", httpServer.getPort());
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertEquals("http://www.example.com", httpClientResponse.getHeader("Location"));
    }

    @Test
    void shouldReturnBody() throws IOException {
        HttpClient httpClient = new HttpClient("localhost", "/echo?status=302&Location=http://www.example.com&body=Hello+world", httpServer.getPort());
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertEquals("Hello world", httpClientResponse.getBody());
    }

    @Test
    void shouldReturnExtraLongBody() throws IOException {
        HttpClient httpClient = new HttpClient("localhost", "/echo?status=302&Location=http://www.example.com&body=Hello+world!+I+am+http+server.", httpServer.getPort());
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertEquals("Hello world! I am http server.", httpClientResponse.getBody());
    }

    @Test
    void shouldParsePostParameters() throws IOException {
        String formBody = "content-type=text/html&body=foobar";
        HttpClient httpClient = new HttpClient("localhost", "/echo?" + formBody, httpServer.getPort());
        httpClient.setRequestHeader("content-type","application/x-www-form-urlencoded");
        httpClient.setBody(formBody);
        HttpClientResponse httpClientResponse = httpClient.getResponse("POST");
        assertThat(httpClientResponse.getHeader("content-type")).isEqualTo("text/html; charset=utf-8");
        assertThat(httpClientResponse.getBody()).isEqualTo("foobar");
    }

    @Test
    void shouldReadFile() throws IOException {
        try (FileWriter fw = new FileWriter(httpServer.getAssetRoot() + "/testFile.html")) {
            fw.write("This is just test text.");
        }
        HttpClient httpClient = new HttpClient("localhost", "/testFile.html", httpServer.getPort());
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertEquals("This is just test text.", httpClientResponse.getBody());

    }

}