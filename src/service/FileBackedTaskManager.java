package service;

import model.*;
import java.io.*;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Метод для автосохранения
    protected void save() {
        try (FileWriter writer = new FileWriter(file)) {
            // Заголовок
            writer.write("id,type,name,status,description,epic\n");

            // Сохраняем все задачи
            for (Task task : getTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла", e);
        }
    }

    // Метод сериализации задачи в строку
    private String toString(Task task) {
        String type = (task instanceof Epic) ? "EPIC" :
                (task instanceof Subtask) ? "SUBTASK" : "TASK";
        String epicId = (task instanceof Subtask) ? String.valueOf(((Subtask) task).getEpicId()) : "";
        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                type,
                task.getName(),
                task.getTaskStatus(),
                task.getDescription(),
                epicId);
    }

    // Статический метод загрузки менеджера из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Пропускаем заголовок
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                Task task = fromString(line);
                switch (getTypeFromLine(line)) {
                    case "TASK" -> manager.tasks.put(task.getId(), task);
                    case "EPIC" -> manager.epics.put(task.getId(), (Epic) task);
                    case "SUBTASK" -> {
                        Subtask subtask = (Subtask) task;
                        manager.subtasks.put(subtask.getId(), subtask);
                        Epic epic = manager.epics.get(subtask.getEpicId());
                        if (epic != null) {
                            epic.addSubtask(subtask.getId());
                        }
                    }
                }
                manager.nextId = Math.max(manager.nextId, task.getId() + 1);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке файла", e);
        }

        return manager;
    }

    private static String getTypeFromLine(String line) {
        String[] fields = line.split(",");
        return fields[1];
    }

    // Метод для создания задачи из строки
    private static Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);
        String description = fields[4];

        switch (type) {
            case "TASK" -> {
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            }
            case "EPIC" -> {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setTaskStatus(status);
                return epic;
            }
            case "SUBTASK" -> {
                int epicId = Integer.parseInt(fields[5]);
                Subtask subtask = new Subtask(name, description, status, epicId);
                subtask.setId(id);
                return subtask;
            }
            default -> throw new IllegalArgumentException("Неизвестный тип: " + type);
        }
    }

    // Переопределяем все модифицирующие методы
    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }
}
