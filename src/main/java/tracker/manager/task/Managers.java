package java.tracker.manager.task;

import java.tracker.manager.history.HistoryManager;
import java.tracker.manager.history.InMemoryHistoryManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
