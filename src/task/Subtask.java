package task;

import taskinfo.TasksType;

import java.time.Instant;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(int taskId, String name, String description, String status, long duration, LocalDateTime startTime, int epicId) {
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
