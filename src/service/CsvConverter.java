package service;

import model.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvConverter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static Task taskFromString(String value) {
        String[] fields = value.split(",");

        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];
        Duration duration = fields[5].equals("null") ? null : Duration.ofMinutes(Long.parseLong(fields[5]));
        LocalDateTime startTime = fields[6].equals("null") ? null : LocalDateTime.parse(fields[6], formatter);

        return switch (type) {
            case TASK -> {
                Task task = new Task(name, description, status) {
                    @Override
                    public TaskType getType() {
                        return TaskType.TASK;
                    }
                };
                task.setId(id);
                task.setDuration(duration);
                task.setStartTime(startTime);
                yield task;
            }
            case EPIC -> {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setTaskStatus(status);
                epic.setDuration(duration);
                epic.setStartTime(startTime);
                epic.setEndTime(fields[7].equals("null") ? null : LocalDateTime.parse(fields[7], formatter));
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
        return String.format("%d,%s,%s,%s,%s,%s,%s,",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getTaskStatus(),
                task.getDescription(),
                task.getDuration() != null ? task.getDuration().toMinutes() : "null",
                task.getStartTime() != null ? task.getStartTime().format(formatter) : "null"
        );
    }

    public static String toCsvString(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                epic.getId(),
                epic.getType(),
                epic.getName(),
                epic.getTaskStatus(),
                epic.getDescription(),
                epic.getDuration() != null ? epic.getDuration().toMinutes() : "null",
                epic.getStartTime() != null ? epic.getStartTime().format(formatter) : "null",
                epic.getEndTime() != null ? epic.getEndTime().format(formatter) : "null"
        );
    }

    public static String toCsvString(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%d",
                subtask.getId(),
                subtask.getType(),
                subtask.getName(),
                subtask.getTaskStatus(),
                subtask.getDescription(),
                subtask.getDuration() != null ? subtask.getDuration().toMinutes() : "null",
                subtask.getStartTime() != null ? subtask.getStartTime().format(formatter) : "null",
                subtask.getEpicId()
        );
    }
}
