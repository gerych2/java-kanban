package service;

import model.Task;
import java.util.List;

public interface HistoryManager {
    void add(Task task);           // Добавить задачу в историю
    List<Task> getHistory();       // Получить всю историю
}
