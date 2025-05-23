package model;

import java.util.Objects;

public class Task {

    private Integer id;

    private String name;

    private TaskStatus status;

    private String description;

    public Task(String nameOfTask, String description) {
        this(nameOfTask, description, TaskStatus.NEW, createNewId());
    }

    public Task(String nameOFTask, String description, TaskStatus status) {
        this(nameOFTask, description, status, createNewId());
    }


    public Task(String nameOFTask, String description, TaskStatus status, int taskId) {
        this.name = nameOFTask;
        this.description = description;
        this.status = status;
        this.id = taskId;
    }

    private static int currentId = 0;

    private static int createNewId() {
        currentId++;
        return currentId;
    }


    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String toString() {
        String result = "name={" + this.name + "} id={" + this.id + "} description={" + this.description + "} Status={" +
                this.status + "}";
        return result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}