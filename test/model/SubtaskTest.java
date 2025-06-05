package model;

import org.junit.jupiter.api.Test;
import model.Subtask;
import model.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Subtask subtask1 = new Subtask("Sub1", "Desc1", TaskStatus.NEW, 5);
        subtask1.setId(2);
        Subtask subtask2 = new Subtask("Sub2", "Desc2", TaskStatus.DONE, 5);
        subtask2.setId(2);

        assertEquals(subtask1, subtask2, "Сабтаски с одинаковым id должны быть равны");
    }

    @Test
    void subtaskCannotBeItsOwnEpic() {
        Subtask subtask = new Subtask("Sub", "Desc", TaskStatus.NEW, 999); // временный эпикId
        subtask.setId(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> subtask.setEpicId(1));
        assertEquals("Сабтаск не может быть своим эпиком", exception.getMessage());
    }
}
