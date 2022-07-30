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
        return subtask.getTaskId();
    }

    public void update (Task task) {
        tasks.put(task.getTaskId(), task);
    }

    public void update (Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    public void update (Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());

        updateEpicStatus(epic);
    }

    public void updateEpicStatus (Epic epic) {
        String taskStatus = "NEW";
        int counterProgressStatus = 0;
        int counterDoneStatus = 0;
        for (Integer subtasksId : epic.getSubtasksIds()) {
            Subtask subtask = subtasks.get(subtasksId);
            if (subtask.getTaskStatus() == "IN_PROGRESS") {
                counterProgressStatus++;
            }
            if (subtask.getTaskStatus() == "DONE") {
                counterDoneStatus++;
            }
        }
        if (counterProgressStatus == epic.getSubtasksIds().size()) {
            epic.setTaskStatus("IN_PROGRESS");
        }
        if (counterDoneStatus == epic.getSubtasksIds().size()) {
            epic.setTaskStatus("DONE");
        }
    }
}
