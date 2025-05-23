package service;

import java.util.ArrayList;
import java.util.List;

public class HandLinkedList<T> {

    private Node<T> head;

    private Node<T> tail;

    public Node<T> addLast(T element) {
        Node<T> newNode = new Node<>(tail, element, null);
        if (tail != null) {
            tail.setNext(newNode);
        } else {
            head = newNode;
        }
        tail = newNode;
        return newNode;
    }

    public void clear() {
        Node<T> current = head;
        while (current != null) {
            Node<T> next = current.getNext();
            current.setNext(null);
            current.setPrev(null);
            current = next;
        }
        head = null;
        tail = null;
    }

    public List<T> getDataHistory() {
        List<T> history = new ArrayList<>();
        for (Node<T> current = head; current != null; current = current.getNext()) {
            history.add(current.getData());
        }
        return history;
    }

    public void removeNode(Node<T> removeNode) {
        if (removeNode == null) return;

        Node<T> prev = removeNode.getPrev();
        Node<T> next = removeNode.getNext();

        if (prev != null) {
            prev.setNext(next);
        } else {
            head = next;
        }

        if (next != null) {
            next.setPrev(prev);
        } else {
            tail = prev;
        }

        removeNode.setNext(null);
        removeNode.setPrev(null);
    }
}
