package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("Test", "Description", TaskStatus.NEW) {
            @Override
            public TaskType getType() {
                return TaskType.TASK;
            }
        };
        task1.setId(1);

        Task task2 = new Task("Another Test", "Another Description", TaskStatus.IN_PROGRESS) {
            @Override
            public TaskType getType() {
                return TaskType.TASK;
            }
        };
        task2.setId(1);

        assertTrue(task1.equals(task2), "Задачи с одинаковым id должны быть равны");
        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }
}