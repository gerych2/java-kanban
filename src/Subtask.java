public class Subtask extends Task{
    private int epicID;

    public Subtask(String name, String description, TaskStatus taskStatus,int epicID){
        super(name, description, taskStatus);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + taskStatus +
                ", epicId=" + epicID +
                '}';
    }
}
