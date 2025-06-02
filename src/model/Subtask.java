package model;

public class Subtask extends Task {

    private Integer epicId;

    public Subtask(String name, String description, TaskStatus taskStatus, Integer epicId) {
        super(name, description, taskStatus, TaskType.SUBTASK);
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
    public Subtask clone() {
        Subtask clone = (Subtask) super.clone();
        clone.setEpicId(this.epicId);
        return clone;
    }

    @Override
    public String toCsvString() {
        return String.format("%d,%s,%s,%s,%s,%d", getId(), getType(), getName(), getTaskStatus(), getDescription(), epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", taskStatus=" + getTaskStatus() +
                ", epicId=" + epicId +
                '}';
    }
}
