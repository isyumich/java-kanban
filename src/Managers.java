public class Managers<T extends TaskManager> {

    public TaskManager getDefault(T task) {
        return task;
    }

    public static HistoryManager getDefaultHistory(InMemoryHistoryManager inMemoryHistoryManager) {
        return inMemoryHistoryManager;
    }
}
