package service;

import model.Task;
import model.Epic;
import model.Subtask;
import java.util.List;


public interface TaskManager {

    void addTask(Task task);

    Task getTask(int id);

    List<Task> getTasks();

    void updateTask(Task task);

    void deleteTask(int id);

    void removeAllTasks();

    void addEpic(Epic epic);

    Epic getEpic(int id);

    List<Epic> getEpics();

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    void removeAllEpics();

    void addSubtask(Subtask subtask);

    Subtask getSubtask(int id);

    List<Subtask> getSubtasks();

    void updateSubtask(Subtask subtask);

    void deleteSubtask(int id);

    void removeAllSubtasks();

    List<Subtask> getEpicSubtasks(int epicId);

    List<Task> getHistory();
}
