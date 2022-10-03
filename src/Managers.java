public class Managers<T extends TaskManager<T>> {

    public static TaskManager<Task> getDefault(TaskManager<Task> task) {
        return task;
    }

    public static HistoryManager<Task> getDefaultHistory(InMemoryHistoryManager<Task> inMemoryHistoryManager) {
        return inMemoryHistoryManager;
    }
}