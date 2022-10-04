import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager<T> implements HistoryManager<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    protected List<Task> lastViewTasks = new ArrayList<>();
    protected HashMap<Integer, Node<T>> mapLastViewTasks = new HashMap<>();

    public void linkLast(Task element) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(tail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    @Override
    public List<Task> getLastViewTask() {
        return lastViewTasks;
    }

    @Override
    public void getTasks() {
        lastViewTasks.clear();
        for (Node<T> x = head; x != null; ) {
            Node<T> next = x.next;
            lastViewTasks.add(x.data);
            x = next;
        }
    }

    @Override
    public void add(Task task) {
        if (mapLastViewTasks.containsKey(task.getTaskId())) {
            Node<T> node = mapLastViewTasks.get(task.getTaskId());
            removeNode(node);
        }
        linkLast(task);
        mapLastViewTasks.put(tail.data.getTaskId(), tail);
    }

    @Override
    public void removeNode(Node<T> node) {
        mapLastViewTasks.remove(node.data.getTaskId());
        final Node<T> prev = node.prev;
        final Node<T> next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        node.data = null;
        size--;
    }

    @Override
    public String toString() {
        getTasks();
        StringBuilder historyInfo = new StringBuilder("История просмотров: " + '\n');
        for (Task lastViewTask: lastViewTasks) {
            historyInfo.append(lastViewTask).append('\n');
        }
        return historyInfo.toString();
    }
}
