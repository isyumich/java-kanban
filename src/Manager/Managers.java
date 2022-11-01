package Manager;

import Task.Task;

public class Managers<T extends TaskManager> {

    public static TaskManager getDefault(TaskManager task) {
        return task;
    }

    public static HistoryManager<Task> getDefaultHistory(InMemoryHistoryManager<Task> inMemoryHistoryManager) {
        return inMemoryHistoryManager;
    }
}