package java.tracker.manager.history;

import java.tracker.model.Task;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) return;

        Task snapshot = task.clone();
        history.add(snapshot);

        if (history.size() > 10) {
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
