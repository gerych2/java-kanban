import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("\nВыберите действие:");
            System.out.println("1. Создать задачу");
            System.out.println("2. Создать эпик");
            System.out.println("3. Создать подзадачу");
            System.out.println("4. Показать все задачи");
            System.out.println("5. Показать все эпики");
            System.out.println("6. Показать все подзадачи");
            System.out.println("7. Обновить задачу");
            System.out.println("8. Удалить задачу");
            System.out.println("9. Выход");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Введите название задачи: ");
                    String taskName = scanner.nextLine();
                    System.out.print("Введите описание задачи: ");
                    String taskDesc = scanner.nextLine();
                    manager.addTask(new Task(taskName, taskDesc, TaskStatus.NEW));
                    break;
                case 2:
                    System.out.print("Введите название эпика: ");
                    String epicName = scanner.nextLine();
                    System.out.print("Введите описание эпика: ");
                    String epicDesc = scanner.nextLine();
                    manager.addEpic(new Epic(epicName, epicDesc));
                    break;
                case 3:
                    System.out.print("Введите название подзадачи: ");
                    String subtaskName = scanner.nextLine();
                    System.out.print("Введите описание подзадачи: ");
                    String subtaskDesc = scanner.nextLine();
                    System.out.print("Введите ID эпика: ");
                    int epicId = Integer.parseInt(scanner.nextLine());
                    manager.addSubtask(new Subtask(subtaskName, subtaskDesc, TaskStatus.NEW, epicId));
                    break;
                case 4:
                    System.out.println(manager.getAllTask());
                    break;
                case 5:
                    System.out.println(manager.getAllEpic());
                    break;
                case 6:
                    System.out.println(manager.getAllSubtasks());
                    break;
                case 7:
                    System.out.print("Введите ID задачи для обновления: ");
                    int updateId = Integer.parseInt(scanner.nextLine());
                    Task taskToUpdate = manager.getTaskId(updateId);
                    if (taskToUpdate != null) {
                        System.out.print("Введите новое название: ");
                        taskToUpdate.name = scanner.nextLine();
                        System.out.print("Введите новое описание: ");
                        taskToUpdate.description = scanner.nextLine();
                        System.out.println("Выберите новый статус: 1.NEW 2.IN_PROGRESS 3.DONE");
                        int statusChoice = Integer.parseInt(scanner.nextLine());
                        if (statusChoice == 1) taskToUpdate.setTaskStatus(TaskStatus.NEW);
                        else if (statusChoice == 2) taskToUpdate.setTaskStatus(TaskStatus.IN_PROGRESS);
                        else if (statusChoice == 3) taskToUpdate.setTaskStatus(TaskStatus.DONE);
                        manager.updateTask(taskToUpdate);
                    } else {
                        System.out.println("Задача не найдена!");
                    }
                    break;
                case 8:
                    System.out.print("Введите ID задачи для удаления: ");
                    int deleteId = Integer.parseInt(scanner.nextLine());
                    manager.deleteTask(deleteId);
                    break;
                case 9:
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
}
