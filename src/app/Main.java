package app;

import model.*;
import service.*;

public class Main {
    public static void main(String[] args) {
        Task task1 = new Task("Переезд", "Упаковать вещи", TaskStatus.NEW) {
            @Override
            public TaskType getType() {
                return TaskType.TASK;
            }
        };
        task1.setId(1);

        Task task2 = new Task("Покупка продуктов", "Купить хлеб и молоко", TaskStatus.NEW) {
            @Override
            public TaskType getType() {
                return TaskType.TASK;
            }
        };
        task2.setId(2);

        // Используем CsvConverter
        System.out.println(CsvConverter.toCsvString(task1));
        System.out.println(CsvConverter.toCsvString(task2));
    }
}