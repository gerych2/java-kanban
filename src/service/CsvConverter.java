package service;

import model.*;

public class CsvConverter {

    public static Task taskFromString(String value) {
        String[] fields = value.split(",");

        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];

        return switch (type) {
            case TASK -> {
                Task task = new Task(name, description, status) {
                    @Override
                    public TaskType getType() {
                        return TaskType.TASK;
                    }
                };
                task.setId(id);
                yield task;
            }
            case EPIC -> {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setTaskStatus(status);
                yield epic;
            }
            case SUBTASK -> {
                int epicId = Integer.parseInt(fields[5]);
                Subtask subtask = new Subtask(name, description, status, epicId);
                subtask.setId(id);
                yield subtask;
            }
        };
    }

    public static String toCsvString(Task task) {
        return String.format("%d,%s,%s,%s,%s,",
                task.getId(), task.getType(), task.getName(), task.getTaskStatus(), task.getDescription());
    }

    public static String toCsvString(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,",
                epic.getId(), epic.getType(), epic.getName(), epic.getTaskStatus(), epic.getDescription());
    }

    public static String toCsvString(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%d",
                subtask.getId(), subtask.getType(), subtask.getName(), subtask.getTaskStatus(),
                subtask.getDescription(), subtask.getEpicId());
    }
}
