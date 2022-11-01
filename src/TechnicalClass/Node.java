package TechnicalClass;

import Task.Task;

public class Node<T> {
    public Task data;
    public Node<T> prev;
    public Node<T> next;

    public Node(Node<T> prev, Task data, Node<T> next) {
        this.prev = prev;
        this.data = data;
        this.next = next;
    }
}