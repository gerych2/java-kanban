package service;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm");

    // Сериализация задачи в строку
    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getType()).append(",");
        sb.append(task.getName()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");

        if (task instanceof Subtask subtask) {
            sb.append(subtask.getEpicId()).append(",");
        } else {
            sb.append(",");
        }

        sb.append(task.getStartTime() != null ? task.getStartTime().format(FORMATTER) : "").append(",");
        sb.append(task.getDuration() != null ? task.getDuration().toMinutes() : "");

        return sb.toString();
    }

    // Десериализация строки в задачу
    public static Task fromString(String value) {
        String[] fields = value.split(",", -1);
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];

        String startTimeStr = fields.length > 6 && !fields[6].isBlank() ? fields[6] : null;
        String durationStr = fields.length > 7 && !fields[7].isBlank() ? fields[7] : null;

        LocalDateTime startTime = (startTimeStr != null) ? LocalDateTime.parse(startTimeStr, FORMATTER) : null;
        Duration duration = (durationStr != null) ? Duration.ofMinutes(Long.parseLong(durationStr)) : null;

        return switch (type) {
            case TASK -> {
                Task task = new Task(name, description, status, duration, startTime);
                task.setId(id);
                yield task;
            }
            case EPIC -> {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                yield epic;
            }
            case SUBTASK -> {
                int epicId = Integer.parseInt(fields[5]);
                Subtask sub = new Subtask(name, description, status, duration, startTime, epicId);
                sub.setId(id);
                yield sub;
            }
        };
    }

    public static String toCsvHeader() {
        return "id,type,name,status,description,epic,startTime,duration";
    }

    public static String historyToString(HistoryManager manager) {
        return manager.getHistory().stream()
                .map(task -> String.valueOf(task.getId()))
                .collect(Collectors.joining(","));
    }

    public static List<Integer> historyFromString(String value) {
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
