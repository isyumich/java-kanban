package Manager;

import Task.Task;
import Task.Epic;
import Task.Subtask;
import TasksInfo.TasksStatus;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {
    T taskManager;

    /*Тесты на создание задач*/
    @Test
    public void shouldAddTaskStandard() {
        HashMap<Integer, Task> tasksForTest = new HashMap<>();
        Task task = new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000));
        tasksForTest.put(1, task);
        taskManager.add(task);
        HashMap<Integer, Task> tasksFromManager = taskManager.getTasksMap();
        assertNotNull(task.getStatus());
        assertEquals(tasksForTest, tasksFromManager);
    }
    @Test
    public void shouldAddTaskIfNull() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,

                () -> {
                    Task task = null;
                    taskManager.add(task);
                }
        );
        assertEquals("You can't create empty task", exception.getMessage());
    }

    @Test
    public void shouldAddTaskIfWrongTaskId() {
        HashMap<Integer, Task> tasksForTest = new HashMap<>();
        Task task = new Task(999, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000));
        tasksForTest.put(1, task);
        taskManager.add(task);
        HashMap<Integer, Task> tasksFromManager = taskManager.getTasksMap();
        assertNotNull(task.getStatus());
        assertEquals(tasksForTest, tasksFromManager);
        assertEquals(1, task.getTaskId());
    }


    /*Тесты на создание эпиков*/
    @Test
    public void shouldAddEpicStandard() {
        HashMap<Integer, Epic> epicsForTest = new HashMap<>();
        Epic epic = new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        epicsForTest.put(1, epic);
        taskManager.add(epic);
        HashMap<Integer, Epic> epicsFromManager = taskManager.getEpicsMap();
        assertNotNull(epic.getStatus());
        assertEquals(epicsForTest, epicsFromManager);
    }
    @Test
    public void shouldAddEpicIfNull() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    Epic epic = null;
                    taskManager.add(epic);
                }
        );
        assertEquals("You can't create empty epic", exception.getMessage());
    }

    @Test
    public void shouldAddEpicIfWrongTaskId() {
        HashMap<Integer, Epic> epicsForTest = new HashMap<>();
        Epic epic = new Epic(999, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        epicsForTest.put(1, epic);
        taskManager.add(epic);
        HashMap<Integer, Epic> epicsFromManager = taskManager.getEpicsMap();
        assertNotNull(epic.getStatus());
        assertEquals(epicsForTest, epicsFromManager);
        assertEquals(1, epic.getTaskId());
    }

    /*Тесты на создание подзадач*/
    @Test
    public void shouldAddSubtaskStandard() {
        taskManager.add(new Epic(999, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600)));
        HashMap<Integer, Subtask> tasksForTest = new HashMap<>();
        Subtask subtask = new Subtask(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000), 1);
        tasksForTest.put(2, subtask);
        taskManager.add(subtask);
        HashMap<Integer, Subtask> tasksFromManager = taskManager.getSubtasksMap();
        assertNotNull(subtask.getStatus());
        assertEquals(tasksForTest, tasksFromManager);
    }
    @Test
    public void shouldAddSubtaskIfNull() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    Subtask subtask = null;
                    taskManager.add(subtask);
                }
        );
        assertEquals("You can't create empty subtask", exception.getMessage());
    }

    @Test
    public void shouldAddSubtaskIfWrongTaskId() {
        taskManager.add(new Epic(999, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600)));
        HashMap<Integer, Subtask> tasksForTest = new HashMap<>();
        Subtask subtask = new Subtask(999, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000), 1);
        tasksForTest.put(2, subtask);
        taskManager.add(subtask);
        HashMap<Integer, Subtask> tasksFromManager = taskManager.getSubtasksMap();
        assertNotNull(subtask.getStatus());
        assertEquals(tasksForTest, tasksFromManager);
        assertEquals(2, subtask.getTaskId());
        assertEquals(1, subtask.getEpicId());
    }

    /*Тест методов апдейта задач*/
    @Test
    public void shouldUpdateTaskStandard () {
        HashMap<Integer, Task> tasksForTest = new HashMap<>();
        Task task = new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000));
        taskManager.add(task);
        task.setDescription("Updated Description Task 1");
        tasksForTest.put(1, task);
        taskManager.update(task);
        HashMap<Integer, Task> tasksFromManager = taskManager.getTasksMap();
        assertNotNull(task.getStatus());
        assertEquals(tasksForTest, tasksFromManager);
    }
    @Test
    public void shouldUpdateTaskIfNull () {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,

                () -> {
                    Task task = null;
                    taskManager.update(task);
                }
        );
        assertEquals("You can't create empty task", exception.getMessage());
    }


    /*Тест методов апдейта эпиков*/
    @Test
    public void shouldUpdateEpicStandard () {
        HashMap<Integer, Epic> epicsForTest = new HashMap<>();
        Epic epic = new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        taskManager.add(epic);
        epic.setDescription("Updated Description Epic 1");
        epicsForTest.put(1, epic);
        taskManager.update(epic);
        HashMap<Integer, Epic> epicsFromManager = taskManager.getEpicsMap();
        assertNotNull(epic.getStatus());
        assertEquals(epicsForTest, epicsFromManager);
    }
    @Test
    public void shouldUpdateEpicIfNull () {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    Epic epic = null;
                    taskManager.update(epic);
                }
        );
        assertEquals("You can't create empty epic", exception.getMessage());
    }

    /*Тест методов апдейта подзадач*/
    @Test
    public void shouldUpdateSubtaskStandard () {
        taskManager.add(new Epic(999, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600)));
        HashMap<Integer, Subtask> subtasksForTest = new HashMap<>();
        Subtask subtask = new Subtask(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000), 1);
        taskManager.add(subtask);
        subtask.setDescription("Updated Description Task 1");
        subtasksForTest.put(2, subtask);
        taskManager.update(subtask);
        HashMap<Integer, Subtask> subtasksFromManager = taskManager.getSubtasksMap();
        assertNotNull(subtask.getStatus());
        assertEquals(subtasksForTest, subtasksFromManager);
    }
    @Test
    public void shouldUpdateSubtaskIfNull () {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    Subtask subtask = null;
                    taskManager.update(subtask);
                }
        );
        assertEquals("You can't create empty subtask", exception.getMessage());
    }

    /*Тест методов по удалению задач*/
    @Test
    public void shouldDeleteOneTask () {
        HashMap<Integer, Task> tasksForTest = new HashMap<>();
        Task task = new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000));
        taskManager.add(task);
        taskManager.deleteOneTask(1);
        HashMap<Integer, Task> tasksFromManager = taskManager.getTasksMap();
        assertEquals(tasksForTest, tasksFromManager);
    }

    @Test
    public void shouldDeleteAllTasks () {
        HashMap <Integer, Task> tasksForTest = new HashMap<>();
        Task task1 = new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000));
        Task task2 = new Task(1, "Task 2", "Description Task 2", "NEW", 1440L, Instant.ofEpochSecond(1664812800));
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.deleteAllTasks();
        HashMap<Integer, Task> tasksFromManager = taskManager.getTasksMap();
        assertEquals(tasksForTest, tasksFromManager);
    }
    /*Тест методов по удалению эпиков*/
    @Test
    public void shouldDeleteOneEpic () {
        HashMap<Integer, Epic> epicsForTest = new HashMap<>();
        Epic epic = new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        taskManager.add(epic);
        taskManager.deleteOneEpic(1);
        HashMap<Integer, Task> tasksFromManager = taskManager.getTasksMap();
        assertEquals(epicsForTest, tasksFromManager);
    }

    @Test
    public void shouldDeleteAllEpics () {
        HashMap <Integer, Epic> epicsForTest = new HashMap<>();
        Epic epic1 = new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        Epic epic2 = new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1665504000));
        taskManager.add(epic1);
        taskManager.add(epic2);
        taskManager.deleteAllEpics();
        HashMap<Integer, Epic> epicsFromManager = taskManager.getEpicsMap();
        assertEquals(epicsForTest, epicsFromManager);
    }

    /*Тест методов по удалению подзадач*/

    @Test
    public void shouldDeleteOneSubtask () {
        HashMap<Integer, Subtask> subtasksForTest = new HashMap<>();
        Epic epic = new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        taskManager.add(epic);
        Subtask subtask = new Subtask(1, "Subtask 1 Epic 1", "Description Subtask 1 Epic 1", "NEW",1440L, Instant.ofEpochSecond(1665158400), 1);
        taskManager.add(subtask);
        taskManager.deleteOneSubTask(2);
        HashMap<Integer, Subtask> subtasksFromManager = taskManager.getSubtasksMap();
        assertEquals(subtasksForTest, subtasksFromManager);
    }

    @Test
    public void shouldDeleteAllSubtasks () {
        HashMap<Integer, Subtask> subtasksForTest = new HashMap<>();
        Epic epic = new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600));
        taskManager.add(epic);
        Subtask subtask1 = new Subtask(1, "Subtask 1 Epic 1", "Description Subtask 1 Epic 1", "NEW",1440L, Instant.ofEpochSecond(1665158400), 1);
        Subtask subtask2 = new Subtask(1, "Subtask 2 Epic 1", "Description Subtask 2 Epic 1", "NEW",1440L, Instant.ofEpochSecond(1665331200), 1);
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        taskManager.deleteAllSubTasks();
        HashMap<Integer, Subtask> subtasksFromManager = taskManager.getSubtasksMap();
        assertEquals(subtasksForTest, subtasksFromManager);
    }

    /*Тест методов расчёта статуса эпиков*/
    @Test
    void shouldNewStatusForEmptyEpic () {
        taskManager.add(new Epic(1, "Epic 1", "Description Epic 1", "DONE", 1440L, Instant.ofEpochSecond(1664985600)));

        assertEquals(TasksStatus.NEW.toString(), taskManager.getEpicsMap().get(1).getStatus());
    }

    @Test
    void shouldNewStatusForAllNewSubtasks () {
        taskManager.add(new Epic(1, "Epic 1", "Description Epic 1", "DONE", 1440L, Instant.ofEpochSecond(1664985600)));
        taskManager.add(new Subtask(1, "Subtask 1 Epic 1", "Description Subtask 1 Epic 1", "NEW",1440L, Instant.ofEpochSecond(1665158400), 1));
        taskManager.add(new Subtask(1, "Subtask 2 Epic 1", "Description Subtask 2 Epic 1", "NEW",1440L, Instant.ofEpochSecond(1665331200), 1));
        assertEquals(TasksStatus.NEW.toString(), taskManager.getEpicsMap().get(1).getStatus());
    }

    @Test
    void shouldNewStatusForAllDoneSubtasks () {
        taskManager.add(new Epic(1, "Epic 1", "Description Epic 1", "DONE", 1440L, Instant.ofEpochSecond(1664985600)));
        taskManager.add(new Subtask(1, "Subtask 1 Epic 1", "Description Subtask 1 Epic 1", "DONE",1440L, Instant.ofEpochSecond(1665158400), 1));
        taskManager.add(new Subtask(1, "Subtask 2 Epic 1", "Description Subtask 2 Epic 1", "DONE",1440L, Instant.ofEpochSecond(1665331200), 1));

        assertEquals(TasksStatus.DONE.toString(), taskManager.getEpicsMap().get(1).getStatus());
    }

    @Test
    void shouldNewStatusForDoneAndNewSubtasks () {
        taskManager.add(new Epic(1, "Epic 1", "Description Epic 1", "DONE", 1440L, Instant.ofEpochSecond(1664985600)));
        taskManager.add(new Subtask(1, "Subtask 1 Epic 1", "Description Subtask 1 Epic 1", "NEW",1440L, Instant.ofEpochSecond(1665158400), 1));
        taskManager.add(new Subtask(1, "Subtask 2 Epic 1", "Description Subtask 2 Epic 1", "DONE",1440L, Instant.ofEpochSecond(1665331200), 1));

        assertEquals(TasksStatus.IN_PROGRESS.toString(), taskManager.getEpicsMap().get(1).getStatus());
    }

    @Test
    void shouldNewStatusForAllInProgressSubtasks () {
        taskManager.add(new Epic(1, "Epic 1", "Description Epic 1", "DONE", 1440L, Instant.ofEpochSecond(1664985600)));
        taskManager.add(new Subtask(1, "Subtask 1 Epic 1", "Description Subtask 1 Epic 1", "IN_PROGRESS",1440L, Instant.ofEpochSecond(1665158400), 1));
        taskManager.add(new Subtask(1, "Subtask 2 Epic 1", "Description Subtask 2 Epic 1", "IN_PROGRESS",1440L, Instant.ofEpochSecond(1665331200), 1));

        assertEquals(TasksStatus.IN_PROGRESS.toString(), taskManager.getEpicsMap().get(1).getStatus());
    }
}