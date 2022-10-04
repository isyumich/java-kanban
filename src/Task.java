public class Task {
    protected int taskId;
    protected String taskName;
    protected String taskDescription;
    protected String taskStatus;

    public Task(int taskId, String taskName, String taskDescription, String taskStatus) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TasksStatus tasksStatus) {
        this.taskStatus = String.valueOf(tasksStatus);
    }

    @Override
    public String toString() {
        return taskId + "," + TasksType.TASK + "," + taskName + ","
                + taskStatus + "," + taskDescription + ",";
    }
}
