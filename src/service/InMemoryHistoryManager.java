package service;

import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodeMap = new HashMap<>();

    private Node head;

    private Node tail;

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) return;

        int id = task.getId();

        Node existingNode = nodeMap.get(id);
        if (existingNode != null) {
            // Если узел уже в конце, ничего не делаем
            if (existingNode == tail) {
                return;
            }

            if (existingNode.prev != null) {
                existingNode.prev.next = existingNode.next;
            } else {
                head = existingNode.next;
            }
            if (existingNode.next != null) {
                existingNode.next.prev = existingNode.prev;
            }

            existingNode.prev = tail;
            existingNode.next = null;
            tail.next = existingNode;
            tail = existingNode;
            return;
        }

        Node newNode = new Node(task);
        nodeMap.put(id, newNode);

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);
        if (node == null) return;

        // Обновляем связи
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }
}
