package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private TaskManager manager;
    private List<Task> expected;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
        expected = new ArrayList<>();

        Task task1 = new Task("Task1", "Desc1", TaskStatus.NEW, 1);
        Task task2 = new Task("Task2", "Desc2", TaskStatus.NEW, 2);
        Task task3 = new Task("Task3", "Desc3", TaskStatus.NEW, 3);
        Task task4 = new Task("Task4", "Desc4", TaskStatus.NEW, 4);
        Task task5 = new Task("Task5", "Desc5", TaskStatus.NEW, 5);
        Task task6 = new Task("Task6", "Desc6", TaskStatus.NEW, 6);

        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewTask(task3);
        manager.addNewTask(task4);
        manager.addNewTask(task5);
        manager.addNewTask(task6);

        Epic epic1 = new Epic("Epic1", "EpicDesc1", 10);
        Epic epic2 = new Epic("Epic2", "EpicDesc2", 11);
        Subtask sub1 = new Subtask("Sub1", "S1", TaskStatus.NEW, 101, epic1);
        Subtask sub2 = new Subtask("Sub2", "S2", TaskStatus.NEW, 102, epic1);
        Subtask sub3 = new Subtask("Sub3", "S3", TaskStatus.NEW, 103, epic2);

        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        manager.addNewSubtask(sub1);
        manager.addNewSubtask(sub2);
        manager.addNewSubtask(sub3);

        manager.searchTaskById(1);
        manager.searchTaskById(2);
        manager.searchTaskById(3);
        manager.searchTaskById(4);
        manager.searchTaskById(5);
        manager.searchEpicById(10);

        expected.add(task1);
        expected.add(task2);
        expected.add(task3);
        expected.add(task4);
        expected.add(task5);
        expected.add(epic1);
    }

    @Test
    void shouldCorrectlyAddToHistory() {
        assertEquals(expected, manager.getHistory(),
                "История добавляется некорректно или не добавляется вовсе");
    }

    @Test
    void shouldRemoveDuplicateWhenReAccessed() {
        // перемещаем task1 в конец
        manager.searchTaskById(1);
        expected.removeFirst();
        expected.add(new Task("Task1", "Desc1", TaskStatus.NEW, 1));
        assertEquals(expected, manager.getHistory(), "Неправильное удаление повтора в начале");

        // перемещаем task3
        manager.searchTaskById(3);
        Task task3 = new Task("Task3", "Desc3", TaskStatus.NEW, 3);
        expected.remove(task3);
        expected.add(task3);
        assertEquals(expected, manager.getHistory(), "Неправильное удаление дубля в середине");

        // повторный доступ к task3 (уже в конце)
        manager.searchTaskById(3);
        assertEquals(expected, manager.getHistory(), "Неправильное поведение при повторном доступе к последнему");
    }
}
