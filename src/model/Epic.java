package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subtaskIds = new ArrayList<>();
    private final TaskStatus status;

    public Epic(String nameOfEpic, String description) {
        super(nameOfEpic, description);
        this.status = TaskStatus.NEW;
    }

    public Epic(String nameOfEpic, String description, int id) {
        super(nameOfEpic, description);
        this.setId(id);
        this.status = TaskStatus.NEW;
    }

    @Override
    public void setStatus(TaskStatus status) {
        System.out.println("Нельзя менять статус Epic'a напрямую");
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    public void addSubtask(int subtaskId) {
        if (subtaskId != this.getId() && !subtaskIds.contains(subtaskId)) {
            subtaskIds.add(subtaskId);
        }
    }


    public void removeSubtask(int subtaskId) {
        subtaskIds.remove((Integer) subtaskId);
    }

    public Collection<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }
}
