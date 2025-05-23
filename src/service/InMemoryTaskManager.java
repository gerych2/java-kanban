package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();

    private final Map<Integer, Epic> epics = new HashMap<>();

    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public boolean addNewTask(Task newTask) {
        if (newTask == null || tasks.containsKey(newTask.getId())) {
            return false;
        }
        tasks.put(newTask.getId(), newTask);
        return true;
    }

    @Override
    public boolean addNewEpic(Epic newEpic) {
        if (newEpic == null || epics.containsKey(newEpic.getId())) {
            return false;
        }
        epics.put(newEpic.getId(), newEpic);
        return true;
    }

    @Override
    public boolean addNewSubtask(Subtask newSubtask) {
        if (newSubtask == null || subtasks.containsKey(newSubtask.getId())) {
            return false;
        }

        int foundId = newSubtask.getEpicId();
        Epic parentEpic = epics.get(foundId);
        if (parentEpic == null) {
            return false;
        }

        boolean alreadyExists = parentEpic.getSubtaskIds().contains(newSubtask.getId());
        if (alreadyExists) {
            return false;
        }

        subtasks.put(newSubtask.getId(), newSubtask);
        parentEpic.addSubtask(newSubtask.getId());
        updateEpicStatus(parentEpic);
        return true;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void addTask(Task task) {
        if (task != null) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public Task getTask(int id) {
        return tasks.get(id);
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic != null) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subId : epic.getSubtaskIds()) {
                subtasks.remove(subId);
                historyManager.remove(subId);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            for (Integer subId : epic.getSubtaskIds()) {
                subtasks.remove(subId);
                historyManager.remove(subId);
            }
            historyManager.remove(epic.getId());
        }
        epics.clear();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                subtasks.put(subtask.getId(), subtask);
                epic.addSubtask(subtask.getId());
                updateEpicStatus(epic);
            }
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                updateEpicStatus(epic);
            }
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(id);
                updateEpicStatus(epic);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void removeAllSubtasks() {
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic);
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        List<Subtask> list = new ArrayList<>();
        if (epic != null) {
            for (Integer subId : epic.getSubtaskIds()) {
                Subtask sub = subtasks.get(subId);
                if (sub != null) list.add(sub);
            }
        }
        return list;
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        deleteEpic(id); // уже реализовано выше
    }

    @Override
    public void removeSubtaskById(int id) {
        deleteSubtask(id); // уже реализовано выше
    }

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

    private void updateEpicStatus(Epic epic) {
        Collection<Integer> subIds = epic.getSubtaskIds();
        if (subIds.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer subId : subIds) {
            TaskStatus status = subtasks.get(subId).getStatus();
            if (status != TaskStatus.NEW) {
                allNew = false;
            }
            if (status != TaskStatus.DONE) {
                allDone = false;
            }
        }

        if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
