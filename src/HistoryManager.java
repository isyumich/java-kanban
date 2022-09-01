import java.util.List;

public interface HistoryManager<T> {
    void add(Task task);
    void remove(int id);
    List<Task> getTasks();
    void removeNode (Node<T> node);
}
