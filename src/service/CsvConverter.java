package service;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class CsvConverter {

    public static Task taskFromString(String value) {
        String[] fields = value.split(",");

        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];
        Duration duration = fields[5].isEmpty() ? null : Duration.ofMinutes(Long.parseLong(fields[5]));
        LocalDateTime startTime = fields[6].isEmpty() ? null : LocalDateTime.parse(fields[6]);

        return switch (type) {
            case TASK -> {
                Task task = new Task(name, description, status);
                task.setId(id);
                task.setDuration(duration);
                task.setStartTime(startTime);
                yield task;
            }
            case EPIC -> {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setTaskStatus(status);
                yield epic;
            }
            case SUBTASK -> {
                int epicId = Integer.parseInt(fields[7]);
                Subtask subtask = new Subtask(name, description, status, epicId);
                subtask.setId(id);
                subtask.setDuration(duration);
                subtask.setStartTime(startTime);
                yield subtask;
            }
        };
    }

    public static String toCsvString(Task task) {
        String durationStr = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "";
        String startTimeStr = task.getStartTime() != null ? task.getStartTime().toString() : "";
        return String.format("%d,%s,%s,%s,%s,%s,%s,",
                task.getId(), task.getType(), task.getName(), task.getTaskStatus(), task.getDescription(), durationStr, startTimeStr);
    }

    public static String toCsvString(Epic epic) {
        String durationStr = epic.getDuration() != null ? String.valueOf(epic.getDuration().toMinutes()) : "";
        String startTimeStr = epic.getStartTime() != null ? epic.getStartTime().toString() : "";
        return String.format("%d,%s,%s,%s,%s,%s,%s,",
                epic.getId(), epic.getType(), epic.getName(), epic.getTaskStatus(), epic.getDescription(), durationStr, startTimeStr);
    }

    public static String toCsvString(Subtask subtask) {
        String durationStr = subtask.getDuration() != null ? String.valueOf(subtask.getDuration().toMinutes()) : "";
        String startTimeStr = subtask.getStartTime() != null ? subtask.getStartTime().toString() : "";
        return String.format("%d,%s,%s,%s,%s,%s,%s,%d",
                subtask.getId(), subtask.getType(), subtask.getName(), subtask.getTaskStatus(),
                subtask.getDescription(), durationStr, startTimeStr, subtask.getEpicId());
    }
}
