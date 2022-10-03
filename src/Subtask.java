public class Subtask extends Task {
    protected int epicId;

    public Subtask(int taskId, String taskName, String taskDescription, String taskStatus, int epicId) {
        super(taskId, taskName, taskDescription, taskStatus);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return taskId + ", " + TasksType.SUBTASK + ", " + taskName + ", "
                + taskStatus + ", " + taskDescription + ", " + epicId + '\n';
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
