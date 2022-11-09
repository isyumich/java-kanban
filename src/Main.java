import manager.HTTPTaskManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import servers.KVServer;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.time.Instant;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HTTPTaskManager taskManager = Managers.getDefault();
        taskManager.add(new Task(1, "Task 1", "Description Task 1", "NEW", 1440L, Instant.ofEpochSecond(1664640000)));
        taskManager.add(new Task(1, "Task 2", "Description Task 2", "NEW", 1440L, Instant.ofEpochSecond(1664812800)));
        taskManager.add(new Epic(1, "Epic 1", "Description Epic 1", "NEW", 1440L, Instant.ofEpochSecond(1664985600)));
        taskManager.add(new Subtask(1, "Subtask 1 Epic 1", "Description Subtask 1 Epic 1", "NEW",1440L, Instant.ofEpochSecond(1665158400), 3));
        taskManager.add(new Subtask(1, "Subtask 2 Epic 1", "Description Subtask 2 Epic 1", "NEW",1440L, Instant.ofEpochSecond(1665331200), 3));
        taskManager.add(new Epic(1, "Epic 2", "Description Epic 2", "NEW", 1440L, Instant.ofEpochSecond(1665504000)));
        taskManager.add(new Subtask(1, "Subtask 1 Epic 2", "Description Subtask 1 Epic 2", "NEW",1440L, Instant.ofEpochSecond(1665676800), 6));
        System.out.println("All tasks: " + '\n' + taskManager.getAllTasks());
        System.out.println("All epics: " + '\n' + taskManager.getAllEpics());
        System.out.println("All subtasks: " + '\n' + taskManager.getAllSubtasks());
        taskManager.update(new Task(1, "Task 1", "Changed Description Task 1", "IN_PROGRESS", 1440L, Instant.ofEpochSecond(1664640000)));
        taskManager.update(new Subtask(4, "Subtask 1 Epic 1", "Changed Description Subtask 1 Epic 1", "IN_PROGRESS",1440L, Instant.ofEpochSecond(1665158400), taskManager.getSubtasksMap().get(4).getEpicId()));
        taskManager.update(new Subtask(7, "Subtask 1 Epic 2", "Changed Description Subtask 1 Epic 2", "DONE", 1440L, Instant.ofEpochSecond(1665676800), taskManager.getSubtasksMap().get(7).getEpicId()));
        System.out.println("All tasks: " + '\n' + taskManager.getAllTasks());
        System.out.println("All epics: " + '\n' + taskManager.getAllEpics());
        System.out.println("All subtasks: " + '\n' + taskManager.getAllSubtasks());
        System.out.println(taskManager.getHistory().toString());
        taskManager.deleteOneSubTask(4);
        taskManager.deleteOneEpic(3);
        taskManager.deleteOneTask(1);
        System.out.println("All tasks: " + '\n' + taskManager.getAllTasks());
        System.out.println("All epics: " + '\n' + taskManager.getAllEpics());
        System.out.println("All subtasks: " + '\n' + taskManager.getAllSubtasks());
        System.out.println(taskManager.getHistory().toString());
        System.out.println("Browsing task 2: " + '\n' + taskManager.getOneTask(2));
        System.out.println(taskManager.getHistory().toString());
        System.out.println("Prior tasks: " + taskManager.getPrioritizedTasks());
        System.out.println("");
        System.out.println("");
        System.out.println("Check load and save for server");
        taskManager.save();
        taskManager.load();
        //kvServer.stop();
    }
}
