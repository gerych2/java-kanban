package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicTimeFieldsShouldBeCalculatedFromSubtasks() {
        Epic epic = new Epic("Epic title", "Epic description");
        epic.setId(1);

        Subtask sub1 = new Subtask("sub1", "desc", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 6, 1, 10, 0), 1);
        sub1.setId(2);

        Subtask sub2 = new Subtask("sub2", "desc", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2024, 6, 1, 12, 0), 1);
        sub2.setId(3);

        epic.updateTimeFields(List.of(sub1, sub2));

        assertEquals(Duration.ofMinutes(120), epic.getDuration());
        assertEquals(LocalDateTime.of(2024, 6, 1, 10, 0), epic.getStartTime());
        assertEquals(LocalDateTime.of(2024, 6, 1, 13, 30), epic.getEndTime());
    }

    @Test
    void epicWithNoSubtasksShouldHaveZeroDurationAndNullTimes() {
        Epic epic = new Epic("Empty Epic", "No subtasks");
        epic.updateTimeFields(List.of());

        assertEquals(Duration.ZERO, epic.getDuration());
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
    }

    @Test
    void equalsAndHashCodeShouldIncludeTimeFields() {
        Epic epic1 = new Epic("Title", "Desc");
        epic1.setId(1);
        Epic epic2 = new Epic("Title", "Desc");
        epic2.setId(1);

        Subtask sub1 = new Subtask("Sub", "Desc", TaskStatus.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 10, 0), 1);

        epic1.updateTimeFields(List.of(sub1));
        epic2.updateTimeFields(List.of(sub1));

        assertEquals(epic1, epic2);
        assertEquals(epic1.hashCode(), epic2.hashCode());
    }
}
