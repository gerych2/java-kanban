package service;

import model.Task;
import java.util.List;

public interface HistoryManager {

    void add(Task task);           // Добавить задачу в историю

    void remove(int id);           // Удалить задачу из истории

    List<Task> getHistory();

}
