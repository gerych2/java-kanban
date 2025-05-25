package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    private Task task1;

    private Task task2;

    private Epic epic1;

    private Subtask subtask1;

    private Subtask subtask2;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        
        task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        task1.setId(1);
        
        task2 = new Task("Task 2", "Description 2", TaskStatus.NEW);
        task2.setId(2);
        
        epic1 = new Epic("Epic 1", "Description Epic 1");
        epic1.setId(3);
        
        subtask1 = new Subtask("Subtask 1", "Description Subtask 1", TaskStatus.NEW, epic1.getId());
        subtask1.setId(4);
        
        subtask2 = new Subtask("Subtask 2", "Description Subtask 2", TaskStatus.NEW, epic1.getId());
        subtask2.setId(5);
    }

    @Test
    void add_addsTaskToEndOfHistory() {
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История должна быть не пуста");
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }

    @Test
    void add_replacesDuplicateTaskInHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1); // task1 добавляется снова

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0), "Первой должна быть task2");
        assertEquals(task1, history.get(1), "Второй должна быть task1");
    }

    @Test
    void remove_removesTaskById() {
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(1); // удаляем task1
        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    void getHistory_returnsEmptyListWhenNoTasks() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой");
    }

    @Test
    void shouldMaintainOrderWhenAddingMultipleTasks() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1); // Добавляем task1 снова

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));

        // Добавляем task1 снова и проверяем, что он переместился в конец
        historyManager.add(task1);
        history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));
    }

    @Test
    void shouldRemoveTaskFromHistoryWhenDeleted() {
        historyManager.add(task1);
        historyManager.add(task2);
        
        historyManager.remove(task1.getId());
        
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    void shouldRemoveSubtaskFromHistoryWhenDeleted() {
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        
        historyManager.remove(subtask1.getId());
        
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(subtask2, history.get(0));
    }

    @Test
    void shouldRemoveEpicFromHistoryWhenDeleted() {
        historyManager.add(epic1);
        historyManager.add(subtask1);
        
        historyManager.remove(epic1.getId());
        
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(subtask1, history.get(0));
    }

    @Test
    void shouldHandleLargeHistory() {
        // Добавляем много задач
        for (int i = 0; i < 1000; i++) {
            Task task = new Task("Task " + i, "Description " + i, TaskStatus.NEW);
            task.setId(i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(1000, history.size(), "История должна содержать все 1000 задач");
        
        // Проверяем порядок
        for (int i = 0; i < 1000; i++) {
            assertEquals(i, history.get(i).getId(), "Порядок задач должен сохраняться");
        }
    }

    @Test
    void shouldHandleDuplicateTasksInLargeHistory() {
        // Добавляем много задач
        for (int i = 0; i < 1000; i++) {
            Task task = new Task("Task " + i, "Description " + i, TaskStatus.NEW);
            task.setId(i);
            historyManager.add(task);
        }

        // Добавляем дубликаты
        for (int i = 0; i < 1000; i += 2) {
            Task task = new Task("Task " + i, "Description " + i, TaskStatus.NEW);
            task.setId(i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(1000, history.size(), "История должна содержать 1000 уникальных задач");
        
        // Проверяем, что все задачи уникальны
        for (int i = 0; i < history.size(); i++) {
            for (int j = i + 1; j < history.size(); j++) {
                assertNotEquals(history.get(i).getId(), history.get(j).getId(), 
                    "В истории не должно быть дубликатов");
            }
        }
    }
} 