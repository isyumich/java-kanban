package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import task.Epic;
import task.Subtask;
import task.Task;
import taskinfo.TasksStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int nextId = 1;

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected Gson gson = new GsonBuilder().create();

    protected final Comparator<Task> taskStartTimeComparator = Comparator.comparing(Task::getStartTime);
    protected Set<Task> prioritizedTasks = new TreeSet<>(taskStartTimeComparator);

    protected InMemoryHistoryManager<Task> inMemoryHistoryManager = new InMemoryHistoryManager<>();


    /*Методы добавления задач*/
    @Override
    public int add(Task task){
        if (task == null) {
            throw new NullPointerException("You can't create empty task");
        }
        if (checkFreeTimeForTask(task)) {
            task.setTaskId(nextId++);
            tasks.put(task.getTaskId(), task);
            addToPrioritizedTasks(task);
            return task.getTaskId();
        } else {
            System.out.println("You can't create task for this time");
            return 0;
        }
    }
    @Override
    public int add(Epic epic){
        if (epic == null) {
            throw new NullPointerException("You can't create empty epic");
        }
        epic.setTaskId(nextId++);
        update(epic);
        epics.put(epic.getTaskId(), epic);
        return epic.getTaskId();
    }
    @Override
    public int add(Subtask subtask){
        if (subtask == null) {
            throw new NullPointerException("You can't create empty subtask");
        }
        if (checkFreeTimeForTask(subtask)) {
            subtask.setTaskId(nextId++);
            subtasks.put(subtask.getTaskId(), subtask);
            addSubtaskToEpic(subtask);
            addToPrioritizedTasks(subtask);
            return subtask.getTaskId();
        } else {
            System.out.println("You can't create task for this time");
            return 0;
        }
    }
    @Override
    public void addSubtaskToEpic(Subtask subtask){
        ArrayList<Integer> subtasksList = new ArrayList<>();
        int epicID = subtask.getEpicId();
        for (Integer integer : subtasks.keySet()) {
            if (subtasks.get(integer).getEpicId() == epicID) {
                subtasksList.add(integer);
            }
        }
        epics.get(epicID).setSubtasksIds(subtasksList);
        update(epics.get(epicID));
    }
    @Override
    public void addToPrioritizedTasks (Task task) {
        prioritizedTasks.add(task);
    }

    /*Методы получения информации по задачам*/
    @Override
    public String getAllTasks() {
        StringBuilder allTasksInfo = new StringBuilder();
        for (Integer integer : tasks.keySet()) {
            allTasksInfo.append(getOneTask(integer)).append('\n');
        }
        return allTasksInfo.toString();
    }
    @Override
    public String getOneTask(int id) {
        inMemoryHistoryManager.add(tasks.get(id));
        return tasks.get(id).toString();
    }
    @Override
    public String getAllSubtasks() {
        StringBuilder allSubTasksInfo = new StringBuilder();
        for (Integer integer : subtasks.keySet()) {
            allSubTasksInfo.append(getOneSubTask(integer)).append('\n');
        }
        return allSubTasksInfo.toString();
    }
    @Override
    public String getOneSubTask(int id) {
        inMemoryHistoryManager.add(subtasks.get(id));
        return subtasks.get(id).toString();
    }
    @Override
    public String getSubtasksOneEpic(int epicId) {
        ArrayList<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
        StringBuilder allSubTasksInfo = new StringBuilder();
        for (Integer subtaskId : subtasksIds) {
            allSubTasksInfo.append(getOneSubTask(subtaskId)).append('\n');
        }
        return allSubTasksInfo.toString();
    }
    @Override
    public String getAllEpics() {
        StringBuilder allEpicsInfo = new StringBuilder();
        for (Integer integer : epics.keySet()) {
            allEpicsInfo.append(getOneEpic(integer)).append('\n');
        }
        return allEpicsInfo.toString();
    }
    @Override
    public String getOneEpic(int id) {
        inMemoryHistoryManager.add(epics.get(id));
        return epics.get(id).toString();
    }
    @Override
    public HistoryManager<Task> getHistory() {
        return Managers.getDefaultHistory(inMemoryHistoryManager);
    }
    @Override
    public HashMap<Integer, Task> getTasksMap() {
        return tasks;
    }
    @Override
    public HashMap<Integer, Epic> getEpicsMap() {
        return epics;
    }
    @Override
    public HashMap<Integer, Subtask> getSubtasksMap() {
        return subtasks;
    }
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    /*Методы удаления задач*/
    @Override
    public void deleteAllTasks() {
        for (Integer tasksId : tasks.keySet()) {
            if (inMemoryHistoryManager.mapLastViewTasks.get(tasksId) != null) {
                inMemoryHistoryManager.removeNode(inMemoryHistoryManager.mapLastViewTasks.get(tasksId));
            }
            prioritizedTasks.remove(tasks.get(tasksId));
        }
        tasks.clear();
    }
    @Override
    public void deleteOneTask(int id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        if (inMemoryHistoryManager.mapLastViewTasks.get(id) != null) {
            inMemoryHistoryManager.removeNode(inMemoryHistoryManager.mapLastViewTasks.get(id));
        }
    }
    @Override
    public void deleteAllSubTasks() {
        for (Integer subtasksId : subtasks.keySet()) {
            if (inMemoryHistoryManager.mapLastViewTasks.get(subtasksId) != null) {
                inMemoryHistoryManager.removeNode(inMemoryHistoryManager.mapLastViewTasks.get(subtasksId));
            }
            prioritizedTasks.remove(subtasks.get(subtasksId));
        }
        subtasks.clear();
        for (Integer epicsId : epics.keySet()) {
            Epic epic = epics.get(epicsId);
            update(epic);
            epic.setSubtasksIds(new ArrayList<>());
        }
    }
    @Override
    public void deleteOneSubTask(int id) {
        deleteSubtaskFromEpic(id);
        prioritizedTasks.remove(subtasks.get(id));
        subtasks.remove(id);
        if (inMemoryHistoryManager.mapLastViewTasks.get(id) != null) {
            inMemoryHistoryManager.removeNode(inMemoryHistoryManager.mapLastViewTasks.get(id));
        }
    }
    @Override
    public void deleteSubtaskFromEpic(int id) {
        int epicId = subtasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);
        update(epic);
        ArrayList<Integer> newSubtasksIds = epic.getSubtasksIds();
        newSubtasksIds.remove(newSubtasksIds.indexOf(id));
        epic.setSubtasksIds(newSubtasksIds);
    }

    @Override
    public void deleteAllEpics() {
        for (Integer subtasksId : subtasks.keySet()) {
            if (inMemoryHistoryManager.mapLastViewTasks.get(subtasksId) != null) {
                inMemoryHistoryManager.removeNode(inMemoryHistoryManager.mapLastViewTasks.get(subtasksId));
            }
            prioritizedTasks.remove(subtasks.get(subtasksId));
        }
        for (Integer epicId : epics.keySet()) {
            if (inMemoryHistoryManager.mapLastViewTasks.get(epicId) != null) {
                inMemoryHistoryManager.removeNode(inMemoryHistoryManager.mapLastViewTasks.get(epicId));
            }
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteOneEpic(int id) {
        ArrayList<Integer> subtasksIds = epics.get(id).getSubtasksIds();
        epics.remove(id);
        for (Integer subtasksId : subtasksIds) {
            prioritizedTasks.remove(subtasks.get(subtasksId));
            subtasks.remove(subtasksId);
            if (inMemoryHistoryManager.mapLastViewTasks.get(subtasksId) != null) {
                inMemoryHistoryManager.removeNode(inMemoryHistoryManager.mapLastViewTasks.get(subtasksId));
            }
        }
        if (inMemoryHistoryManager.mapLastViewTasks.get(id) != null) {
            inMemoryHistoryManager.removeNode(inMemoryHistoryManager.mapLastViewTasks.get(id));
        }
    }

    /*Методы обновления данных в задачах*/
    @Override
    public void update(Task task) {
        if (task == null) {
            throw new NullPointerException("You can't create empty task");
        }
        if (checkFreeTimeForTask(task)) {
            tasks.put(task.getTaskId(), task);
            addToPrioritizedTasks(task);
        } else {
            System.out.println("You can't create task for this time");
        }

    }
    @Override
    public void update(Subtask subtask) {
        if (subtask == null) {
            throw new NullPointerException("You can't create empty subtask");
        }
        if (checkFreeTimeForTask(subtask)) {
            subtasks.put(subtask.getTaskId(), subtask);

            Epic epic = epics.get(subtask.getEpicId());
            update(epic);
            addToPrioritizedTasks(subtask);
        } else {
            System.out.println("You can't create task for this time");
        }
    }
    @Override
    public void update(Epic epic) {
        if (epic == null) {
            throw new NullPointerException("You can't create empty epic");
        }
        int counterProgressStatus = 0;
        int counterDoneStatus = 0;
        if (!subtasks.isEmpty()) {
            List<Integer> subtasksIds = epic.getSubtasksIds();
            long duration = 0;
            LocalDateTime startTime = LocalDateTime.of(1970, 1, 1, 12,0);
            LocalDateTime endTime = LocalDateTime.of(1970, 1, 1, 12,0);
            for (int i = 0; i < subtasksIds.size(); i++) {
                Subtask subtask = subtasks.get(subtasksIds.get(i));
                duration += subtask.getDuration();
                if (subtask.getStartTime().isBefore(startTime)) {
                        startTime = subtask.getStartTime();
                }
                if (subtask.getEndTime().isAfter((endTime))) {
                        endTime = subtask.getEndTime();
                }
                if (subtask.getStatus().equals(String.valueOf(TasksStatus.IN_PROGRESS))) {
                    counterProgressStatus++;
                }
                if (subtask.getStatus().equals(String.valueOf(TasksStatus.DONE))) {
                    counterDoneStatus++;
                }
            }
            epic.setEndTime(endTime);
            epic.setStartTime(startTime);
            epic.setDuration(duration);
        }
        if (counterDoneStatus == epic.getSubtasksIds().size() && counterDoneStatus != 0) {
            epic.setStatus(TasksStatus.DONE);
        } else if ((counterProgressStatus == 0 && counterDoneStatus == 0) || epic.getSubtasksIds().isEmpty()) {
            epic.setStatus(TasksStatus.NEW);
        } else {
            epic.setStatus(TasksStatus.IN_PROGRESS);
        }
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public boolean checkFreeTimeForTask(Task newTask) {
        List<Task> prioritizedTasks = getPrioritizedTasks();
        int counterSameTimeTasks = 0;
        for (Task task : prioritizedTasks) {
            if (!newTask.equals(task) && newTask.getEndTime().isAfter(task.getStartTime()) && newTask.getEndTime().isBefore(task.getEndTime())) {
                counterSameTimeTasks ++;
            }
        }
        if(counterSameTimeTasks > 0) {
            return false;
        } else {
            return true;
        }
    }
}
