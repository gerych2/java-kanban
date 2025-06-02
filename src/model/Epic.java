package model;

import java.util.List;
import java.util.ArrayList;

public class Epic extends Task {

    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW, TaskType.EPIC);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtask(Integer subtaskId) {
        if (subtaskId != null && subtaskId.equals(getId())) {
            throw new IllegalArgumentException("Эпик не может содержать себя как сабтаск");
        }
        subtaskIds.add(subtaskId);
    }

    public void removeSubtask(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void clearSubtasks() {
        subtaskIds.clear();
    }

    @Override
    public Epic clone() {
        Epic clone = (Epic) super.clone();
        clone.subtaskIds.addAll(this.subtaskIds);
        return clone;
    }

    @Override
    public String toCsvString() {
        return String.format("%d,%s,%s,%s,%s,", getId(), getType(), getName(), getTaskStatus(), getDescription());
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", taskStatus=" + getTaskStatus() +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
