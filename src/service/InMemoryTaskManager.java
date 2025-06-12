package service;

import model.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int nextId = 1;

    protected final Map<Integer, Task> tasks = new HashMap<>();

    protected final Map<Integer, Epic> epics = new HashMap<>();

    protected final Map<Integer, Subtask> subtasks = new HashMap<>();

    protected final Map<Integer, List<Subtask>> epicSubtasks = new HashMap<>();

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Task::getId)
    );

    private int generateId() {
        return nextId++;
    }

    // Добавление задач
    @Override
    public void addTask(Task task) {
        if (task.getStartTime() != null && isIntersecting(task)) {
            throw new IllegalArgumentException("Задача пересекается с другой по времени");
        }
        task.setId(generateId());
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        epicSubtasks.put(epic.getId(), new ArrayList<>());
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask.getStartTime() != null && isIntersecting(subtask)) {
            throw new IllegalArgumentException("Подзадача пересекается с другой по времени");
        }
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        epicSubtasks.get(subtask.getEpicId()).add(subtask);
        prioritizedTasks.add(subtask);
        updateEpicFields(subtask.getEpicId());
    }

    // Обновление задач
    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) return;
        prioritizedTasks.remove(tasks.get(task.getId()));
        if (task.getStartTime() != null && isIntersecting(task)) {
            prioritizedTasks.add(tasks.get(task.getId()));
            throw new IllegalArgumentException("Обновлённая задача пересекается с другой по времени");
        }
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) return;
        epics.put(epic.getId(), epic);
        updateEpicFields(epic.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) return;
        prioritizedTasks.remove(subtasks.get(subtask.getId()));
        if (subtask.getStartTime() != null && isIntersecting(subtask)) {
            prioritizedTasks.add(subtasks.get(subtask.getId()));
            throw new IllegalArgumentException("Обновлённая подзадача пересекается с другой по времени");
        }
        subtasks.put(subtask.getId(), subtask);
        List<Subtask> list = epicSubtasks.get(subtask.getEpicId());
        list.removeIf(st -> st.getId() == subtask.getId());
        list.add(subtask);
        prioritizedTasks.add(subtask);
        updateEpicFields(subtask.getEpicId());
    }

    // Удаление
    @Override
    public void removeTask(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            List<Subtask> list = epicSubtasks.remove(id);
            if (list != null) {
                for (Subtask sub : list) {
                    subtasks.remove(sub.getId());
                    prioritizedTasks.remove(sub);
                    historyManager.remove(sub.getId());
                }
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            List<Subtask> list = epicSubtasks.get(subtask.getEpicId());
            list.removeIf(st -> st.getId() == id);
            prioritizedTasks.remove(subtask);
            updateEpicFields(subtask.getEpicId());
            historyManager.remove(id);
        }
    }

    // Получение
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) task = subtasks.get(id);
        if (task == null) task = epics.get(id);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    protected void updateEpicFields(int epicId) {
        Epic epic = epics.get(epicId);
        List<Subtask> list = epicSubtasks.get(epicId);
        epic.updateTimeFields(list);
        epic.updateStatus(list);
    }

    private boolean isIntersecting(Task newTask) {
        LocalDateTime newStart = newTask.getStartTime();
        LocalDateTime newEnd = newTask.getEndTime();
        if (newStart == null || newEnd == null) return false;
        return prioritizedTasks.stream()
                .filter(task -> task.getStartTime() != null && task.getId() != newTask.getId())
                .anyMatch(existing -> {
                    LocalDateTime start = existing.getStartTime();
                    LocalDateTime end = existing.getEndTime();
                    return start.isBefore(newEnd) && newStart.isBefore(end);
                });
    }

    @Override
    public Task getTask(int id) {
        return tasks.get(id);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTask(int id) {
        removeTask(id);
    }

    @Override
    public void removeAllTasks() {
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
        }
        tasks.clear();
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
    public void deleteEpic(int id) {
        removeEpic(id);
    }

    @Override
    public void removeAllEpics() {
        for (int id : epics.keySet()) {
            historyManager.remove(id);
            List<Subtask> subs = epicSubtasks.get(id);
            if (subs != null) {
                for (Subtask s : subs) {
                    historyManager.remove(s.getId());
                    subtasks.remove(s.getId());
                    prioritizedTasks.remove(s);
                }
            }
        }
        epics.clear();
        epicSubtasks.clear();
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
    public void deleteSubtask(int id) {
        removeSubtask(id);
    }

    @Override
    public void removeAllSubtasks() {
        for (int id : subtasks.keySet()) {
            Subtask st = subtasks.get(id);
            historyManager.remove(id);
            prioritizedTasks.remove(st);
            List<Subtask> list = epicSubtasks.get(st.getEpicId());
            if (list != null) list.removeIf(s -> s.getId() == id);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            updateEpicFields(epic.getId());
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        return epicSubtasks.containsKey(epicId) ? new ArrayList<>(epicSubtasks.get(epicId)) : List.of();
    }

}
