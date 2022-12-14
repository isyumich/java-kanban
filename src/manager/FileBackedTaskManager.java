package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import taskinfo.TasksStatus;
import taskinfo.TasksType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    protected File file;
    protected static final String FIRST_ROW_CSV = "id,type,name,status,description,startTime,duration,epicId";

    public FileBackedTaskManager(String pathName) {
        this.file = new File(pathName);
    }


    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("src\\Manager\\TaskManager.csv");

        Task firstTask = new Task(1, "T1", "Desc T1", TasksStatus.NEW.toString(), 1440L, Managers.createDate(1, 10));
        Task secondTask = new Task(1, "T2", "Desc T2", TasksStatus.NEW.toString(), 1440L, Managers.createDate(3, 10));
        Epic firstEpic = new Epic(1, "E1", "Desc E1", TasksStatus.NEW.toString(), 1440L, Managers.createDate(5, 10));
        Epic secondEpic = new Epic(1, "E2", "Desc E2", TasksStatus.NEW.toString(), 1440L, Managers.createDate(11, 10));
        Subtask firstSubtaskFirstEpic = new Subtask(1, "S1 E1", "Desc S1 E1", TasksStatus.NEW.toString(),1440L, Managers.createDate(7, 10), 3);
        Subtask secondSubtaskFirstEpic = new Subtask(1, "S2 E1", "Desc S2 E1", TasksStatus.NEW.toString(),1440L, Managers.createDate(9, 10), 3);
        Subtask firstSubtaskSecondEpic = new Subtask(1, "S1 E2", "Desc S1 E2", TasksStatus.NEW.toString(),1440L, Managers.createDate(13, 10), 4);

        fileBackedTaskManager.add(firstTask);
        fileBackedTaskManager.add(secondTask);
        fileBackedTaskManager.add(firstEpic);
        fileBackedTaskManager.add(secondEpic);
        fileBackedTaskManager.add(firstSubtaskFirstEpic);
        fileBackedTaskManager.add(secondSubtaskFirstEpic);
        fileBackedTaskManager.add(firstSubtaskSecondEpic);

        Task updatedFirstTask = new Task(1, "Task 1", "Changed Desc T1", TasksStatus.IN_PROGRESS.toString(), 1440L, Managers.createDate(1, 10));
        Subtask updatedFirstSubtaskFirstEpic = new Subtask(5, "S1 E1", "Changed Desc S1 E1", TasksStatus.IN_PROGRESS.toString(),1440L, Managers.createDate(7, 10), fileBackedTaskManager.getSubtasksMap().get(5).getEpicId());
        Subtask updatedFirstSubtaskSecondEpic = new Subtask(7, "S1 E2", "Changed Desc S1 E2", TasksStatus.DONE.toString(), 1440L, Managers.createDate(13, 10), fileBackedTaskManager.getSubtasksMap().get(7).getEpicId());

        fileBackedTaskManager.update(updatedFirstTask);
        fileBackedTaskManager.update(updatedFirstSubtaskFirstEpic);
        fileBackedTaskManager.update(updatedFirstSubtaskSecondEpic);

        System.out.println("All tasks: " + '\n' + fileBackedTaskManager.getAllTasks());
        System.out.println("All epics: " + '\n' + fileBackedTaskManager.getAllEpics());
        System.out.println("All subtasks: " + '\n' + fileBackedTaskManager.getAllSubtasks());
        System.out.println("Browsing task 2: " + '\n' + fileBackedTaskManager.getOneTask(2));
        System.out.println(fileBackedTaskManager.getHistory().toString());

        FileBackedTaskManager fileBackedTaskManagerNew = new FileBackedTaskManager("src\\Manager\\TaskManager.csv");
        System.out.println("Watch info about new manager");
        fileBackedTaskManagerNew.loadFromFile(fileBackedTaskManagerNew.file);
        System.out.println(fileBackedTaskManagerNew.getHistory().toString());
    }


    public void loadFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.equals("")) {
                    break;
                }
                if (!line.equals(FIRST_ROW_CSV)) {
                    Task taskFromString = fromString(line);
                    if (taskFromString instanceof Subtask) {
                        add((Subtask) taskFromString);
                    } else if (taskFromString instanceof Epic) {
                        add((Epic) taskFromString);
                    } else {
                        add(taskFromString);
                    }
                }
            }

            String historyLine = br.readLine();
            for (int idTask : historyFromString(historyLine)) {
                if (tasks.containsKey(idTask)) {
                    inMemoryHistoryManager.add(tasks.get(idTask));
                } else if (subtasks.containsKey(idTask)) {
                    inMemoryHistoryManager.add(subtasks.get(idTask));
                } else {
                    inMemoryHistoryManager.add(epics.get(idTask));
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("???? ?????????????? ?????????????????? ????????");
        }
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(FIRST_ROW_CSV + '\n');
            for (Task task : tasks.values()) {
                fileWriter.write(task.toString() + '\n');
            }
            for (Epic epic : epics.values()) {
                fileWriter.write(epic.toString() + '\n');
            }
            for (Subtask subtask : subtasks.values()) {
                fileWriter.write(subtask.toString() + '\n');
            }
            fileWriter.write('\n');
            fileWriter.write(historyToString(inMemoryHistoryManager));
        } catch (IOException e) {
            throw new ManagerSaveException("???? ?????????????? ?????????????????? ???????????? ?? ????????");
        }
    }

    private static Task fromString(String value) {
        String[] values = value.split(",");
        int taskId = Integer.parseInt(values[0]);
        String taskType = values[1];
        String taskName = values[2];
        String taskDescription = values[4];
        String taskStatus = values[3];
        long duration = Long.parseLong(values[5]);
        LocalDateTime startTime = LocalDateTime.parse(values[6]);
        int epicId = taskType.equals(TasksType.SUBTASK.toString()) ? Integer.parseInt(values[7]) : 0;
        if (taskType.equals(TasksType.EPIC.toString())) {
            return new Epic(taskId, taskName, taskDescription, taskStatus, duration, startTime);
        } else if (taskType.equals(TasksType.SUBTASK.toString())) {
            return new Subtask(taskId, taskName, taskDescription, taskStatus, duration, startTime, epicId);
        } else {
            return new Task(taskId, taskName, taskDescription, taskStatus, duration, startTime);
        }
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> historyList = new ArrayList<>();
        if (value != null) {
            String[] idList = value.split(",");
            for (String idTask : idList) {
                historyList.add(Integer.parseInt(idTask));
            }
        }
        return historyList;
    }

    public static String historyToString(HistoryManager<Task> manager) {
        manager.getTasks();
        StringBuilder historyInfo = new StringBuilder();
        List<Task> lastViewTasks = manager.getLastViewTask();
        if (lastViewTasks.isEmpty()) {
            return "";
        }
        for (int i = 0; i < lastViewTasks.size(); i++) {
            if (i < lastViewTasks.size() - 1) {
                historyInfo.append(lastViewTasks.get(i).getTaskId()).append(",");
            } else {
                historyInfo.append(lastViewTasks.get(i).getTaskId());
            }
        }
        return historyInfo.toString();
    }

    @Override
    public int add(Task task) {
        super.add(task);
        save();
        return task.getTaskId();
    }

    @Override
    public int add(Epic epic) {
        super.add(epic);
        save();
        return epic.getTaskId();
    }

    @Override
    public int add(Subtask subtask) {
        super.add(subtask);
        save();
        return subtask.getTaskId();
    }

    @Override
    public void addSubtaskToEpic(Subtask subtask) {
        super.addSubtaskToEpic(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteOneTask(int id) {
        super.deleteOneTask(id);
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteOneSubTask(int id) {
        super.deleteOneSubTask(id);
        save();
    }

    @Override
    public void deleteSubtaskFromEpic(int id) {
        super.deleteSubtaskFromEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteOneEpic(int id) {
        super.deleteOneEpic(id);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public String getOneTask(int id) {
        inMemoryHistoryManager.add(tasks.get(id));
        return tasks.get(id).toString();
    }

    @Override
    public String getOneSubTask(int id) {
        inMemoryHistoryManager.add(subtasks.get(id));
        save();
        return subtasks.get(id).toString();
    }

    @Override
    public String getOneEpic(int id) {
        inMemoryHistoryManager.add(epics.get(id));
        save();
        return epics.get(id).toString();
    }

    @Override
    public HistoryManager<Task> getHistory() {
        save();
        return Managers.getDefaultHistory(inMemoryHistoryManager);
    }
}
