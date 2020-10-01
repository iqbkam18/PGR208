package no.kristiania.project.management.server;

import no.kristiania.project.management.database.dao.CookiesDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class HttpServer extends HttpMessage {

    private static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    private final ServerSocket serverSocket;
    private String assetRoot = "src/main/resources/default-server";
    private HttpController defaultController = new FileHttpController(this);
    private Map<String, HttpController> controllers = new HashMap<>();
    private CookiesDao cookiesDao;
    public String getAssetRoot() {
        return assetRoot;
    }

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        controllers.put("/echo", new EchoHttpController());
    }


    public void startServer() {
        new Thread(() -> run()).start();
        logger.info("Started on http://localhost:{}", getPort());
    }

    private void run() {
        while (true) {
            try (Socket socket = serverSocket.accept()) {

                String requestLine = HttpMessage.readLine(socket.getInputStream());
                if (requestLine.isBlank()) {
                    continue;
                }
                logger.debug("Handling request: {}", requestLine);
                Map<String, String> headers = readHeaders(socket.getInputStream());
                String body = HttpMessage.readBody(headers, socket.getInputStream());

                String requestAction = requestLine.split(" ")[0];
                String requestTarget = requestLine.split(" ")[1];//        Request-Line   = Method SP Request-URI SP HTTP-Version CRLF
                int positionOfQuestion = requestTarget.indexOf('?');
                String requestPath = positionOfQuestion == -1 ? requestTarget : requestTarget.substring(0, positionOfQuestion);
                Map<String, String> query = getQueryParameters(requestTarget);
                //If web browser that is sending request doesn't have cookie generate it
                //we could have inserted data in database at this point but we choose not to so that server
                //if needed can run independently
                if (!headers.containsKey("cookie")) {
                    String randomCookie = String.valueOf(UUID.randomUUID());
                    String cookie = "Set-cookie: sessionId=" + randomCookie;
                    query.put("cookie", cookie); //Send cookie to database handlers

                } else{
                    query.put("cookie", headers.get("cookie"));//Send cookie to database handlers
                }
                controllers
                        .getOrDefault(requestPath, defaultController)
                        .handle(requestAction, requestPath, query, body, socket.getOutputStream());
                socket.getOutputStream().flush();
                socket.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    static Map<String, String> getQueryParameters(String requestTarget) {
        int indexOfQuestion = requestTarget.indexOf("?");
        if (indexOfQuestion > 0) //If there is question sign
        {
            String query = requestTarget.substring(indexOfQuestion + 1);
            return parseQueryString(query);
        }

        return new HashMap<>();
    }

    public static Map<String, String> parseQueryString(String query) {
        String[] requestParameterSplit = query.split("&");
        Map<String, String> queryParameters = new HashMap<>();
        for (String parameter : requestParameterSplit) {
            int equalsPosition = parameter.indexOf('=');
            //To read norwegian characters and remove + and %20 from body we use URLDecoder
            String parameterName = URLDecoder.decode(parameter.substring(0, equalsPosition), StandardCharsets.UTF_8);
            String parameterValue = URLDecoder.decode(parameter.substring(equalsPosition + 1), StandardCharsets.UTF_8);
            queryParameters.put(parameterName, parameterValue);
        }
        return queryParameters;
    }


    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setAssetRoot(String assetRoot) {
        this.assetRoot = assetRoot;
    }

    public void addController(String requestPath, HttpController controller) {
        controllers.put(requestPath, controller);
    }
}
