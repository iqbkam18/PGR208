package no.kristiania.project.management.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HttpClientTest {
    @Test
    void shouldReturn200() throws IOException {
        HttpClient httpClient = new HttpClient("urlecho.appspot.com", "/echo", 80);
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertEquals(200, httpClientResponse.getStatusCode());
    }

    @Test
    void shouldReturn401() throws IOException {
        HttpClient httpClient = new HttpClient("urlecho.appspot.com", "/echo?status=401", 80);
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertEquals(401, httpClientResponse.getStatusCode());
    }

    @Test
    void shouldReturnContentLength() throws IOException {
        HttpClient httpClient = new HttpClient("urlecho.appspot.com", "/echo?status=200&Content-Type=text%2Fhtml&body=Hello%20world!", 80);
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertEquals("12", httpClientResponse.getHeader("Content-Length"));
    }
    @Test
    void shouldReturnNull() throws IOException {
        HttpClient httpClient = new HttpClient("urlecho.appspot.com", "/echo?status=200&Content-Type=text%2Fhtml", 80);
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertNull(httpClientResponse.getHeader("body"));
    }

    @Test
    void shouldReturnServer() throws IOException {
        HttpClient httpClient = new HttpClient("urlecho.appspot.com", "/echo?status=200&Content-Type=text%2Fhtml&body=Hello%20world!", 80);
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertEquals("Google Frontend", httpClientResponse.getHeader("Server"));
    }

    @Test
    void shouldReturnBody() throws IOException {
        HttpClient httpClient = new HttpClient("urlecho.appspot.com", "/echo?status=200&Content-Type=text%2Fhtml&body=Hello%20world!", 80);
        HttpClientResponse httpClientResponse = httpClient.getResponse();
        assertEquals("Hello world!", httpClientResponse.getBody());
    }
}