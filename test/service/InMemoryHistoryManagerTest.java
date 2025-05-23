package service;

import org.junit.jupiter.api.BeforeEach;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManagerTest {
    private static TaskManager manager;
    private static List<Task> expected;

    @BeforeEach
    public void setupHistory() {
        manager = Managers.getDefault();
        expected = new ArrayList<>();

        Task a = new Task("Buy groceries", "Milk and eggs", TaskStatus.NEW, 10);
        Task b = new Task("Call mom", "Weekly check-in", TaskStatus.NEW, 11);
        Task c = new Task("Read book", "Finish chapter 4", TaskStatus.NEW, 12);
        Task d = new Task("Workout", "Cardio session", TaskStatus.NEW, 13);
        Task e = new Task("Code review", "Check PRs", TaskStatus.NEW, 14);
        Task f = new Task("Send email", "To client about updates", TaskStatus.NEW, 15);

        manager.addNewTask(a);
        manager.addNewTask(b);
        manager.addNewTask(c);
        manager.addNewTask(d);
        manager.addNewTask(e);
        manager.addNewTask(f);

        Epic epicA = new Epic("Launch project", "Release MVP", 100);
        Epic epicB = new Epic("Plan event", "Set venue and invite guests", 101);

        Subtask sa = new Subtask("Setup CI", "Integrate Jenkins", TaskStatus.NEW, 102, epicA);
        Subtask sb = new Subtask("Write script", "Opening remarks", TaskStatus.NEW, 103, epicA);
        Subtask sc = new Subtask("Book speakers", "Invite experts", TaskStatus.NEW, 104, epicB);

        manager.addNewEpic(epicA);
        manager.addNewEpic(epicB);
        manager.addNewSubtask(sa);
        manager.addNewSubtask(sb);
        manager.addNewSubtask(sc);

        manager.searchTaskById(10);
        manager.searchTaskById(11);
        manager.searchTaskById(12);
        manager.searchTaskById(13);
        manager.searchTaskById(14);
        manager.searchEpicById(100);

        expected.add(a);
        expected.add(b);
        expected.add(c);
        expected.add(d);
        expected.add(e);
        expected.add(epicA);
    }

    @Test
    public void shouldCorrectlyTrackHistory() {
        Assertions.assertEquals(expected, manager.getHistory(),
                "History tracking is incorrect or incomplete");
    }

    @Test
    public void shouldHandleDuplicateEntriesCorrectly() {
        manager.searchTaskById(10);
        expected.removeFirst();
        expected.add(new Task("Buy groceries", "Milk and eggs", TaskStatus.NEW, 10));
        Assertions.assertEquals(expected, manager.getHistory(), "Failed to move duplicate from beginning");

        manager.searchTaskById(12);
        Task duplicate = new Task("Read book", "Finish chapter 4", TaskStatus.NEW, 12);
        expected.remove(duplicate);
        expected.add(duplicate);
        Assertions.assertEquals(expected, manager.getHistory(), "Failed to move middle duplicate");

        manager.searchTaskById(12);
        Assertions.assertEquals(expected, manager.getHistory(), "Failed to handle tail duplicate");
    }
}
