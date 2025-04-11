import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks  = new HashMap<>();
    private int nextId = 1;

    public int geteratedID(){
        return nextId++;
    }

    public ArrayList<Task> getAllTask(){
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpic(){
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks(){
        return new ArrayList<>(subtasks.values());
    }

    public void removeAllTask(){
        tasks.clear();
    }

    public void removeAllEpics(){
        epics.clear();
    }

    public void removeAllSubtasks(){
        subtasks.clear();
        for (Epic epic : epics.values()){
            epic.getSubtaskIds().clear();
            updateEpicStatus(epic);
        }
    }

    public Task getTaskId(int id){
        return tasks.get(id);
    }

    public Epic getEpicId(int id){
        return epics.get(id);
    }

    public Subtask getSubtaskId(int id){
        return subtasks.get(id);
    }

    public void addTask(Task task){
        task.setId(geteratedID());
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic){
        epic.setId(geteratedID());
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(geteratedID());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicID());
        if (epic != null) {
            epic.addSubtask(subtask.getId());
            updateEpicStatus(epic);
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epics.get(subtask.getEpicID()));
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicID());
            if (epic != null) {
                epic.remove(id);
                updateEpicStatus(epic);
            }
        }
    }

    public ArrayList<Subtask> getSubtasksOfEpic(int epicId) {
        ArrayList<Subtask> result = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                result.add(subtasks.get(subtaskId));
            }
        }
        return result;
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        for (int id : subtaskIds) {
            TaskStatus status = subtasks.get(id).getTaskStatus();
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
}
