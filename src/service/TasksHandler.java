package service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();

        try {
            switch (method) {
                case "GET" -> {
                    if (query == null) {
                        List<Task> tasks = manager.getTasks();
                        sendText(exchange, gson.toJson(tasks), 200);
                    } else {
                        int id = parseId(query);
                        Task task = manager.getTask(id);
                        if (task != null) {
                            sendText(exchange, gson.toJson(task), 200);
                        } else {
                            sendNotFound(exchange, "Task not found");
                        }
                    }
                }
                case "POST" -> {
                    Task task = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Task.class);
                    if (task.getId() == 0) {
                        manager.addTask(task);
                        sendText(exchange, "Task added", 201);
                    } else {
                        manager.updateTask(task);
                        sendText(exchange, "Task updated", 201);
                    }
                }
                case "DELETE" -> {
                    if (query == null) {
                        manager.removeAllTasks();
                        sendText(exchange, "All tasks removed", 200);
                    } else {
                        int id = parseId(query);
                        manager.deleteTask(id);
                        sendText(exchange, "Task deleted", 200);
                    }
                }
                default -> sendNotFound(exchange, "Unsupported method");
            }
        } catch (Exception e) {
            sendServerError(exchange, "Internal server error: " + e.getMessage());
        }
    }

    private int parseId(String query) {
        return Integer.parseInt(query.replaceFirst("id=", ""));
    }
}
