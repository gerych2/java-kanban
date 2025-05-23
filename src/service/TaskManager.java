package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Collection;
import java.util.Map;

public interface TaskManager {
    boolean addNewTask(Task newTask);

    boolean addNewEpic(Epic newEpic);

    boolean addNewSubtask(Subtask newSubtask);

    Map<Integer, Task> showUpTask();

    Map<Integer, Epic> showUpEpic();

    Map<Integer, Subtask> showUpSubtask();

    void removeAllTask();

    void removeAllSubtask();

    void removeAllEpic();

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    Task searchTaskById(int id);

    Epic searchEpicById(int id);

    Subtask searchSubtaskById(int id);

    void updateTask(Task oldTask, Task newTask);

    void updateSubtask(Subtask oldSubtask, Subtask newSubtask);

    void updateEpic(Epic oldEpic, Epic newEpic);

    Collection<Subtask> listSubtaskOfEpic(Epic foundEpic);

    Collection<Task> getHistory();


}
