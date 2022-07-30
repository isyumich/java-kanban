import java.util.ArrayList;

public class Epic extends Task{
    protected ArrayList<Integer> subtasksIds;
    public Epic(int taskId, String taskName, String taskDescription, String taskStatus) {
        super(taskId, taskName, taskDescription, taskStatus);
        subtasksIds = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksIds=" + subtasksIds +
                ", taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }
}
