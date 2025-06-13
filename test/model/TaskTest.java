package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void taskFieldsShouldBeStoredCorrectly() {
        Duration duration = Duration.ofMinutes(90);
        LocalDateTime startTime = LocalDateTime.of(2023, 6, 1, 14, 0);
        Task task = new Task("Test Task", "Description", TaskStatus.NEW, duration, startTime);

        assertEquals("Test Task", task.getName());
        assertEquals("Description", task.getDescription());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals(duration, task.getDuration());
        assertEquals(startTime, task.getStartTime());
        assertEquals(startTime.plus(duration), task.getEndTime());
    }

    @Test
    void getEndTimeShouldReturnNullIfStartTimeOrDurationIsNull() {
        Task taskWithNoTime = new Task("Test", "Test desc", TaskStatus.NEW);
        assertNull(taskWithNoTime.getEndTime());

        Task taskWithStartOnly = new Task("Test", "Test desc", TaskStatus.NEW);
        taskWithStartOnly.setStartTime(LocalDateTime.now());
        assertNull(taskWithStartOnly.getEndTime());

        Task taskWithDurationOnly = new Task("Test", "Test desc", TaskStatus.NEW);
        taskWithDurationOnly.setDuration(Duration.ofMinutes(30));
        assertNull(taskWithDurationOnly.getEndTime());
    }

    @Test
    void equalsAndHashCodeShouldWorkCorrectly() {
        Duration duration = Duration.ofMinutes(30);
        LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 10, 0);
        Task t1 = new Task("A", "B", TaskStatus.NEW, duration, startTime);
        t1.setId(1);

        Task t2 = new Task("A", "B", TaskStatus.NEW, duration, startTime);
        t2.setId(1);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}
