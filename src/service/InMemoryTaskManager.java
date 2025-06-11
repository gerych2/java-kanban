package service;

import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;
import model.TaskType;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks = new HashMap<>();

    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    protected int nextId = 1;

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>((t1, t2) -> {
        if (t1.getStartTime() == null && t2.getStartTime() == null) {
            return t1.getId().compareTo(t2.getId());
        }
        if (t1.getStartTime() == null) {
            return 1;
        }
        if (t2.getStartTime() == null) {
            return -1;
        }
        return t1.getStartTime().compareTo(t2.getStartTime());
    });

    protected int generateId() {
        return nextId++;
    }

    protected void addToPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    protected void removeFromPrioritizedTasks(Task task) {
        prioritizedTasks.remove(task);
    }

    protected boolean hasTimeOverlap(Task task) {
        if (task.getStartTime() == null || task.getDuration() == null) {
            return false;
        }

        LocalDateTime taskEnd = task.getEndTime();
        return prioritizedTasks.stream()
                .filter(t -> t.getStartTime() != null && t.getDuration() != null)
                .anyMatch(t -> {
                    LocalDateTime tEnd = t.getEndTime();
                    return !(taskEnd.isBefore(t.getStartTime()) || task.getStartTime().isAfter(tEnd));
                });
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public void addTask(Task task) {
        if (task == null) return;
        if (hasTimeOverlap(task)) {
            throw new IllegalArgumentException("Task overlaps with existing tasks");
        }
        task.setId(generateId());
        tasks.put(task.getId(), task);
        addToPrioritizedTasks(task);
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void updateTask(Task task) {
        if (task == null || !tasks.containsKey(task.getId())) return;
        Task oldTask = tasks.get(task.getId());
        removeFromPrioritizedTasks(oldTask);
        if (hasTimeOverlap(task)) {
            addToPrioritizedTasks(oldTask);
            throw new IllegalArgumentException("Task overlaps with existing tasks");
        }
        tasks.put(task.getId(), task);
        addToPrioritizedTasks(task);
    }

    @Override
    public void deleteTask(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            removeFromPrioritizedTasks(task);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeAllTasks() {
        for (Task task : tasks.values()) {
            removeFromPrioritizedTasks(task);
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic == null) return;
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null || !epics.containsKey(epic.getId())) return;
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
        updateEpicTime(epic);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            historyManager.remove(id);
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
        }
    }

    @Override
    public void removeAllEpics() {
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask == null) return;
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) return;
        if (hasTimeOverlap(subtask)) {
            throw new IllegalArgumentException("Subtask overlaps with existing tasks");
        }
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtask(subtask.getId());
        addToPrioritizedTasks(subtask);
        updateEpicStatus(epic);
        updateEpicTime(epic);
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null || !subtasks.containsKey(subtask.getId())) return;
        Subtask oldSubtask = subtasks.get(subtask.getId());
        removeFromPrioritizedTasks(oldSubtask);
        if (hasTimeOverlap(subtask)) {
            addToPrioritizedTasks(oldSubtask);
            throw new IllegalArgumentException("Subtask overlaps with existing tasks");
        }
        subtasks.put(subtask.getId(), subtask);
        addToPrioritizedTasks(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            removeFromPrioritizedTasks(subtask);
            historyManager.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(id);
                updateEpicStatus(epic);
                updateEpicTime(epic);
            }
        }
    }

    @Override
    public void removeAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            removeFromPrioritizedTasks(subtask);
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        List<Subtask> result = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                result.add(subtasks.get(subtaskId));
            }
        }
        return result;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask == null) continue;

            TaskStatus status = subtask.getTaskStatus();
            if (status != TaskStatus.NEW) {
                allNew = false;
            }
            if (status != TaskStatus.DONE) {
                allDone = false;
            }
        }

        if (allDone) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else if (allNew) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private void updateEpicTime(Epic epic) {
        List<Subtask> epicSubtasks = getEpicSubtasks(epic.getId());
        epic.updateTimeFields(epicSubtasks);
    }
}
