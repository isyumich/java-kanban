import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int nextId = 1;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public int add (Task task) {
        task.setTaskId(nextId++);
        tasks.put(task.getTaskId(), task);
        return task.getTaskId();
    }

    public int add (Epic epic) {
        epic.setTaskId(nextId++);
        epics.put(epic.getTaskId(), epic);
        return epic.getTaskId();
    }

    public int add (Subtask subtask) {
        subtask.setTaskId(nextId++);
        subtasks.put(subtask.getTaskId(), subtask);
        addSubtaskToEpic(subtask);
        return subtask.getTaskId();
    }

    public String getAllTasks () {
        StringBuilder allTasksInfo = new StringBuilder();
        for (Integer integer : tasks.keySet()) {
            allTasksInfo.append(getOneTask(integer));
        }
        return allTasksInfo.toString();
    }

    public String getOneTask (int id) {
        return tasks.get(id).toString();
    }

    public String getAllSubTasks () {
        StringBuilder allSubTasksInfo = new StringBuilder();
        for (Integer integer : subtasks.keySet()) {
            allSubTasksInfo.append(getOneSubTask(integer));
        }
        return allSubTasksInfo.toString();
    }

    public String getOneSubTask (int id) {
        return subtasks.get(id).toString();
    }

    public String getSubtasksOneEpic (int epicId) {
        ArrayList<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
        StringBuilder allSubTasksInfo = new StringBuilder();
        for (Integer subtaskId : subtasksIds) {
            allSubTasksInfo.append(getOneSubTask(subtaskId));
        }
        return allSubTasksInfo.toString();
    }

    public String getAllEpics () {
        StringBuilder allEpicsInfo = new StringBuilder();
        for (Integer integer : epics.keySet()) {
            allEpicsInfo.append(getOneEpic(integer));
        }
        return allEpicsInfo.toString();
    }

    public String getOneEpic (int id) {
        return epics.get(id).toString();
    }

    public void deleteAllTasks () {
        tasks.clear();
    }

    public void deleteOneTask (int id) {
        tasks.remove(id);
    }

    public void deleteAllSubTasks () {
        subtasks.clear();
        for (Integer integer : epics.keySet()) {
            Epic epic = epics.get(integer);
            updateEpicStatus(epic);
            epic.setSubtasksIds(new ArrayList<>());
        }
    }

    public void deleteOneSubTask (int id) {
        deleteSubtaskFromEpic(id);
        subtasks.remove(id);
    }

    public void deleteSubtaskFromEpic (int id) {
        int epicId = subtasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);
        updateEpicStatus(epic);
        ArrayList<Integer> newSubtasksIds = epic.getSubtasksIds();
        newSubtasksIds.remove(newSubtasksIds.indexOf(id));
        epic.setSubtasksIds(newSubtasksIds);
    }

    public void deleteAllEpics () {
        subtasks.clear();
        epics.clear();
    }
    public void deleteOneEpic (int id) {
        ArrayList<Integer> subtasksIds = epics.get(id).getSubtasksIds();
        epics.remove(id);
        for (Integer subtasksId : subtasksIds) {
            subtasks.remove(subtasksId);
        }
    }

    public void update (Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void update (Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }

    public void updateEpicStatus (Epic epic) {
        int counterProgressStatus = 0;
        int counterDoneStatus = 0;
        if (!subtasks.isEmpty()) {
            for (Integer subtasksId : epic.getSubtasksIds()) {
                Subtask subtask = subtasks.get(subtasksId);
                if (subtask.getTaskStatus().equals("IN_PROGRESS")) {
                    counterProgressStatus++;
                }
                if (subtask.getTaskStatus().equals("DONE")) {
                    counterDoneStatus++;
                }
            }
        }
        if (counterDoneStatus == epic.getSubtasksIds().size() && counterDoneStatus != 0) {
            epic.setTaskStatus("DONE");
        } else if ((counterProgressStatus == 0 && counterDoneStatus == 0) || epic.getSubtasksIds().isEmpty()) {
            epic.setTaskStatus("NEW");
        } else {
            epic.setTaskStatus("IN_PROGRESS");
        }
    }

    public void addSubtaskToEpic (Subtask subtask) {
        ArrayList<Integer> subtasksList = new ArrayList<>();
        int epicID = subtask.epicId;
        for (Integer integer : subtasks.keySet()) {
            if (subtasks.get(integer).epicId == epicID) {
                subtasksList.add(integer);
            }
        }
        epics.get(epicID).setSubtasksIds(subtasksList);
    }

    /*public void deleteSubtaskFromEpic (int id) {
        int epicId = subtasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);
        updateEpicStatus(epic);
        ArrayList<Integer> newSubtasksIds = epic.getSubtasksIds();
        newSubtasksIds.remove(id);
        epic.setSubtasksIds(newSubtasksIds);
    }*/
}
