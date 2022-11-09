package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryManagerTest {

    InMemoryTaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void shouldAddTasksEpicsSubtasksToHistory() {
        Task task = new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000));
        Epic epic = new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        Subtask subtask = new Subtask(1, "Subtask 1 Epic 1", "Description Subtask 1 Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1665158400), 2);
        taskManager.add(task);
        taskManager.add(epic);
        taskManager.add(subtask);
        taskManager.getAllTasks();
        taskManager.getAllEpics();
        taskManager.getAllSubtasks();
        assertEquals(List.of(task, epic, subtask), taskManager.getHistory().getLastViewTask());
    }

    @Test
    public void shouldGetHistoryWithoutDoubles() {
        Task task = new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000));
        Epic epic = new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        Subtask subtask = new Subtask(1, "Subtask 1 Epic 1", "Description Subtask 1 Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1665158400), 2);
        taskManager.add(task);
        taskManager.add(epic);
        taskManager.add(subtask);
        taskManager.getOneTask(1);
        taskManager.getAllEpics();
        taskManager.getAllSubtasks();
        taskManager.getOneTask(1);
        assertEquals(List.of(epic, subtask, task), taskManager.getHistory().getLastViewTask());
    }

    @Test
    public void shouldGetHistoryWithoutDeletedFirstTask() {
        Task task1 = new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000));
        Task task2 = new Task(1, "Task 2", "Description Task 2", "NEW", 1440L, Instant.ofEpochSecond(1664812800));
        Task task3 = new Task(1, "Task 3", "Description Task 3", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(task3);
        taskManager.getOneTask(1);
        taskManager.getOneTask(2);
        taskManager.getOneTask(3);
        assertEquals(List.of(task1, task2, task3), taskManager.getHistory().getLastViewTask());
        taskManager.deleteOneTask(1);
        assertEquals(List.of(task2, task3), taskManager.getHistory().getLastViewTask());
    }

    @Test
    public void shouldGetHistoryWithoutDeletedMiddleTask() {
        Task task1 = new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000));
        Task task2 = new Task(1, "Task 2", "Description Task 2", "NEW", 1440L, Instant.ofEpochSecond(1664812800));
        Task task3 = new Task(1, "Task 3", "Description Task 3", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(task3);
        taskManager.getOneTask(1);
        taskManager.getOneTask(2);
        taskManager.getOneTask(3);
        assertEquals(List.of(task1, task2, task3), taskManager.getHistory().getLastViewTask());
        taskManager.deleteOneTask(2);
        assertEquals(List.of(task1, task3), taskManager.getHistory().getLastViewTask());
    }

    @Test
    public void shouldGetHistoryWithoutDeletedLastTask() {
        Task task1 = new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000));
        Task task2 = new Task(1, "Task 2", "Description Task 2", "NEW", 1440L, Instant.ofEpochSecond(1664812800));
        Task task3 = new Task(1, "Task 3", "Description Task 3", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(task3);
        taskManager.getOneTask(1);
        taskManager.getOneTask(2);
        taskManager.getOneTask(3);
        assertEquals(List.of(task1, task2, task3), taskManager.getHistory().getLastViewTask());
        taskManager.deleteOneTask(3);
        assertEquals(List.of(task1, task2), taskManager.getHistory().getLastViewTask());
    }
}
