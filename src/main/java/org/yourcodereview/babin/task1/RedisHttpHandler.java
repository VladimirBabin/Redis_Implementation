package org.yourcodereview.babin.task1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.regex.Pattern;

public class RedisHttpHandler implements HttpHandler {
    public final String getHttpMethod = "GET";
    public final String postHttpMethod = "POST";
    private Storage storage;

    RedisHttpHandler() {
        if (this.storage == null) {
            this.storage = new Storage();
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestParamValue = null;

        if (getHttpMethod.equals(exchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(exchange);
        } else if (postHttpMethod.equals(exchange.getRequestMethod())) {
            requestParamValue = handlePostRequest(exchange);
        }
        handleResponse(exchange, requestParamValue);
    }

    private String handleGetRequest(HttpExchange exchange) {
        String uriString = exchange.getRequestURI().toString();
        String result = null;
        if (uriString.startsWith("/redis/get")) {
            result = (String) storage.get(uriString.split(Pattern.quote("?"))[1]);
        } else if (uriString.startsWith("/redis/set")) {
            String[] keyWithValue = uriString.split(Pattern.quote("?"))[1].split("=");
            result = storage.set(keyWithValue[0], keyWithValue[1]);
        } else if (uriString.startsWith("/redis/delete")) {
            result = (String) storage.delete(uriString.split(Pattern.quote("?"))[1]);
        } else if (uriString.startsWith("/redis/keys")) {
            Set<String> keys = (Set<String>) storage.keys();
            StringBuilder keysStringBuilder = new StringBuilder();
            for (String key : keys) {
                keysStringBuilder.append(key);
                keysStringBuilder.append("\n\r");
            }
            result = keysStringBuilder.toString();
        } else {
            System.err.println("Not correct input");
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
