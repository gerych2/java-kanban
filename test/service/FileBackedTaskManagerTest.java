package service;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File testFile;

    @Override
    FileBackedTaskManager createManager() {
        testFile = new File("test-tasks.csv");
        return new FileBackedTaskManager(testFile);
    }

    @AfterEach
    void cleanup() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void taskShouldBeSavedAndLoadedWithTimeFields() {
        Task task = new Task("TestTask", "desc", TaskStatus.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2024, 6, 1, 14, 0));
        manager.addTask(task);

        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(testFile);
        List<Task> tasks = loaded.getTasks();

        assertEquals(1, tasks.size());
        Task loadedTask = tasks.get(0);
        assertEquals(task.getStartTime(), loadedTask.getStartTime());
        assertEquals(task.getDuration(), loadedTask.getDuration());
        assertEquals(task.getEndTime(), loadedTask.getEndTime());
    }

    @Test
    void emptyManagerShouldNotCrashOnLoad() {
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(testFile);
        assertNotNull(loaded.getTasks());
        assertTrue(loaded.getTasks().isEmpty());
    }
}
