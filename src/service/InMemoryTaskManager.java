package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();

    private Map<Integer, Epic> epics = new HashMap<>();

    private Map<Integer, Subtask> subtasks = new HashMap<>();

    private HistoryManager historyManager;

    InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public boolean addNewTask(Task newTask) {
        if (tasks.containsValue(newTask)) {
            return false;
        } else {
            tasks.put(newTask.getId(), newTask);
            return true;
        }

    }

    @Override
    public boolean addNewEpic(Epic newEpic) {
        if (epics.containsValue(newEpic)) {
            return false;
        } else {
            epics.put(newEpic.getId(), newEpic);
            return true;
        }

    }

    @Override
    public boolean addNewSubtask(Subtask newSubtask) {

        int foundId = newSubtask.getIdEpic();

        boolean relevantEpic = epics.get(foundId).getListOfSubtask().contains(newSubtask);
        if (relevantEpic) {
            return false;
        } else {
            subtasks.put(newSubtask.getId(), newSubtask);
            epics.get(foundId).getListOfSubtask().add(newSubtask);
            return true;
        }
    }

    @Override
    public Map<Integer, Task> showUpTask() {
        return tasks;
    }

    @Override
    public Map<Integer, Epic> showUpEpic() {
        return epics;
    }

    @Override
    public Map<Integer, Subtask> showUpSubtask() {
        return subtasks;
    }


    @Override
    public void removeAllTask() {
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void removeAllSubtask() {
        for (int id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
        for (Integer id : epics.keySet()) {
            epics.get(id).getListOfSubtask().clear();
            epics.get(id).checkStatus();
        }
    }

    @Override
    public void removeAllEpic() {
        for (int id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeTaskById(int id) {

        Task foundTask = tasks.get(id);
        if (foundTask != null) {
            historyManager.remove(id);
            tasks.remove(id);

        }

    }

    @Override
    public void removeEpicById(int id) {
        List<Subtask> deletedSubtasks = new ArrayList<>();
        Epic foundEpic = epics.get(id);
        if (foundEpic != null) {
            historyManager.remove(id);
            epics.remove(id);
            for (Integer idSubtask : subtasks.keySet()) {
                if (subtasks.get(idSubtask).getIdEpic() == id) {
                    historyManager.remove(idSubtask);
                    deletedSubtasks.add(subtasks.get(idSubtask));

                }
            }
            for (Subtask subtask : deletedSubtasks) {
                subtasks.remove(subtask.getId());
            }


        }
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask foundSubtask = subtasks.get(id);
        if (foundSubtask != null) {
            historyManager.remove(id);
            subtasks.remove(id);
            int idEpic = foundSubtask.getIdEpic();
            epics.get(idEpic).getListOfSubtask().remove(foundSubtask);

        }

    }


    @Override
    public Task searchTaskById(int id) {
        Task foundTask = tasks.get(id);
        if (foundTask != null)
            historyManager.add(foundTask);
        return foundTask;

    }

    @Override
    public Epic searchEpicById(int id) {
        Epic foundEpic = epics.get(id);
        if (foundEpic != null) {
            historyManager.add(foundEpic);

        }
        return foundEpic;

    }

    @Override
    public Subtask searchSubtaskById(int id) {
        Subtask foundSubtask = subtasks.get(id);
        if (foundSubtask != null) {
            historyManager.add(foundSubtask);

        }
        return foundSubtask;

    }

    @Override
    public Collection<Subtask> listSubtaskOfEpic(Epic foundEpic) {
        return foundEpic.getListOfSubtask();
    }

    @Override
    public void updateTask(Task oldTask, Task newTask) {
        int id = oldTask.getId();
        newTask.setId(id);
        tasks.replace(id, newTask);
    }

    @Override
    public void updateSubtask(Subtask oldSubtask, Subtask newSubtask) {
        int id = oldSubtask.getId();
        newSubtask.setId(id);
        subtasks.replace(id, newSubtask);
        epics.get(oldSubtask.getIdEpic()).getListOfSubtask().remove(oldSubtask);
        epics.get(oldSubtask.getIdEpic()).getListOfSubtask().add(newSubtask);
        int idForEpic = oldSubtask.getIdEpic();
        newSubtask.setIdEpic(searchEpicById(idForEpic));
        epics.get(idForEpic).checkStatus();

    }

    @Override
    public void updateEpic(Epic oldEpic, Epic newEpic) {
        int id = oldEpic.getId();
        epics.replace(id, newEpic);
        Collection<Subtask> newListOfSubtask = oldEpic.getListOfSubtask();
        newEpic.setListOfSubtask(newListOfSubtask);
        newEpic.checkStatus();
    }

    @Override
    public Collection<Task> getHistory() {

        return historyManager.getHistory();

    }

}
