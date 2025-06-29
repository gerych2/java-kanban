package service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Subtask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;

    private final Gson gson;

    public SubtasksHandler(TaskManager manager, Gson gson) {
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
            getAllSubtasks(exchange);
        } else {
            getSubtaskById(exchange, query);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        Subtask subtask = gson.fromJson(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Subtask.class);
        if (subtask.getId() == 0) {
            manager.addSubtask(subtask);
            sendText(exchange, "Subtask added", 201);
        } else {
            manager.updateSubtask(subtask);
            sendText(exchange, "Subtask updated", 201);
        }
    }

    private void handleDelete(HttpExchange exchange, String query) throws IOException {
        if (query == null) {
            manager.removeAllSubtasks();
            sendText(exchange, "All subtasks removed", 200);
        } else {
            int id = parseId(query);
            manager.deleteSubtask(id);
            sendText(exchange, "Subtask deleted", 200);
        }
    }

    private void getAllSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> subtasks = manager.getSubtasks();
        sendText(exchange, gson.toJson(subtasks), 200);
    }

    private void getSubtaskById(HttpExchange exchange, String query) throws IOException {
        int id = parseId(query);
        Subtask subtask = manager.getSubtask(id);
        if (subtask != null) {
            sendText(exchange, gson.toJson(subtask), 200);
        } else {
            sendNotFound(exchange, "Subtask not found");
        }
    }
}
