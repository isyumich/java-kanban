package Task;

import TasksInfo.TasksType;

import java.time.Instant;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(int taskId, String name, String description, String status, long duration, Instant startTime, int epicId) {
        super(taskId, name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return taskId + "," + TasksType.SUBTASK + "," + name + ","
                + status + "," + description + "," + duration + "," + startTime + "," + epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
