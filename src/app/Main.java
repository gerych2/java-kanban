package app;

import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        Task taskA = new Task("Clean room", "Vacuum and dust", TaskStatus.NEW);
        Task taskB = new Task("Do laundry", "Wash and fold clothes", TaskStatus.NEW);

        Epic epicAlpha = new Epic("Vacation Planning", "Plan trip to mountains");
        Epic epicBeta = new Epic("Birthday Event", "Organize birthday party");

        Subtask subAlpha1 = new Subtask("Book hotel", "Reserve a room for 3 nights", TaskStatus.NEW,
                1001, epicAlpha);
        Subtask subAlpha2 = new Subtask("Pack bags", "Prepare clothes and equipment", TaskStatus.NEW,
                1002, epicAlpha);
        Subtask subAlpha3 = new Subtask("Buy snacks", "Purchase food for road", TaskStatus.NEW,
                1003, epicAlpha);

        TaskManager taskManager = Managers.getDefault();

        taskManager.addNewTask(taskA);
        taskManager.addNewEpic(epicAlpha);
        taskManager.addNewSubtask(subAlpha1);
        taskManager.addNewTask(taskB);
        taskManager.addNewEpic(epicBeta);
        taskManager.addNewSubtask(subAlpha2);
        taskManager.addNewSubtask(subAlpha3);

        taskManager.searchEpicById(epicAlpha.getId());
        taskManager.searchSubtaskById(subAlpha2.getId());
        taskManager.searchTaskById(taskB.getId());
        taskManager.searchTaskById(taskA.getId());
        taskManager.searchTaskById(taskA.getId());
        taskManager.searchEpicById(epicAlpha.getId());
        taskManager.searchEpicById(epicBeta.getId());

        System.out.println(taskManager.getHistory());
        taskManager.removeTaskById(taskA.getId());
        System.out.println(taskManager.getHistory());
        taskManager.removeEpicById(epicAlpha.getId());
        System.out.println(taskManager.getHistory());
    }
}
