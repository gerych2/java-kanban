package tracker.manager;

import tracker.model.Task;
import tracker.model.Epic;
import tracker.model.Subtask;

import java.util.List;

public interface TaskManager {

    // Методы для обычных задач
    void addTask(Task task);
    Task getTask(int id);
    List<Task> getTasks();
    void updateTask(Task task);
    void deleteTask(int id);
    void removeAllTasks();

    // Методы для эпиков
    void addEpic(Epic epic);
    Epic getEpic(int id);
    List<Epic> getEpics();
    void updateEpic(Epic epic);
    void deleteEpic(int id);
    void removeAllEpics();

    // Методы для подзадач
    void addSubtask(Subtask subtask);
    Subtask getSubtask(int id);
    List<Subtask> getSubtasks();
    void updateSubtask(Subtask subtask);
    void deleteSubtask(int id);
    void removeAllSubtasks();

    // Получить все подзадачи эпика
    List<Subtask> getEpicSubtasks(int epicId);

    // Получить историю просмотров задач
    List<Task> getHistory();
}
