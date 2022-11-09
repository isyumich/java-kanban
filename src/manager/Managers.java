package manager;

import task.Task;

import java.io.File;

public class Managers<T extends TaskManager> {

    public static HTTPTaskManager getDefault() {
        return new HTTPTaskManager();
    }

    public static HistoryManager<Task> getDefaultHistory(InMemoryHistoryManager<Task> inMemoryHistoryManager) {
        return inMemoryHistoryManager;
    }
}