import manager.HTTPTaskManager;
import manager.Managers;
import servers.KVServer;
import task.Epic;
import task.Subtask;
import task.Task;
import taskinfo.TasksStatus;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HTTPTaskManager taskManager = Managers.getDefault();

        Task firstTask = new Task(1, "T1", "Desc T1", TasksStatus.NEW.toString(), 1440L, Managers.createDate(1, 10));
        Task secondTask = new Task(1, "T2", "Desc T2", TasksStatus.NEW.toString(), 1440L, Managers.createDate(3, 10));
        Epic firstEpic = new Epic(1, "E1", "Desc E1", TasksStatus.NEW.toString(), 1440L, Managers.createDate(5, 10));
        Subtask firstSubtaskFirstEpic = new Subtask(1, "S1 E1", "Desc S1 E1", TasksStatus.NEW.toString(),1440L, Managers.createDate(7, 10), 3);
        Subtask secondSubtaskFirstEpic = new Subtask(1, "S2 E1", "Desc S2 E1", TasksStatus.NEW.toString(),1440L, Managers.createDate(9, 10), 3);
        Epic secondEpic = new Epic(1, "E2", "Desc E2", TasksStatus.NEW.toString(), 1440L, Managers.createDate(11, 10));
        Subtask firstSubtaskSecondEpic = new Subtask(1, "S1 E2", "Desc S1 E2", TasksStatus.NEW.toString(),1440L, Managers.createDate(13, 10), 6);

        taskManager.add(firstTask);
        taskManager.add(secondTask);
        taskManager.add(firstEpic);
        taskManager.add(firstSubtaskFirstEpic);
        taskManager.add(secondSubtaskFirstEpic);
        taskManager.add(secondEpic);
        taskManager.add(firstSubtaskSecondEpic);

        System.out.println("All tasks: " + '\n' + taskManager.getAllTasks());
        System.out.println("All epics: " + '\n' + taskManager.getAllEpics());
        System.out.println("All subtasks: " + '\n' + taskManager.getAllSubtasks());

        Task updatedFirstTask = new Task(1, "Task 1", "Changed Desc T1", TasksStatus.IN_PROGRESS.toString(), 1440L, Managers.createDate(1, 10));
        Subtask updatedFirstSubtaskFirstEpic = new Subtask(4, "S1 E1", "Changed Desc S1 E1", TasksStatus.IN_PROGRESS.toString(),1440L, Managers.createDate(7, 10), taskManager.getSubtasksMap().get(4).getEpicId());
        Subtask updatedFirstSubtaskSecondEpic = new Subtask(7, "S1 E2", "Changed Desc S1 E2", TasksStatus.DONE.toString(), 1440L, Managers.createDate(13, 10), taskManager.getSubtasksMap().get(7).getEpicId());

        taskManager.update(updatedFirstTask);
        taskManager.update(updatedFirstSubtaskFirstEpic);
        taskManager.update(updatedFirstSubtaskSecondEpic);
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

        taskManager.save();
        taskManager.load();
        System.out.println("All tasks: " + '\n' + taskManager.getAllTasks());
        System.out.println("All epics: " + '\n' + taskManager.getAllEpics());
        System.out.println("All subtasks: " + '\n' + taskManager.getAllSubtasks());
        System.out.println(taskManager.getHistory().toString());

        kvServer.stop();
    }
}
