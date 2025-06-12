package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void equalsAndHashCodeShouldIncludeTimeFields() {
        Epic epic1 = new Epic("Эпик", "Описание");
        Epic epic2 = new Epic("Эпик", "Описание");

        assertEquals(epic1, epic2);
        assertEquals(epic1.hashCode(), epic2.hashCode());
    }

    @Test
    void epicFieldsShouldRemainEmptyWithoutManagerUpdate() {
        Epic epic = new Epic("Эпик", "Описание");

        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertEquals(Duration.ZERO, epic.getDuration());
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
    }
}
