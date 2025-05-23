package service;

public class Node<E> {

    private Node<E> prev;

    private E data;

    private Node<E> next;

    public Node(Node<E> prev, E data, Node<E> next) {

        setPrev(prev);

        setData(data);

        setNext(next);

    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setPrev(Node<E> prev) {
        this.prev = prev;
    }

    public Node<E> getPrev() {
        return prev;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}
