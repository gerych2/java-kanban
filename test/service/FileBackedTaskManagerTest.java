package service;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File file;

    private FileBackedTaskManager manager;

    @BeforeEach
    void setUpConcrete() {
        file = new File("test_tasks.csv");
        manager = new FileBackedTaskManager(file);
    }

    @AfterEach
    void tearDown() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    FileBackedTaskManager createManager() {
        return new FileBackedTaskManager(new File("test_tasks.csv"));
    }

    @Test
    void taskShouldBeSavedAndLoadedWithTimeFields() {
        Task task = new Task("Timed task", "desc", TaskStatus.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2024, 6, 2, 9, 0));
        manager.addTask(task);

        FileBackedTaskManager loaded = new FileBackedTaskManager(file);
        List<Task> tasks = loaded.getPrioritizedTasks();

        assertEquals(1, tasks.size());
        Task loadedTask = tasks.get(0);

        assertEquals(task.getName(), loadedTask.getName());
        assertEquals(task.getDescription(), loadedTask.getDescription());
        assertEquals(task.getStartTime(), loadedTask.getStartTime());
        assertEquals(task.getDuration(), loadedTask.getDuration());
        assertEquals(task.getEndTime(), loadedTask.getEndTime());
    }

    @Test
    void emptyManagerShouldNotCrashOnLoad() {
        assertDoesNotThrow(() -> new FileBackedTaskManager(file));
    }
}
