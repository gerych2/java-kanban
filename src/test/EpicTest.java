package test;

import org.junit.jupiter.api.Test;
import tracker.model.Epic;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epics_shouldBeEqual_whenIdIsSame() {
        Epic epic1 = new Epic("Epic1", "Desc1");
        epic1.setId(1);
        Epic epic2 = new Epic("Epic2", "Desc2");
        epic2.setId(1);

        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
    }

    @Test
    void epic_shouldNotContainItselfAsSubtask() {
        Epic epic = new Epic("Epic", "Desc");
        epic.setId(1);

        epic.addSubtask(1);

        assertFalse(epic.getSubtaskIds().contains(epic.getId()), "Эпик не должен содержать самого себя в сабтасках");
    }
}