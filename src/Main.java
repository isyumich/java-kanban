import Manager.InMemoryTaskManager;
import Task.Epic;
import Task.Subtask;
import Task.Task;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.add(new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000)));
        inMemoryTaskManager.add(new Task(1, "Task 2", "Description Task 2", "NEW", 1440L, Instant.ofEpochSecond(1664812800)));
        inMemoryTaskManager.add(new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600)));
        inMemoryTaskManager.add(new Subtask(1, "Subtask 1 Epic 1", "Description Subtask 1 Epic 1", "NEW",1440L, Instant.ofEpochSecond(1665158400), 3));
        inMemoryTaskManager.add(new Subtask(1, "Subtask 2 Epic 1", "Description Subtask 2 Epic 1", "NEW",1440L, Instant.ofEpochSecond(1665331200), 3));
        inMemoryTaskManager.add(new Epic(1, "Epic 2", "Description Epic 2", "NEW", 1440L, Instant.ofEpochSecond(1665504000)));
        inMemoryTaskManager.add(new Subtask(1, "Subtask 1 Epic 2", "Description Subtask 1 Epic 2", "NEW",1440L, Instant.ofEpochSecond(1665676800), 6));
        System.out.println("All tasks: " + '\n' + inMemoryTaskManager.getAllTasks());
        System.out.println("All epics: " + '\n' + inMemoryTaskManager.getAllEpics());
        System.out.println("All subtasks: " + '\n' + inMemoryTaskManager.getAllSubtasks());
        inMemoryTaskManager.update(new Task(1, "Task 1", "Changed Description Task 1", "IN_PROGRESS", 1440L, Instant.ofEpochSecond(1664640000)));
        inMemoryTaskManager.update(new Subtask(4, "Subtask 1 Epic 1", "Changed Description Subtask 1 Epic 1", "IN_PROGRESS",1440L, Instant.ofEpochSecond(1665158400), inMemoryTaskManager.getSubtasksMap().get(4).getEpicId()));
        inMemoryTaskManager.update(new Subtask(7, "Subtask 1 Epic 2", "Changed Description Subtask 1 Epic 2", "DONE", 1440L, Instant.ofEpochSecond(1665676800), inMemoryTaskManager.getSubtasksMap().get(7).getEpicId()));
        System.out.println("All tasks: " + '\n' + inMemoryTaskManager.getAllTasks());
        System.out.println("All epics: " + '\n' + inMemoryTaskManager.getAllEpics());
        System.out.println("All subtasks: " + '\n' + inMemoryTaskManager.getAllSubtasks());
        System.out.println(inMemoryTaskManager.getHistory().toString());
        inMemoryTaskManager.deleteOneSubTask(4);
        inMemoryTaskManager.deleteOneEpic(3);
        inMemoryTaskManager.deleteOneTask(1);
        System.out.println("All tasks: " + '\n' + inMemoryTaskManager.getAllTasks());
        System.out.println("All epics: " + '\n' + inMemoryTaskManager.getAllEpics());
        System.out.println("All subtasks: " + '\n' + inMemoryTaskManager.getAllSubtasks());
        System.out.println(inMemoryTaskManager.getHistory().toString());
        System.out.println("Browsing task 2: " + '\n' + inMemoryTaskManager.getOneTask(2));
        System.out.println(inMemoryTaskManager.getHistory().toString());
        System.out.println("Prior tasks: " + inMemoryTaskManager.getPrioritizedTasks());
    }
}
