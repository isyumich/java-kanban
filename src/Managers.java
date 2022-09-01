public class Managers<T extends TaskManager> {

    public TaskManager getDefault(T task) {
        return task;
    }

    public static HistoryManager<Task> getDefaultHistory(InMemoryHistoryManager<Task> inMemoryHistoryManager) {
        return inMemoryHistoryManager;
    }
}
