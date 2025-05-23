package model;

import org.junit.jupiter.api.Test;


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

}