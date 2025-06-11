package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected Task task1;

    protected Task task2;

    protected Epic epic1;

    protected Subtask subtask1;

    protected Subtask subtask2;

    @BeforeEach
    void setUp() {
        task1 = new Task("Task 1", "Description 1", TaskStatus.NEW) {
            @Override
            public TaskType getType() {
                return TaskType.TASK;
            }
        };
        task1.setId(1);
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(30));

        task2 = new Task("Task 2", "Description 2", TaskStatus.NEW) {
            @Override
            public TaskType getType() {
                return TaskType.TASK;
            }
        };
        task2.setId(2);
        task2.setStartTime(LocalDateTime.now().plusHours(1));
        task2.setDuration(Duration.ofMinutes(45));

        epic1 = new Epic("Epic 1", "Description Epic 1");
        epic1.setId(3);

        subtask1 = new Subtask("Subtask 1", "Description Subtask 1", TaskStatus.NEW, epic1.getId());
        subtask1.setId(4);
        subtask1.setStartTime(LocalDateTime.now().plusHours(2));
        subtask1.setDuration(Duration.ofMinutes(60));

        subtask2 = new Subtask("Subtask 2", "Description Subtask 2", TaskStatus.NEW, epic1.getId());
        subtask2.setId(5);
        subtask2.setStartTime(LocalDateTime.now().plusHours(3));
        subtask2.setDuration(Duration.ofMinutes(90));
    }

    @Test
    void shouldAddTask() {
        taskManager.addTask(task1);
        Task savedTask = taskManager.getTask(task1.getId());
        assertNotNull(savedTask, "Задача должна быть найдена");
        assertEquals(task1, savedTask, "Задачи должны совпадать");
    }

    @Test
    void shouldUpdateTask() {
        taskManager.addTask(task1);
        task1.setName("Updated Task");
        taskManager.updateTask(task1);
        Task updatedTask = taskManager.getTask(task1.getId());
        assertEquals("Updated Task", updatedTask.getName(), "Имя задачи должно быть обновлено");
    }

    @Test
    void shouldDeleteTask() {
        taskManager.addTask(task1);
        taskManager.deleteTask(task1.getId());
        assertNull(taskManager.getTask(task1.getId()), "Задача должна быть удалена");
    }

    @Test
    void shouldAddEpic() {
        taskManager.addEpic(epic1);
        Epic savedEpic = taskManager.getEpic(epic1.getId());
        assertNotNull(savedEpic, "Эпик должен быть найден");
        assertEquals(epic1, savedEpic, "Эпики должны совпадать");
    }

    @Test
    void shouldUpdateEpic() {
        taskManager.addEpic(epic1);
        epic1.setName("Updated Epic");
        taskManager.updateEpic(epic1);
        Epic updatedEpic = taskManager.getEpic(epic1.getId());
        assertEquals("Updated Epic", updatedEpic.getName(), "Имя эпика должно быть обновлено");
    }

    @Test
    void shouldDeleteEpic() {
        taskManager.addEpic(epic1);
        taskManager.deleteEpic(epic1.getId());
        assertNull(taskManager.getEpic(epic1.getId()), "Эпик должен быть удален");
    }

    @Test
    void shouldAddSubtask() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        Subtask savedSubtask = taskManager.getSubtask(subtask1.getId());
        assertNotNull(savedSubtask, "Подзадача должна быть найдена");
        assertEquals(subtask1, savedSubtask, "Подзадачи должны совпадать");
    }

    @Test
    void shouldUpdateSubtask() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        subtask1.setName("Updated Subtask");
        taskManager.updateSubtask(subtask1);
        Subtask updatedSubtask = taskManager.getSubtask(subtask1.getId());
        assertEquals("Updated Subtask", updatedSubtask.getName(), "Имя подзадачи должно быть обновлено");
    }

    @Test
    void shouldDeleteSubtask() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.deleteSubtask(subtask1.getId());
        assertNull(taskManager.getSubtask(subtask1.getId()), "Подзадача должна быть удалена");
    }

    @Test
    void shouldGetEpicSubtasks() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        List<Subtask> epicSubtasks = taskManager.getEpicSubtasks(epic1.getId());
        assertEquals(2, epicSubtasks.size(), "Должно быть 2 подзадачи");
        assertTrue(epicSubtasks.contains(subtask1), "Список должен содержать первую подзадачу");
        assertTrue(epicSubtasks.contains(subtask2), "Список должен содержать вторую подзадачу");
    }

    @Test
    void shouldGetPrioritizedTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(4, prioritizedTasks.size(), "Должно быть 4 задачи в списке приоритетов");
        assertTrue(prioritizedTasks.indexOf(task1) < prioritizedTasks.indexOf(task2), 
                "Первая задача должна быть перед второй");
        assertTrue(prioritizedTasks.indexOf(task2) < prioritizedTasks.indexOf(subtask1), 
                "Вторая задача должна быть перед первой подзадачей");
        assertTrue(prioritizedTasks.indexOf(subtask1) < prioritizedTasks.indexOf(subtask2), 
                "Первая подзадача должна быть перед второй");
    }

    @Test
    void shouldPreventTaskOverlap() {
        taskManager.addTask(task1);
        Task overlappingTask = new Task("Overlapping Task", "Description", TaskStatus.NEW) {
            @Override
            public TaskType getType() {
                return TaskType.TASK;
            }
        };
        overlappingTask.setStartTime(task1.getStartTime().plusMinutes(15));
        overlappingTask.setDuration(Duration.ofMinutes(30));

        assertThrows(IllegalArgumentException.class, () -> taskManager.addTask(overlappingTask),
                "Должно быть выброшено исключение при попытке добавить пересекающуюся задачу");
    }

    @Test
    void shouldUpdateEpicTimeFields() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        Epic epic = taskManager.getEpic(epic1.getId());
        assertNotNull(epic.getStartTime(), "Время начала эпика должно быть установлено");
        assertNotNull(epic.getEndTime(), "Время окончания эпика должно быть установлено");
        assertNotNull(epic.getDuration(), "Продолжительность эпика должна быть установлена");
        assertEquals(Duration.ofMinutes(150), epic.getDuration(), 
                "Продолжительность эпика должна быть равна сумме продолжительностей подзадач");
    }
} 