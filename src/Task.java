import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus taskStatus;

    public Task(String name, String description, TaskStatus taskStatus){
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public int setId(int id){
        return id;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public TaskStatus setTaskStatus(TaskStatus taskStatus) {
        return taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
