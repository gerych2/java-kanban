package service;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import model.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
        loadFromFile();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(file);
    }

    private void loadFromFile() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            List<String> lines = Files.readAllLines(file.toPath());
            boolean isHistory = false;

            for (String line : lines) {
                if (line.isBlank()) {
                    isHistory = true;
                    continue;
                }

                if (!isHistory) {
                    Task task = CsvConverter.fromString(line);
                    int id = task.getId();

                    switch (task.getType()) {
                        case TASK -> tasks.put(id, task);
                        case EPIC -> epics.put(id, (Epic) task);
                        case SUBTASK -> {
                            subtasks.put(id, (Subtask) task);
                            int epicId = ((Subtask) task).getEpicId();
                            Epic epic = epics.get(epicId);
                            if (epic != null) {
                                epicSubtasks.get(epicId).add((Subtask) task);
                            } else {
                                List<Subtask> list = new ArrayList<>();
                                list.add((Subtask) task);
                                epicSubtasks.put(epicId, list);
                            }
                        }
                    }

                    if (id >= nextId) {
                        nextId = id + 1;
                    }
                } else {
                    List<Integer> historyIds = CsvConverter.historyFromString(line);
                    for (int historyId : historyIds) {
                        Task task = tasks.get(historyId);
                        if (task == null) task = subtasks.get(historyId);
                        if (task == null) task = epics.get(historyId);

                        if (task != null) {
                            historyManager.add(task);
                        }
                    }


                }
            }

            // после загрузки всех задач — обновляем время у эпиков
            for (Epic epic : epics.values()) {
                epic.updateTimeFields(epicSubtasks.get(epic.getId()));
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении из файла: " + file.getName(), e);
        }
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks.values()) {
                writer.write(CsvConverter.toString(task));
                writer.newLine();
            }
            for (Epic epic : epics.values()) {
                writer.write(CsvConverter.toString(epic));
                writer.newLine();
            }
            for (Subtask subtask : subtasks.values()) {
                writer.write(CsvConverter.toString(subtask));
                writer.newLine();
            }

            writer.newLine(); // пустая строка
            writer.write(CsvConverter.historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл: " + file.getName(), e);
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}
