package model;

public class Subtask extends Task {


    private int idEpic;

    public Subtask(String nameOfSubtask, String description, Epic epic) {
        super(nameOfSubtask, description);
        idEpic = epic.getId();
    }

    public Subtask(String nameOfSubtask, String description, TaskStatus status) {
        super(nameOfSubtask, description);
        this.setStatus(status);

    }

    public Subtask(String nameOfSubtask, String description, TaskStatus status, int subtaskId) {
        this(nameOfSubtask, description, status);
        this.setId(subtaskId);

    }

    public Subtask(String nameOfSubtask, String description, TaskStatus status, int subtaskId, Epic epic) {
        super(nameOfSubtask, description, status);
        this.setId(subtaskId);
        idEpic = epic.getId();

    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Epic epic) {
        this.idEpic = epic.getId();
    }

}
