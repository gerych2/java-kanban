package service;

import model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private HandLinkedList<Task> list = new HandLinkedList<>();

    private Map<Integer, Node<Task>> mapHistory = new HashMap<>();

    @Override
    public void add(Task task) {
        int idTask = task.getId();

        if (mapHistory.containsKey(idTask)) {
            list.removeNode(mapHistory.get(idTask));
            mapHistory.remove(idTask);
            Node<Task> newNode = list.addLast(task);
            mapHistory.put(idTask, newNode);
            return;
        }
        Node<Task> newNode = list.addLast(task);
        mapHistory.put(idTask, newNode);
    }

    @Override
    public Collection<Task> getHistory() {
        return list.getDataHistory();
    }

    @Override
    public void remove(int id) {
        if (mapHistory.containsKey(id)) {
            Node<Task> foundNode = mapHistory.get(id);
            mapHistory.remove(id);
            list.removeNode(foundNode);
        }
    }
}
