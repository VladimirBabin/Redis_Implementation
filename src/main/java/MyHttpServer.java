import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;

public class MyHttpServer {

    Storage storage = new Storage();

    public static void main(String[] args) throws Exception {
        MyHttpServer myHttpServer = new MyHttpServer();
        myHttpServer.setServer();

    }

    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    public void setServer() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8888), 0);
        server.createContext("/redis", new MyHttpHandler());
        server.setExecutor(threadPoolExecutor);
        System.out.println("Server started at port 8888");
        server.start();
    }

    private class MyHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestParamValue = null;



            if ("GET".equals(exchange.getRequestMethod())) {
                requestParamValue = handleGetRequest(exchange);
            } else if ("POST".equals(exchange.getRequestMethod())) {
                requestParamValue = handlePostRequest(exchange);
            }
            System.out.println(requestParamValue);
            handleResponse(exchange, requestParamValue);
        }

        private String handleGetRequest(HttpExchange exchange) {
            String uriString = exchange.getRequestURI().toString();
            String result = null;
            if (uriString.startsWith("/redis/get")) {
                result = (String) storage.get(uriString.split(Pattern.quote("?"))[1]);
                System.out.println(result);
            } else if (uriString.startsWith("/redis/set")) {
                String[] keyWithValue = uriString.split(Pattern.quote("?"))[1].split("=");
                result = storage.set(keyWithValue[0], keyWithValue[1]);
                System.out.println(result);
            } else if (uriString.startsWith("/redis/delete")) {
                result = (String) storage.delete(uriString.split(Pattern.quote("?"))[1]);
                System.out.println(result);
            } else if (uriString.startsWith("/redis/keys")) {
                Set<String> setOfKeys = (Set<String>) storage.keys();
                StringBuilder stringBuilder = new StringBuilder();
                for (String key : setOfKeys) {
                    System.out.println(key);
                    stringBuilder.append(key);
                    stringBuilder.append("\n\r");
                }
                result = stringBuilder.toString();
            }
            return result;
        }

        private String handlePostRequest(HttpExchange exchange) {
            return exchange.getRequestURI().toString();
        }

        private void handleResponse(HttpExchange exchange, String requestParamValue) throws IOException {
            OutputStream os = exchange.getResponseBody();
            StringBuilder htmlBuilder = new StringBuilder();


            htmlBuilder.append("<h1>");
            htmlBuilder.append("Server answer: ");
            htmlBuilder.append(requestParamValue);
            htmlBuilder.append("</h1>");

            String htmlResponse = htmlBuilder.toString();

            exchange.sendResponseHeaders(200, htmlBuilder.length());

            os.write(htmlResponse.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();
        }
    }
}