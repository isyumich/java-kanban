package manager;

import task.Task;

import java.io.File;
import java.time.LocalDateTime;

public class Managers<T extends TaskManager> {

    public static HTTPTaskManager getDefault() {
        return new HTTPTaskManager();
    }

    public static HistoryManager<Task> getDefaultHistory(InMemoryHistoryManager<Task> inMemoryHistoryManager) {
        return inMemoryHistoryManager;
    }

    public static LocalDateTime createDate (int day, int month) {
        return LocalDateTime.of(2022, month, day, 12, 0);
    }
}