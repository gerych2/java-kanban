package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void subtaskFieldsShouldBeStoredCorrectly() {
        Duration duration = Duration.ofMinutes(45);
        LocalDateTime startTime = LocalDateTime.of(2024, 2, 10, 12, 0);
        int epicId = 100;

        Subtask subtask = new Subtask("Subtask Title", "Subtask Desc", TaskStatus.IN_PROGRESS,
                duration, startTime, epicId);
        subtask.setId(5);

        assertEquals("Subtask Title", subtask.getName());
        assertEquals("Subtask Desc", subtask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, subtask.getStatus());
        assertEquals(duration, subtask.getDuration());
        assertEquals(startTime, subtask.getStartTime());
        assertEquals(startTime.plus(duration), subtask.getEndTime());
        assertEquals(epicId, subtask.getEpicId());
        assertEquals(TaskType.SUBTASK, subtask.getType());
    }

    @Test
    void equalsAndHashCodeShouldIncludeEpicId() {
        Duration duration = Duration.ofMinutes(15);
        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 9, 0);

        Subtask s1 = new Subtask("S", "D", TaskStatus.NEW, duration, time, 1);
        s1.setId(10);

        Subtask s2 = new Subtask("S", "D", TaskStatus.NEW, duration, time, 1);
        s2.setId(10);

        Subtask s3 = new Subtask("S", "D", TaskStatus.NEW, duration, time, 2);
        s3.setId(10);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, s3);
    }
}
