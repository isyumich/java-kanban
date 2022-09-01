import java.util.List;

public interface HistoryManager<T> {
    void add(Task task);
//    void remove(int id);
    void getTasks();
    void removeNode (Node<T> node);
}
