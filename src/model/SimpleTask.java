package model;

public class SimpleTask extends Task {

    public SimpleTask(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    @Override
    public TaskType getType() {
        return TaskType.TASK;
    }

    @Override
    public String toCsvString() {
        return String.format("%d,%s,%s,%s,%s,", getId(), getType(), getName(), getTaskStatus(), getDescription());
    }
}
