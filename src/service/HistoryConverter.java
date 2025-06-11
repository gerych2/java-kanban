package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryConverter {

    public static String toString(HistoryManager historyManager) {
        List<Task> history = historyManager.getHistory();
        return history.stream()
                .map(task -> String.valueOf(task.getId()))
                .collect(Collectors.joining(","));
    }

    public static List<Integer> fromString(String value) {
        List<Integer> ids = new ArrayList<>();
        if (value == null || value.isBlank()) return ids;

        String[] parts = value.split(",");
        for (String part : parts) {
            ids.add(Integer.parseInt(part));
        }
        return ids;
    }
}
