package app;

import model.*;
import service.Managers;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("task1", "descriptionForTask1", TaskStatus.NEW);
        Task task2 = new Task("task2", "descriptionForTask2", TaskStatus.NEW);

        Epic epic1 = new Epic("epic1", "descriptionForEpic1");
        Epic epic2 = new Epic("epic2", "descriptionForEpic2");

        Subtask subtask1 = new Subtask("subtask1", "descriptionForSubtask1", TaskStatus.NEW, 5, epic1);
        Subtask subtask2 = new Subtask("subtask2", "descriptionForSubtask2", TaskStatus.NEW, 6, epic1);
        Subtask subtask3 = new Subtask("subtask3", "descriptionForSubtask3", TaskStatus.NEW, 7, epic1);

        manager.addNewTask(task1);
        manager.addNewTask(task2);

        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);

        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        manager.addNewSubtask(subtask3);

        System.out.println("All tasks: " + manager.getTasks());
        System.out.println("All epics: " + manager.getEpics());
        System.out.println("All subtasks: " + manager.getSubtasks());

        System.out.println("History: " + manager.getHistory());
    }
}
