package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final HandLinkedList<Task> list = new HandLinkedList<>();
    private final Map<Integer, Node<Task>> mapHistory = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) return;

        int idTask = task.getId();

        Node<Task> existingNode = mapHistory.remove(idTask);
        if (existingNode != null) {
            list.removeNode(existingNode);
        }

        Node<Task> newNode = list.addLast(task);
        mapHistory.put(idTask, newNode);
    }

    @Override
    public Collection<Task> getHistory() {
        return new ArrayList<>(list.getDataHistory());
    }

    @Override
    public void remove(int id) {
        Node<Task> node = mapHistory.remove(id);
        if (node != null) {
            list.removeNode(node);
        }
    }
}
