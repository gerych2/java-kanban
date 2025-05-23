package model;

public class Subtask extends Task {

    private int idEpic;

    public Subtask(String nameOfSubtask, String description, Epic epic) {
        super(nameOfSubtask, description);
        setIdEpic(epic);
    }

    public Subtask(String nameOfSubtask, String description, TaskStatus status) {
        super(nameOfSubtask, description);
        setStatus(status);
    }

    public Subtask(String nameOfSubtask, String description, TaskStatus status, int subtaskId) {
        this(nameOfSubtask, description, status);
        setId(subtaskId);
    }

    public Subtask(String nameOfSubtask, String description, TaskStatus status, int subtaskId, Epic epic) {
        super(nameOfSubtask, description, status);
        setId(subtaskId);
        setIdEpic(epic);
    }

    public int getEpicId() {
        return idEpic;
    }

    public void setIdEpic(Epic epic) {
        if (epic != null) {
            this.idEpic = epic.getId();
        }
    }
}
