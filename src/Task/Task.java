package Task;

import TasksInfo.TasksStatus;
import TasksInfo.TasksType;

import java.time.Instant;

public class Task {
    protected final int SECONDS_IN_MINUTES = 60;
    protected int taskId;
    protected String name;
    protected String description;
    protected String status;
    protected long duration;
    protected Instant startTime;

    public Task(int taskId, String name, String Description, String Status, long duration, Instant startTime) {
        this.taskId = taskId;
        this.name = name;
        this.description = Description;
        this.status = Status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }
    public void setName(String taskName) {
        this.name = taskName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String taskDescription) {
        this.description = taskDescription;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(TasksStatus tasksStatus) {
        this.status = String.valueOf(tasksStatus);
    }

    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Instant getStartTime() {
        return startTime;
    }
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return startTime.plusSeconds(SECONDS_IN_MINUTES * duration);
    }

    @Override
    public String toString() {
        return taskId + "," + TasksType.TASK + "," + name + ","
                + status + "," + description + "," + duration + "," + startTime + ",";
    }
}
