package model;

import java.util.ArrayList;
import java.util.Collection;

public class Epic extends Task {

    private Collection<Subtask> listOfSubtask;

    private TaskStatus status;

    public Epic(String nameOfEpic, String description) {
        super(nameOfEpic, description);
        status = TaskStatus.NEW;
        listOfSubtask = new ArrayList<>();
    }

    public Epic(String nameOfEpic, String description, int id) {
        super(nameOfEpic, description);
        status = TaskStatus.NEW;
        listOfSubtask = new ArrayList<>();
        this.setId(id);
    }

    @Override
    public void setStatus(TaskStatus status) {
        System.out.println("Нельзя менять статус Epic'a");
    }

    @Override
    public TaskStatus getStatus() {
        return this.status;
    }

    public Collection<Subtask> getListOfSubtask() {
        return listOfSubtask;
    }

    public void setListOfSubtask(Collection<Subtask> listOfSubtask) {
        this.listOfSubtask = listOfSubtask;
    }

    @Override
    public String toString() {

        String result = "name={" + this.getName() + "} id={" + this.getId() + "} description={" + this.getDescription() +
                "} Status={" + this.getStatus() + "}";
        return result;

    }

    public void checkStatus() {
        Collection<Subtask> checkList = this.listOfSubtask;
        int size = checkList.size();


        if (size == 0) {

            this.status = TaskStatus.NEW;
            return;

        }
        int doneOfStatus = 0;
        int newOfStatus = 0;

        for (Subtask subtask : checkList) {

            if (subtask.getStatus().equals(TaskStatus.DONE)) {
                doneOfStatus++;
            }
            if (subtask.getStatus().equals(TaskStatus.NEW)) {
                newOfStatus++;

            }

        }
        if (doneOfStatus == size) {
            this.status = TaskStatus.DONE;
        } else if (newOfStatus == size) {
            this.status = TaskStatus.NEW;
        } else {
            this.status = TaskStatus.IN_PROGRESS;

        }
    }
}
