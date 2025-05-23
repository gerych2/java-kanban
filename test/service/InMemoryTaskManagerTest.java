package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class InMemoryTaskManagerTest {
    private static TaskManager manager;

    @BeforeEach
    public void setupTasks() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());

        Task a = new Task("Morning routine", "Brush and shower", TaskStatus.NEW, 201);
        Task b = new Task("Work emails", "Respond to inbox", TaskStatus.NEW, 202);
        manager.addNewTask(a);
        manager.addNewTask(b);

        Epic alpha = new Epic("Home renovation", "Fix kitchen and paint walls", 301);
        Epic beta = new Epic("Fitness goals", "Training and tracking", 302);
        manager.addNewEpic(alpha);
        manager.addNewEpic(beta);

        Subtask sa = new Subtask("Buy materials", "Cement and paint", TaskStatus.NEW, 401, alpha);
        Subtask sb = new Subtask("Hire workers", "Find painters", TaskStatus.NEW, 402, alpha);
        Subtask sc = new Subtask("Track progress", "Log workouts", TaskStatus.NEW, 403, beta);
        manager.addNewSubtask(sa);
        manager.addNewSubtask(sb);
        manager.addNewSubtask(sc);
    }

    @Test
    public void shouldRejectDuplicateTaskIds() {
        Task duplicate = new Task("Morning routine", "Brush and shower", TaskStatus.NEW, 201);
        Assertions.assertFalse(manager.addNewTask(duplicate));
    }

    @Test
    public void shouldRejectDuplicateEpicIds() {
        Epic duplicate = new Epic("Home renovation", "Fix kitchen and paint walls", 301);
        Assertions.assertFalse(manager.addNewEpic(duplicate));
    }

    @Test
    public void shouldRejectDuplicateSubtaskIdsInSameEpic() {
        Subtask dupeInSame = new Subtask("Buy materials", "Cement and paint", TaskStatus.NEW, 401, manager.searchEpicById(301));
        Subtask validInOther = new Subtask("Buy materials", "Cement and paint", TaskStatus.NEW, 401, manager.searchEpicById(302));
        Assertions.assertFalse(manager.addNewSubtask(dupeInSame));
        Assertions.assertTrue(manager.addNewSubtask(validInOther));
    }

    @Test
    public void shouldKeepTaskFieldsWhenAdded() {
        Task task = new Task("Write report", "Monthly update", TaskStatus.DONE, 250);
        manager.addNewTask(task);
        Task retrieved = manager.searchTaskById(250);
        Assertions.assertEquals("Write report", retrieved.getName());
        Assertions.assertEquals("Monthly update", retrieved.getDescription());
        Assertions.assertEquals(TaskStatus.DONE, retrieved.getStatus());
        Assertions.assertEquals(250, retrieved.getId());
    }

    @Test
    public void shouldKeepEpicFieldsWhenAdded() {
        Epic epic = new Epic("Yearly Plan", "Outline key goals", 260);
        manager.addNewTask(epic);
        Task retrieved = manager.searchTaskById(260);
        Assertions.assertEquals("Yearly Plan", retrieved.getName());
        Assertions.assertEquals("Outline key goals", retrieved.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, retrieved.getStatus());
        Assertions.assertEquals(260, retrieved.getId());
    }

    @Test
    public void shouldKeepSubtaskFieldsWhenAdded() {
        Subtask sub = new Subtask("Upload files", "To shared drive", TaskStatus.IN_PROGRESS, 270);
        manager.addNewTask(sub);
        Task retrieved = manager.searchTaskById(270);
        Assertions.assertEquals("Upload files", retrieved.getName());
        Assertions.assertEquals("To shared drive", retrieved.getDescription());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, retrieved.getStatus());
        Assertions.assertEquals(270, retrieved.getId());
    }

    @Test
    public void searchMethodsShouldReturnCorrectEntities() {
        Assertions.assertEquals(manager.searchTaskById(201), new Task("Morning routine", "Brush and shower", TaskStatus.NEW, 201));
        Assertions.assertEquals(manager.searchEpicById(301), new Epic("Home renovation", "Fix kitchen and paint walls", 301));
        Assertions.assertEquals(manager.searchSubtaskById(403), new Subtask("Track progress", "Log workouts", TaskStatus.NEW, 403, manager.searchEpicById(302)));
    }

    @Test
    public void shouldUpdateStatusCorrectly() {
        manager.updateTask(new Task("Morning routine", "Brush and shower", TaskStatus.NEW, 201),
                new Task("Morning routine", "Brush and shower", TaskStatus.IN_PROGRESS, 201));
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, manager.searchTaskById(201).getStatus());

        Subtask oldSub = new Subtask("Buy materials", "Cement and paint", TaskStatus.NEW, 401, manager.searchEpicById(301));
        Subtask updatedSub = new Subtask("Buy materials", "Cement and paint", TaskStatus.DONE, 401, manager.searchEpicById(301));
        manager.updateSubtask(oldSub, updatedSub);
        Assertions.assertEquals(TaskStatus.DONE, manager.searchSubtaskById(401).getStatus());
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, manager.searchEpicById(301).getStatus());

        manager.updateSubtask(manager.searchSubtaskById(402),
                new Subtask("Hire workers", "Find painters", TaskStatus.DONE, 402, manager.searchEpicById(301)));
        Assertions.assertEquals(TaskStatus.DONE, manager.searchEpicById(301).getStatus());
    }

    @Test
    public void shouldResetEpicStatusWhenAllSubtasksRemoved() {
        manager.updateSubtask(manager.searchSubtaskById(401),
                new Subtask("Buy materials", "Cement and paint", TaskStatus.DONE, 401, manager.searchEpicById(301)));
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, manager.searchEpicById(301).getStatus());
        manager.removeAllSubtask();
        Assertions.assertEquals(TaskStatus.NEW, manager.searchEpicById(301).getStatus());
    }

    @Test
    public void shouldDeleteByIdCorrectly() {
        manager.removeTaskById(201);
        Assertions.assertNull(manager.searchTaskById(201));
        manager.removeEpicById(301);
        Assertions.assertNull(manager.searchEpicById(301));
        manager.removeSubtaskById(403);
        Assertions.assertNull(manager.searchSubtaskById(403));
    }

    @Test
    public void shouldReturnCorrectSubtasksOfEpic() {
        Collection<Subtask> expected = new ArrayList<>();
        expected.add(manager.searchSubtaskById(401));
        expected.add(manager.searchSubtaskById(402));
        Assertions.assertEquals(expected, manager.listSubtaskOfEpic(manager.searchEpicById(301)));
    }

    @Test
    public void shouldClearSubtasksOfRemovedEpic() {
        Epic epic = new Epic("Home renovation", "Fix kitchen and paint walls", 301);
        Epic otherEpic = new Epic("Fitness goals", "Training and tracking", 302);
        Subtask s1 = new Subtask("Buy materials", "Cement and paint", TaskStatus.NEW, 401, epic);
        Subtask s2 = new Subtask("Hire workers", "Find painters", TaskStatus.NEW, 402, epic);
        Subtask s3 = new Subtask("Track progress", "Log workouts", TaskStatus.NEW, 403, otherEpic);
        TaskManager tempManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        tempManager.addNewEpic(epic);
        tempManager.addNewEpic(otherEpic);
        tempManager.addNewSubtask(s1);
        tempManager.addNewSubtask(s2);
        tempManager.addNewSubtask(s3);

        Map<Integer, Subtask> remaining = new HashMap<>();
        remaining.put(s3.getId(), s3);
        tempManager.removeEpicById(301);
        Assertions.assertEquals(remaining, tempManager.showUpSubtask());
    }

    @Test
    public void shouldUpdateEpicSubtaskListOnSubtaskRemoval() {
        Epic epic = new Epic("Home renovation", "Fix kitchen and paint walls", 301);
        Subtask s1 = new Subtask("Buy materials", "Cement and paint", TaskStatus.NEW, 401, epic);
        Subtask s2 = new Subtask("Hire workers", "Find painters", TaskStatus.NEW, 402, epic);
        TaskManager tempManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        tempManager.addNewEpic(epic);
        tempManager.addNewSubtask(s1);
        tempManager.addNewSubtask(s2);

        tempManager.removeSubtaskById(401);

        List<Subtask> expected = new ArrayList<>();
        expected.add(s2);
        Assertions.assertEquals(expected, tempManager.listSubtaskOfEpic(epic));
    }
}
