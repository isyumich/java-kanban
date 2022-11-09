package manager;

import clients.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HTTPTaskManager extends FileBackedTaskManager {
    protected KVTaskClient kvTaskClient = new KVTaskClient();
    protected Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public HTTPTaskManager() {
        super("src\\Manager\\TaskManager.csv");
    }


    public void save() {
        String jsonTask = gson.toJson(tasks);
        kvTaskClient.put("tasks", jsonTask);

        String jsonEpic = gson.toJson(epics);
        kvTaskClient.put("epics", jsonEpic);

        String jsonSubTask = gson.toJson(subtasks);
        kvTaskClient.put("subtasks", jsonSubTask);

        List<Task> history = getHistory().getLastViewTask();
        String jsonHistory = gson.toJson((history));
        kvTaskClient.put("history", jsonHistory);
    }

    public void load () {
    String jsonTasks = kvTaskClient.load("tasks");
    String jsonSubtasks = kvTaskClient.load("subtasks");
    String jsonEpics = kvTaskClient.load("epics");
    String jsonHistory = kvTaskClient.load("history");

    tasks = gson.fromJson(jsonTasks, new TypeToken<HashMap<Integer, Task>>(){}.getType());
    subtasks = gson.fromJson(jsonSubtasks, new TypeToken<HashMap<Integer, Subtask>>(){}.getType());
    epics = gson.fromJson(jsonEpics, new TypeToken<HashMap<Integer, Epic>>(){}.getType());
    ArrayList<Task> jsonHistoryArray= gson.fromJson(jsonHistory, new TypeToken<ArrayList<Task>>(){}.getType());


        for (Task task : jsonHistoryArray) {
            if (tasks.containsKey(task.getTaskId())) {
                inMemoryHistoryManager.add(task);
            } else if (subtasks.containsKey(task.getTaskId())) {
                inMemoryHistoryManager.add(subtasks.get(task));
            } else {
                inMemoryHistoryManager.add(epics.get(task));
            }
        }
    }
}
