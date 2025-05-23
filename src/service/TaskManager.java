package service;

import model.Task;
import model.Epic;
import model.Subtask;
import java.util.List;


public interface TaskManager {

    // Методы для обычных задач
    void addTask(Task task);

    Task getTask(int id);

    boolean addNewTask(Task newTask);

    boolean addNewEpic(Epic newEpic);

    boolean addNewSubtask(Subtask newSubtask);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    Task searchTaskById(int id);

    Epic searchEpicById(int id);

    Subtask searchSubtaskById(int id);

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

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

}
