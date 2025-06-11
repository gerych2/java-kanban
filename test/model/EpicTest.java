package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicTimeFieldsShouldBeCalculatedFromSubtasks() {
        Epic epic = new Epic("Эпик", "Описание");

        Subtask sub1 = new Subtask("Подзадача 1", "Описание", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 6, 1, 10, 0), epic.getId());
        Subtask sub2 = new Subtask("Подзадача 2", "Описание", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2024, 6, 1, 12, 0), epic.getId());

        epic.updateTimeFields(java.util.List.of(sub1, sub2));

        assertEquals(Duration.ofMinutes(120), epic.getDuration());
        assertEquals(LocalDateTime.of(2024, 6, 1, 10, 0), epic.getStartTime());
        assertEquals(LocalDateTime.of(2024, 6, 1, 13, 30), epic.getEndTime()); // 12:00 + 1.5ч
    }

    @Test
    void epicWithNoSubtasksShouldHaveZeroDurationAndNullTimes() {
        Epic epic = new Epic("Эпик", "Описание");
        epic.updateTimeFields(java.util.Collections.emptyList());

        assertEquals(Duration.ZERO, epic.getDuration());
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
    }

    @Test
    void equalsAndHashCodeShouldIncludeTimeFields() {
        Epic epic1 = new Epic("Эпик", "Описание");
        Epic epic2 = new Epic("Эпик", "Описание");

        assertEquals(epic1, epic2);
        assertEquals(epic1.hashCode(), epic2.hashCode());
    }
}
