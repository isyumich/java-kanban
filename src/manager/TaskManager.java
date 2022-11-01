package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    /*Методы добавления задач*/
    int add(Task task);
    int add(Epic epic);
    int add(Subtask subtask);
    void addSubtaskToEpic(Subtask subtask);
    void addToPrioritizedTasks(Task task);

    /*Методы получения информации по задачам*/
    String getAllTasks();
    String getOneTask(int id);
    String getAllSubtasks();
    String getOneSubTask(int id);
    String getSubtasksOneEpic(int epicId);
    String getAllEpics();
    String getOneEpic(int id);
    HistoryManager<Task> getHistory();
    HashMap<Integer, Task> getTasksMap();
    HashMap<Integer, Epic> getEpicsMap();
    HashMap<Integer, Subtask> getSubtasksMap();
    List<Task> getPrioritizedTasks();

    /*Методы удаления задач*/
    void deleteAllTasks();
    void deleteOneTask(int id);
    void deleteAllSubTasks();
    void deleteOneSubTask(int id);
    void deleteSubtaskFromEpic(int id);
    void deleteAllEpics();
    void deleteOneEpic(int id);

    /*Методы обновления данных в задачах*/
    void update(Task task);
    void update(Subtask subtask);
    void update(Epic epic);

    boolean checkFreeTimeForTask(Task newTask);

}
