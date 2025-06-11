package model;

import java.time.format.DateTimeFormatter;

public class Subtask extends Task {

    private Integer epicId;

    public Subtask(String name, String description, TaskStatus taskStatus, Integer epicId) {
        super(name, description, taskStatus);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        if (epicId != null && epicId.equals(getId())) {
            throw new IllegalArgumentException("Сабтаск не может быть своим эпиком");
        }
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", taskStatus=" + getTaskStatus() +
                ", epicId=" + epicId +
                ", duration=" + (getDuration() != null ? getDuration().toMinutes() + "m" : "null") +
                ", startTime=" + (getStartTime() != null ? getStartTime().format(formatter) : "null") +
                ", endTime=" + (getEndTime() != null ? getEndTime().format(formatter) : "null") +
                '}';
    }
}
