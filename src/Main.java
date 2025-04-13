import model.Task;
import model.Epic;
import model.Subtask;
import model.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание обычных задач
        Task task1 = new Task("Переезд", "Собрать коробки", TaskStatus.NEW);
        Task task2 = new Task("Покупка продуктов", "Купить продукты на неделю", TaskStatus.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        // Создание эпиков и подзадач
        Epic epic1 = new Epic("Организация вечеринки", "Подготовка ко дню рождения");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Купить торт", "Выбрать шоколадный торт", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Пригласить гостей", "Создать список гостей", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic epic2 = new Epic("Ремонт кухни", "Обновить кухню полностью");
        manager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Покрасить стены", "Купить краску и покрасить стены", TaskStatus.NEW, epic2.getId());
        manager.addSubtask(subtask3);

        // Печать всех задач
        System.out.println("Все задачи:");
        System.out.println(manager.getTask());

        System.out.println("\nВсе эпики:");
        System.out.println(manager.getEpic());

        System.out.println("\nВсе подзадачи:");
        System.out.println(manager.getSubtask());

        // Обновление статусов подзадач
        subtask1.setTaskStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);

        subtask2.setTaskStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask2);

        // Проверка пересчёта статуса эпика
        System.out.println("\nЭпики после обновления статусов подзадач:");
        System.out.println(manager.getEpic());

        // Удаление задачи и эпика
        manager.deleteTask(task1.getId());
        manager.deleteEpic(epic2.getId());

        System.out.println("\nВсе задачи после удаления:");
        System.out.println(manager.getTask());

        System.out.println("\nВсе эпики после удаления:");
        System.out.println(manager.getEpic());

        System.out.println("\nВсе подзадачи после удаления:");
        System.out.println(manager.getSubtask());
    }
}
