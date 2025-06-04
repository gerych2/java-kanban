package app;

import model.*;
import service.*;

public class Main {
    public static void main(String[] args) {
        // Создаем SimpleTask вместо анонимного класса
        SimpleTask task1 = new SimpleTask("Переезд", "Упаковать вещи", TaskStatus.NEW);
        task1.setId(1);

        SimpleTask task2 = new SimpleTask("Покупка продуктов", "Купить хлеб и молоко", TaskStatus.NEW);
        task2.setId(2);

        System.out.println(task1.toCsvString());
        System.out.println(task2.toCsvString());
    }
}
