package tracker.manager;

import org.junit.jupiter.api.Test;
import tracker.model.Task;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private final TaskManager manager = Managers.getDefault();

    @Test
    void shouldAddAndFindTask() {
        Task task = new Task("Test task", "Test desc", TaskStatus.NEW);
        manager.addTask(task);

        Task foundTask = manager.getTask(task.getId());
        assertNotNull(foundTask, "Задача должна быть найдена");
        assertEquals(task, foundTask, "Найденная задача должна совпадать с исходной");
    }

    @Test
    void shouldAddEpicWithSubtasks() {
        Epic epic = new Epic("Epic", "Epic desc");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Sub", "Sub desc", TaskStatus.NEW, epic.getId());
        manager.addSubtask(subtask);

        List<Subtask> subtasks = manager.getEpicSubtasks(epic.getId());
        assertEquals(1, subtasks.size(), "Эпик должен содержать одну подзадачу");
    }

    @Test
    void idShouldBeUnique() {
        Task task1 = new Task("Task1", "Desc1", TaskStatus.NEW);
        Task task2 = new Task("Task2", "Desc2", TaskStatus.NEW);

        manager.addTask(task1);
        manager.addTask(task2);

        assertNotEquals(task1.getId(), task2.getId(), "ID задач должны быть уникальными");
    }
}
