package test.java.tracker.manager;

import org.junit.jupiter.api.Test;
import java.tracker.manager.history.HistoryManager;
import java.tracker.manager.task.Managers;
import java.tracker.manager.task.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void shouldReturnInitializedTaskManager() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager, "Менеджер задач должен быть создан");
    }

    @Test
    void shouldReturnInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Менеджер истории должен быть создан");
    }
}
