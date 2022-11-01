package Manager;

import Task.Task;
import TechnicalClass.Node;

import java.util.List;

public interface HistoryManager<T> {
    void add(Task task);

    void getTasks();

    List<Task> getLastViewTask();

    void removeNode(Node<T> node);
}
