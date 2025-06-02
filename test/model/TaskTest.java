package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void tasksWithSameIdShouldBeEqual() {
        // Создаем первую задачу через анонимный класс
        Task task1 = new Task("Test", "Description", TaskStatus.NEW, TaskType.TASK) {
            @Override
            public String toCsvString() {
                return String.format("%d,%s,%s,%s,%s,",
                        getId(), getType(), getName(), getTaskStatus(), getDescription());
            }
        };
        task1.setId(1);

        // Создаем вторую задачу с тем же id, но разными другими полями
        Task task2 = new Task("Another Test", "Another Description", TaskStatus.IN_PROGRESS, TaskType.TASK) {
            @Override
            public String toCsvString() {
                return String.format("%d,%s,%s,%s,%s,",
                        getId(), getType(), getName(), getTaskStatus(), getDescription());
            }
        };
        task2.setId(1);

        // Проверяем равенство через equals()
        assertTrue(task1.equals(task2), "Задачи с одинаковым id должны быть равны");

        // Дополнительно проверяем через assertEquals (чтобы убедиться, что тест проходит)
        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }
}
