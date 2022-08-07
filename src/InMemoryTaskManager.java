import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();


    @Override
    public int add (Task task) {
        task.setTaskId(nextId++);
        tasks.put(task.getTaskId(), task);
        return task.getTaskId();
    }
    @Override
    public int add (Epic epic) {
        epic.setTaskId(nextId++);
        epics.put(epic.getTaskId(), epic);
        return epic.getTaskId();
    }
    @Override
    public int add (Subtask subtask) {
        subtask.setTaskId(nextId++);
        subtasks.put(subtask.getTaskId(), subtask);
        addSubtaskToEpic(subtask);
        return subtask.getTaskId();
    }
    @Override
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
    @Override
    public String getAllTasks () {
        StringBuilder allTasksInfo = new StringBuilder();
        for (Integer integer : tasks.keySet()) {
            allTasksInfo.append(getOneTask(integer));
        }
        return allTasksInfo.toString();
    }
    @Override
    public String getOneTask (int id) {
        inMemoryHistoryManager.add(tasks.get(id));
        return tasks.get(id).toString();
    }
    @Override
    public String getAllSubTasks () {
        StringBuilder allSubTasksInfo = new StringBuilder();
        for (Integer integer : subtasks.keySet()) {
            allSubTasksInfo.append(getOneSubTask(integer));
        }
        return allSubTasksInfo.toString();
    }
    @Override
    public String getOneSubTask (int id) {
        inMemoryHistoryManager.add(subtasks.get(id));
        return subtasks.get(id).toString();
    }
    @Override
    public String getSubtasksOneEpic (int epicId) {
        ArrayList<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
        StringBuilder allSubTasksInfo = new StringBuilder();
        for (Integer subtaskId : subtasksIds) {
            allSubTasksInfo.append(getOneSubTask(subtaskId));
        }
        return allSubTasksInfo.toString();
    }
    @Override
    public HistoryManager getHistory() {
        return Managers.getDefaultHistory(inMemoryHistoryManager);
    }
    @Override
    public String getAllEpics () {
        StringBuilder allEpicsInfo = new StringBuilder();
        for (Integer integer : epics.keySet()) {
            allEpicsInfo.append(getOneEpic(integer));
        }
        return allEpicsInfo.toString();
    }
    @Override
    public String getOneEpic (int id) {
        inMemoryHistoryManager.add(epics.get(id));
        return epics.get(id).toString();
    }
    @Override
    public void deleteAllTasks () {
        tasks.clear();
    }
    @Override
    public void deleteOneTask (int id) {
        tasks.remove(id);
    }
    @Override
    public void deleteAllSubTasks () {
        subtasks.clear();
        for (Integer integer : epics.keySet()) {
            Epic epic = epics.get(integer);
            updateEpicStatus(epic);
            epic.setSubtasksIds(new ArrayList<>());
        }
    }
    @Override
    public void deleteOneSubTask (int id) {
        deleteSubtaskFromEpic(id);
        subtasks.remove(id);
    }
    @Override
    public void deleteSubtaskFromEpic (int id) {
        int epicId = subtasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);
        updateEpicStatus(epic);
        ArrayList<Integer> newSubtasksIds = epic.getSubtasksIds();
        newSubtasksIds.remove(newSubtasksIds.indexOf(id));
        epic.setSubtasksIds(newSubtasksIds);
    }
    @Override
    public void deleteAllEpics () {
        subtasks.clear();
        epics.clear();
    }
    @Override
    public void deleteOneEpic (int id) {
        ArrayList<Integer> subtasksIds = epics.get(id).getSubtasksIds();
        epics.remove(id);
        for (Integer subtasksId : subtasksIds) {
            subtasks.remove(subtasksId);
        }
    }
    @Override
    public void update (Task task) {
        tasks.put(task.getTaskId(), task);
    }
    @Override
    public void update (Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }
    @Override
    public void updateEpicStatus (Epic epic) {
        int counterProgressStatus = 0;
        int counterDoneStatus = 0;
        if (!subtasks.isEmpty()) {
            for (Integer subtasksId : epic.getSubtasksIds()) {
                Subtask subtask = subtasks.get(subtasksId);
                if (subtask.getTaskStatus().equals(String.valueOf(TasksStatus.IN_PROGRESS))) {
                    counterProgressStatus++;
                }
                if (subtask.getTaskStatus().equals(String.valueOf(TasksStatus.DONE))) {
                    counterDoneStatus++;
                }
            }
        }
        if (counterDoneStatus == epic.getSubtasksIds().size() && counterDoneStatus != 0) {
            epic.setTaskStatus(TasksStatus.DONE);
        } else if ((counterProgressStatus == 0 && counterDoneStatus == 0) || epic.getSubtasksIds().isEmpty()) {
            epic.setTaskStatus(TasksStatus.NEW);
        } else {
            epic.setTaskStatus(TasksStatus.IN_PROGRESS);
        }
    }
}
