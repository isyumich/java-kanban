import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager<T> implements HistoryManager {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        protected List<Task> lastViewTasks = new ArrayList<>();
        protected HashMap<Integer, Node<T>> mapLastViewTasks = new HashMap<>();

    public void linkLast (T element) {
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
    public List<Task> getTasks () {
        for (Node<T> x = head; x != null; ) {
            Node<T> next = x.next;
            lastViewTasks.add((Task) x.data);
            x = next;
        }
        return lastViewTasks;
    }

    @Override
    public void add (Task task) {
        if (lastViewTasks.size() == 10) {
            lastViewTasks.remove(0);
        }
        lastViewTasks.add(task);
    }

    @Override
    public void removeNode (Node<T> node) {
        System.out.println("Hello");
    }

    @Override
    public void remove (int id) {
        lastViewTasks.remove(id);
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "lastViewTasks=" + lastViewTasks +
                '}';
    }
}
