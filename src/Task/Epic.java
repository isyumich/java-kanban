package Task;

import TasksInfo.TasksType;

import java.time.Instant;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtasksIds;
    protected Instant endTime = startTime.plusSeconds((duration * SECONDS_IN_MINUTES));

    public Epic(int taskId, String name, String description, String status, long duration, Instant startTime) {
        super(taskId, name, description, status, duration, startTime);
        subtasksIds = new ArrayList<>();
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return taskId + "," + TasksType.EPIC + "," + name + ","
                + status + "," + description + "," + duration + "," + startTime + ",";
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }
}
