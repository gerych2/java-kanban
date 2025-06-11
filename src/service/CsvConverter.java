package service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.*;

public class CsvConverter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static String toString(Task task) {
        String[] fields = {
                String.valueOf(task.getId()),
                task.getType().toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                task instanceof Subtask ? String.valueOf(((Subtask) task).getEpicId()) : "",
                task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "",
                task.getStartTime() != null ? task.getStartTime().format(formatter) : ""
        };
        return String.join(",", fields);
    }

    public static Task fromString(String value) {
        String[] fields = value.split(",", -1);
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];

        Duration duration = fields[6].isEmpty() ? null : Duration.ofMinutes(Long.parseLong(fields[6]));
        LocalDateTime startTime = fields[7].isEmpty() ? null : LocalDateTime.parse(fields[7], formatter);

        Task task;
        switch (type) {
            case TASK -> task = new Task(name, description, status, duration, startTime);
            case SUBTASK -> {
                int epicId = Integer.parseInt(fields[5]);
                task = new Subtask(name, description, status, duration, startTime, epicId);
            }
            case EPIC -> task = new Epic(name, description); // duration/startTime will be updated later
            default -> throw new IllegalArgumentException("Unknown task type");
        }

        task.setId(id);
        task.setStatus(status);
        return task;
    }
}
