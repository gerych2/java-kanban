package app;

import model.*;
import service.*;

public class Main {
    public static void main(String[] args) {
        // Используем анонимный класс для создания Task
        Task task1 = new Task("Переезд", "Упаковать вещи", TaskStatus.NEW, TaskType.TASK) {
            @Override
            public String toCsvString() {
                return String.format("%d,%s,%s,%s,%s,", getId(), getType(), getName(), getTaskStatus(), getDescription());
            }
        };
        task1.setId(1);

        Task task2 = new Task("Покупка продуктов", "Купить хлеб и молоко", TaskStatus.NEW, TaskType.TASK) {
            @Override
            public String toCsvString() {
                return String.format("%d,%s,%s,%s,%s,", getId(), getType(), getName(), getTaskStatus(), getDescription());
            }
        };
        task2.setId(2);

        System.out.println(task1.toCsvString());
        System.out.println(task2.toCsvString());
    }
}
