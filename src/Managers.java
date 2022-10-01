public class Managers<T extends TaskManager<T>> {

    public TaskManager<T> getDefault(TaskManager<T> task) {
        return task;
    }

    public static HistoryManager<Task> getDefaultHistory(InMemoryHistoryManager<Task> inMemoryHistoryManager) {
        return inMemoryHistoryManager;
    }
}