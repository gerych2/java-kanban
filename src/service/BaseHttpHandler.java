package service;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler {

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] responseBytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    protected void sendNotFound(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 404);
    }

    protected void sendConflict(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 409);
    }

    protected void sendServerError(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, message, 500);
    }

    protected int parseId(String query) {
        return Integer.parseInt(query.replaceFirst("id=", ""));
    }
}
