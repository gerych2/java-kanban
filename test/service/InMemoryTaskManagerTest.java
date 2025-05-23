package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    void setup() {
        manager = Managers.getDefault();
    }

    @Test
    void shouldAddAndGetTaskById() {
        Task task = new Task("Test Task", "Description", TaskStatus.NEW, 1);
        manager.addNewTask(task);

        Task found = manager.getTaskById(1);
        assertNotNull(found);
        assertEquals("Test Task", found.getName());
    }

    @Test
    void shouldReturnTasksList() {
        Task task1 = new Task("T1", "D1", TaskStatus.NEW, 1);
        Task task2 = new Task("T2", "D2", TaskStatus.NEW, 2);
        manager.addNewTask(task1);
        manager.addNewTask(task2);

        List<Task> tasks = manager.getTasks();
        assertEquals(2, tasks.size());
    }

    @Test
    void shouldAddEpicAndSubtasks() {
        Epic epic = new Epic("Epic1", "Epic Desc", 10);
        manager.addNewEpic(epic);

        Subtask sub1 = new Subtask("S1", "SD1", TaskStatus.NEW, 11, epic);
        Subtask sub2 = new Subtask("S2", "SD2", TaskStatus.NEW, 12, epic);
        manager.addNewSubtask(sub1);
        manager.addNewSubtask(sub2);

        List<Subtask> subs = manager.getEpicSubtasks(epic.getId());
        assertEquals(2, subs.size());
    }

    @Test
    void shouldTrackHistory() {
        Task task = new Task("H", "track", TaskStatus.NEW, 100);
        manager.addNewTask(task);

        Task found = manager.getTaskById(100);
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(found, history.get(0));
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task("DeleteMe", "x", TaskStatus.NEW, 200);
        manager.addNewTask(task);

        manager.removeTaskById(200);
        assertNull(manager.getTaskById(200));
    }
}
