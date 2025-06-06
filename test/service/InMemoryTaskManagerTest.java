package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import model.TaskType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    private Task task1;

    private Task task2;

    private Epic epic1;

    private Subtask subtask1;

    private Subtask subtask2;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();

        task1 = new Task("Task 1", "Description 1", TaskStatus.NEW) {
            @Override
            public TaskType getType() {
                return TaskType.TASK;
            }
        };
        task1.setId(1);

        task2 = new Task("Task 2", "Description 2", TaskStatus.NEW) {
            @Override
            public TaskType getType() {
                return TaskType.TASK;
            }
        };
        task2.setId(2);

        epic1 = new Epic("Epic 1", "Description Epic 1");
        epic1.setId(3);

        subtask1 = new Subtask("Subtask 1", "Description Subtask 1", TaskStatus.NEW, epic1.getId());
        subtask1.setId(4);

        subtask2 = new Subtask("Subtask 2", "Description Subtask 2", TaskStatus.NEW, epic1.getId());
        subtask2.setId(5);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
    }

    @Test
    void shouldRemoveTaskFromHistoryWhenDeleted() {
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());

        taskManager.deleteTask(task1.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    void shouldRemoveSubtaskFromHistoryWhenDeleted() {
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());

        taskManager.deleteSubtask(subtask1.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(subtask2, history.get(0));
    }

    @Test
    void shouldRemoveEpicAndSubtasksFromHistoryWhenDeleted() {
        taskManager.getEpic(epic1.getId());
        taskManager.getSubtask(subtask1.getId());

        taskManager.deleteEpic(epic1.getId());

        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void shouldMaintainTaskOrderInHistory() {
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));
    }

    @Test
    void shouldUpdateTaskStatusInHistory() {
        taskManager.getTask(task1.getId());
        task1.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);

        List<Task> history = taskManager.getHistory();
        assertEquals(TaskStatus.IN_PROGRESS, history.get(0).getTaskStatus());
    }

    @Test
    void shouldUpdateSubtaskStatusInHistory() {
        taskManager.getSubtask(subtask1.getId());
        subtask1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);

        List<Task> history = taskManager.getHistory();
        assertEquals(TaskStatus.DONE, history.get(0).getTaskStatus());
    }

    @Test
    void shouldUpdateEpicStatusInHistory() {
        taskManager.getEpic(epic1.getId());
        subtask1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);

        List<Task> history = taskManager.getHistory();
        assertEquals(TaskStatus.IN_PROGRESS, history.get(0).getTaskStatus());
    }

    @Test
    void shouldHandleLargeHistory() {
        for (int i = 0; i < 1000; i++) {
            Task task = new Task("Task " + i, "Description " + i, TaskStatus.NEW) {
                @Override
                public TaskType getType() {
                    return TaskType.TASK;
                }
            };
            task.setId(i);
            taskManager.addTask(task);
            taskManager.getTask(task.getId());
        }

        List<Task> history = taskManager.getHistory();
        assertEquals(1000, history.size());
    }
}
