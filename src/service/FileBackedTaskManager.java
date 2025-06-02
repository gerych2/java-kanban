package service;

import model.*;
import java.io.*;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    private FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // пропускаем заголовок
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                Task task = CsvConverter.taskFromString(line);

                switch (task.getType()) {
                    case TASK -> manager.tasks.put(task.getId(), task);
                    case EPIC -> manager.epics.put(task.getId(), (Epic) task);
                    case SUBTASK -> {
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

    protected void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getTasks()) {
                writer.write(CsvConverter.taskToString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(CsvConverter.taskToString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(CsvConverter.taskToString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла", e);
        }
    }

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
