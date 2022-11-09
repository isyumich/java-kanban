package manager;

import task.Task;
import task.Epic;
import task.Subtask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;


import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    public final Path path = Path.of("src\\Manager\\TaskManager.csv");
    File file = new File(String.valueOf(path));

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTaskManager(String.valueOf(path));
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(path);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void shouldSaveAndLoadStandard() {
        HashMap<Integer, Task> tasksForTest = new HashMap<>();
        Task task = new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000));
        taskManager.add(task);
        tasksForTest.put(1, task);
        HashMap<Integer, Epic> epicsForTest = new HashMap<>();
        Epic epic = new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        taskManager.add(epic);
        epicsForTest.put(2, epic);
        HashMap<Integer, Subtask> subtasksForTest = new HashMap<>();
        Subtask subtask = new Subtask(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600), 2);
        taskManager.add(subtask);
        subtasksForTest.put(3, subtask);
        FileBackedTaskManager fileManager = new FileBackedTaskManager(String.valueOf(path));
        fileManager.loadFromFile(file);
        assertEquals(tasksForTest, taskManager.tasks);
        assertEquals(epicsForTest, taskManager.epics);
        assertEquals(subtasksForTest, taskManager.subtasks);
    }

    @Test
    public void shouldSaveAndLoadIfEmptyAll() {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(String.valueOf(path));
        fileManager.save();
        fileManager.loadFromFile(file);
        assertTrue(taskManager.tasks.isEmpty());
        assertTrue(taskManager.subtasks.isEmpty());
        assertTrue(taskManager.epics.isEmpty());
    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(String.valueOf(path));
        fileManager.save();
        fileManager.loadFromFile(file);
        assertTrue(taskManager.getHistory().getLastViewTask().isEmpty());
    }
}