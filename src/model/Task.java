package model;

import java.util.Objects;

public class Task {

    private int id;
    private String name;
    private TaskStatus status;
    private String description;

    private static int currentId = 0;

    public Task(String nameOfTask, String description) {
        this(nameOfTask, description, TaskStatus.NEW, generateId());
    }

    public Task(String nameOfTask, String description, TaskStatus status) {
        this(nameOfTask, description, status, generateId());
    }

    public Task(String nameOfTask, String description, TaskStatus status, int taskId) {
        this.name = nameOfTask;
        this.description = description;
        this.status = status;
        this.id = taskId;
    }

    private static int generateId() {
        return ++currentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Task task)) return false;
        return id == task.id &&
                Objects.equals(name, task.name) &&
                status == task.status &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description);
    }

    @Override
    public String toString() {
        return String.format("Task{id=%d, name='%s', status=%s, description='%s'}",
                id, name, status, description);
    }
}
