package service;

import model.*;

public class CsvConverter {

    public static String taskToString(Task task) {
        return task.toCsvString();
    }

    public static Task taskFromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];

        switch (type) {
            case TASK -> {
                Task task = new Task(name, description, status, TaskType.TASK) {
                    @Override
                    public String toCsvString() {
                        return String.format("%d,%s,%s,%s,%s,", getId(), getType(), getName(), getTaskStatus(), getDescription());
                    }
                };
                task.setId(id);
                return task;
            }
            case EPIC -> {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setTaskStatus(status);
                return epic;
            }
            case SUBTASK -> {
                int epicId = Integer.parseInt(fields[5]);
                Subtask subtask = new Subtask(name, description, status, epicId);
                subtask.setId(id);
                return subtask;
            }
            default -> throw new IllegalArgumentException("Неизвестный тип: " + type);
        }
    }
}

