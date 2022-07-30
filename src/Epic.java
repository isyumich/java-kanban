public class Epic extends Task{
    public Epic(int taskId, String taskName, String taskDescription, String taskStatus) {
        super(taskId, taskName, taskDescription, taskStatus);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }
}
