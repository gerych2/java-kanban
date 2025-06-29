package service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;

    private final Gson gson;

    public EpicsHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();

        try {
            switch (method) {
                case "GET" -> handleGet(exchange, query);
                case "POST" -> handlePost(exchange);
                case "DELETE" -> handleDelete(exchange, query);
                default -> sendNotFound(exchange, "Unsupported method");
            }
        } catch (Exception e) {
            sendServerError(exchange, "Internal server error: " + e.getMessage());
        }
    }

    private void handleGet(HttpExchange exchange, String query) throws IOException {
        if (query == null) {
            getAllEpics(exchange);
        } else {
            getEpicById(exchange, query);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        Epic epic = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Epic.class);
        if (epic.getId() == 0) {
            manager.addEpic(epic);
            sendText(exchange, "Epic added", 201);
        } else {
            manager.updateEpic(epic);
            sendText(exchange, "Epic updated", 201);
        }
    }

    private void handleDelete(HttpExchange exchange, String query) throws IOException {
        if (query == null) {
            manager.removeAllEpics();
            sendText(exchange, "All epics removed", 200);
        } else {
            int id = parseId(query);
            manager.deleteEpic(id);
            sendText(exchange, "Epic deleted", 200);
        }
    }

    private void getAllEpics(HttpExchange exchange) throws IOException {
        List<Epic> epics = manager.getEpics();
        sendText(exchange, gson.toJson(epics), 200);
    }

    private void getEpicById(HttpExchange exchange, String query) throws IOException {
        int id = parseId(query);
        Epic epic = manager.getEpic(id);
        if (epic != null) {
            sendText(exchange, gson.toJson(epic), 200);
        } else {
            sendNotFound(exchange, "Epic not found");
        }
    }
}
