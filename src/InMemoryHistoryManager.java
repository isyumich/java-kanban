import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    protected List<Task> lastViewTasks = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
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
    public String toString() {
        return "InMemoryHistoryManager{" +
                "lastViewTasks=" + lastViewTasks +
                '}';
    }
}
