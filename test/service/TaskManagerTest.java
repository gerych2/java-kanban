package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    abstract T createManager();

    @BeforeEach
    void setup() {
        manager = createManager();
    }

    @Test
    void shouldAddAndRetrieveTask() {
        Task task = new Task("Task1", "desc", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 6, 1, 8, 0));
        manager.addTask(task);
        Task saved = manager.getTaskById(task.getId());

        assertEquals(task.getName(), saved.getName());
        assertEquals(task.getDescription(), saved.getDescription());
        assertEquals(task.getStartTime(), saved.getStartTime());
        assertEquals(task.getDuration(), saved.getDuration());
    }

    @Test
    void shouldLinkSubtaskToEpicAndUpdateEpicFields() {
        Epic epic = new Epic("Epic1", "desc");
        manager.addEpic(epic);

        Subtask sub = new Subtask("Sub1", "desc", TaskStatus.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 6, 2, 10, 0), epic.getId());
        manager.addSubtask(sub);

        Epic loaded = (Epic) manager.getTaskById(epic.getId());

        assertEquals(Duration.ofMinutes(60), loaded.getDuration());
        assertEquals(sub.getStartTime(), loaded.getStartTime());
        assertEquals(sub.getEndTime(), loaded.getEndTime());
    }

    @Test
    void epicStatusShouldBeNewWhenAllSubtasksNew() {
        Epic epic = new Epic("Epic", "desc");
        manager.addEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "desc", TaskStatus.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 6, 2, 9, 0), epic.getId());
        Subtask sub2 = new Subtask("Sub2", "desc", TaskStatus.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 6, 2, 10, 0), epic.getId());

        manager.addSubtask(sub1);
        manager.addSubtask(sub2);

        Epic loaded = (Epic) manager.getTaskById(epic.getId());
        assertEquals(TaskStatus.NEW, loaded.getStatus());
    }

    @Test
    void epicStatusShouldBeDoneWhenAllSubtasksDone() {
        Epic epic = new Epic("Epic", "desc");
        manager.addEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "desc", TaskStatus.DONE, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 6, 2, 9, 0), epic.getId());
        Subtask sub2 = new Subtask("Sub2", "desc", TaskStatus.DONE, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 6, 2, 10, 0), epic.getId());

        manager.addSubtask(sub1);
        manager.addSubtask(sub2);

        Epic loaded = (Epic) manager.getTaskById(epic.getId());
        assertEquals(TaskStatus.DONE, loaded.getStatus());
    }

    @Test
    void epicStatusShouldBeInProgressWhenMixedStatuses() {
        Epic epic = new Epic("Epic", "desc");
        manager.addEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "desc", TaskStatus.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 6, 2, 9, 0), epic.getId());
        Subtask sub2 = new Subtask("Sub2", "desc", TaskStatus.DONE, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 6, 2, 10, 0), epic.getId());

        manager.addSubtask(sub1);
        manager.addSubtask(sub2);

        Epic loaded = (Epic) manager.getTaskById(epic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, loaded.getStatus());
    }

    @Test
    void shouldNotCrashOnEmptyHistory() {
        List<Task> history = manager.getHistory();
        assertTrue(history.isEmpty());
    }
}
