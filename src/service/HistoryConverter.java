package service;

import model.Task;
import service.HistoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryConverter {

    public static String toString(HistoryManager manager) {
        return manager.getHistory().stream()
                .map(task -> String.valueOf(task.getId()))
                .collect(Collectors.joining(","));
    }

    public static List<Integer> fromString(String value) {
        List<Integer> ids = new ArrayList<>();
        if (value == null || value.isBlank()) return ids;

        String[] tokens = value.split(",");
        for (String token : tokens) {
            try {
                ids.add(Integer.parseInt(token.trim()));
            } catch (NumberFormatException ignored) {
            }
        }
        return ids;
    }
}
