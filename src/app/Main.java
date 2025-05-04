package app;

import service.TaskManager;
import service.Managers;
import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;


public class Main {
    public static void main(String[] args) {
        // Создаём менеджер задач через утилитарный класс Managers
        TaskManager manager = Managers.getDefault();

        // Создаём обычные задачи
        Task task1 = new Task("Переезд", "Упаковать вещи", TaskStatus.NEW);
        Task task2 = new Task("Покупка продуктов", "Купить хлеб и молоко", TaskStatus.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        // Создаём эпик с двумя подзадачами
        Epic epic1 = new Epic("Организация вечеринки", "Купить торт и пригласить гостей");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Купить торт", "Выбрать торт в магазине", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Пригласить гостей", "Создать чат в мессенджере", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Создаём ещё один эпик без подзадач
        Epic epic2 = new Epic("Ремонт квартиры", "Ремонт кухни");
        manager.addEpic(epic2);

        // Печатаем все задачи
        printAllTasks(manager);

        // Имитация просмотров задач
        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        manager.getEpic(epic2.getId());
        manager.getTask(task2.getId());
        manager.getEpic(epic1.getId()); // Повторный просмотр эпика

        // Печатаем историю после просмотров
        System.out.println("\nИстория просмотров:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Все задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }

        System.out.println("\nВсе эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);

            for (Subtask subtask : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + subtask);
            }
        }

        System.out.println("\nВсе подзадачи:");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
    }
}
