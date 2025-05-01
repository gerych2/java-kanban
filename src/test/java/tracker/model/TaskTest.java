package test.java.tracker.model;

import org.junit.jupiter.api.Test;
import java.tracker.model.Task;
import java.tracker.model.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("Test", "Description", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("Another Test", "Another Description", TaskStatus.IN_PROGRESS);
        task2.setId(1);

        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
    }
}
