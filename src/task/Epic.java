package task;

import taskinfo.TasksType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtasksIds;
    protected LocalDateTime endTime = startTime.plusSeconds((duration * SECONDS_IN_MINUTES));

    public Epic(int taskId, String name, String description, String status, long duration, LocalDateTime startTime) {
        super(taskId, name, description, status, duration, startTime);
        subtasksIds = new ArrayList<>();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
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
