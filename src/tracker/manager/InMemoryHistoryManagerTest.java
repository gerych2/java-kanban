package tracker.manager;

import org.junit.jupiter.api.Test;
import tracker.model.Task;
import tracker.model.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private final HistoryManager historyManager = Managers.getDefaultHistory();

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
