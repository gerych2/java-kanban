package test.java.tracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.tracker.manager.history.InMemoryHistoryManager;
import java.tracker.model.Task;
import java.tracker.model.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager = new InMemoryHistoryManager();


    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldAddTaskToHistory() {
        Task task = new Task("Task", "Desc", TaskStatus.NEW);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История должна быть не пуста");
        assertEquals(1, history.size(), "Должна быть одна задача в истории");
    }

    @Test
    void historyShouldNotExceedTenTasks() {
        for (int i = 0; i < 15; i++) {
            Task task = new Task("Task" + i, "Desc" + i, TaskStatus.NEW);
            task.setId(i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size(), "История должна содержать не более 10 задач");
    }
}