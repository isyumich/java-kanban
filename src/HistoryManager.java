public interface HistoryManager<T> {
    void add(Task task);
    void getTasks();
    void removeNode (Node<T> node);
}
