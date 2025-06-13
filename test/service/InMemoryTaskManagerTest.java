package service;

import model.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }

    @Test
    void tasksShouldBeSortedByStartTime() {
        Task t1 = new Task("task1", "desc", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 6, 1, 10, 0));
        Task t2 = new Task("task2", "desc", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 6, 1, 9, 0));
        Task t3 = new Task("task3", "desc", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 6, 1, 11, 0));

        manager.addTask(t1);
        manager.addTask(t2);
        manager.addTask(t3);

        List<Task> sorted = manager.getPrioritizedTasks();
        assertEquals(t2, sorted.get(0));
        assertEquals(t1, sorted.get(1));
        assertEquals(t3, sorted.get(2));
    }

    @Test
    void overlappingTasksShouldThrowException() {
        Task task1 = new Task("T1", "desc", TaskStatus.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 6, 1, 10, 0));
        Task task2 = new Task("T2", "desc", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 6, 1, 10, 30)); // пересекается

        manager.addTask(task1);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> manager.addTask(task2));
        assertTrue(ex.getMessage().toLowerCase().contains("пересекается"));
    }
}
