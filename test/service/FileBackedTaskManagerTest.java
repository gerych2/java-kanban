package service;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void shouldSaveAndLoadTasksCorrectly() throws IOException {
        File tempFile = File.createTempFile("tasks", ".csv");

        // Создаем менеджер через фабричный метод
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);

        // Создаём обычную задачу через анонимный класс
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW) {
            @Override
            public TaskType getType() {
                return TaskType.TASK;
            }
        };
        task.setId(1);
        manager.addTask(task);

        Epic epic = new Epic("Epic 1", "Epic Description");
        epic.setId(2);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", TaskStatus.NEW, epic.getId());
        subtask.setId(3);
        manager.addSubtask(subtask);

        // Загружаем менеджер из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> tasks = loadedManager.getTasks();
        List<Epic> epics = loadedManager.getEpics();
        List<Subtask> subtasks = loadedManager.getSubtasks();

        assertEquals(1, tasks.size(), "Должна быть 1 задача");
        assertEquals(1, epics.size(), "Должен быть 1 эпик");
        assertEquals(1, subtasks.size(), "Должна быть 1 подзадача");

        assertEquals(task.getId(), tasks.get(0).getId());
        assertEquals(epic.getId(), epics.get(0).getId());
        assertEquals(subtask.getId(), subtasks.get(0).getId());

        Files.deleteIfExists(tempFile.toPath());
    }
}