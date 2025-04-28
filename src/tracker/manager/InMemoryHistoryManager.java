package tracker.manager;

import tracker.model.Task;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        Task copy = copyTask(task); // ✨ создаём копию!
        history.add(copy);

        if (history.size() > 10) {
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    private Task copyTask(Task task) {
        if (task instanceof Subtask) {
            Subtask original = (Subtask) task;
            Subtask copy = new Subtask(original.getName(), original.getDescription(), original.getTaskStatus(), original.getEpicId());
            copy.setId(original.getId());
            return copy;
        } else if (task instanceof Epic) {
            Epic original = (Epic) task;
            Epic copy = new Epic(original.getName(), original.getDescription());
            copy.setId(original.getId());
            // В эпиках мы не копируем сабтаски здесь, только базовую информацию
            return copy;
        } else {
            Task copy = new Task(task.getName(), task.getDescription(), task.getTaskStatus());
            copy.setId(task.getId());
            return copy;
        }
    }
}
