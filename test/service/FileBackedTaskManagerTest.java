package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File file;

    @BeforeEach
    void setupFile() {
        file = new File("test_save.csv");
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    FileBackedTaskManager createManager() {
        return new FileBackedTaskManager(file);
    }

    @Test
    void taskShouldBeSavedAndLoadedWithTimeFields() {
        Task task = new Task("SaveTest", "Description", TaskStatus.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2024, 6, 1, 12, 0));
        manager.addTask(task);

        FileBackedTaskManager loaded = new FileBackedTaskManager(file);
        List<Task> tasks = loaded.getTasks();

        assertEquals(1, tasks.size());

        Task loadedTask = tasks.get(0);
        assertEquals(task.getName(), loadedTask.getName());
        assertEquals(task.getDescription(), loadedTask.getDescription());
        assertEquals(task.getStatus(), loadedTask.getStatus());
        assertEquals(task.getStartTime(), loadedTask.getStartTime());
        assertEquals(task.getDuration(), loadedTask.getDuration());
        assertEquals(task.getEndTime(), loadedTask.getEndTime());
    }

    @Test
    void emptyManagerShouldNotCrashOnLoad() {
        assertDoesNotThrow(() -> new FileBackedTaskManager(file));
    }
}
