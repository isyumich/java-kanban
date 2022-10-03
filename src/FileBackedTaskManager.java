import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager<T> extends InMemoryTaskManager<T> {
    protected File file;
    protected static final String FIRST_ROW_CSV = "id,type,name,status,description,startTime,duration,epicId\n";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main (String [] args) {
        FileBackedTaskManager<Task> fileBackedTaskManager = new FileBackedTaskManager<>(new File("files\\TaskManager.csv"));
        fileBackedTaskManager.add(new Task(1, "Задача 1", "Описание задачи 1", "NEW"));
        fileBackedTaskManager.add(new Task(1, "Задача 2", "Описание задачи 2", "NEW"));
        fileBackedTaskManager.add(new Epic(1, "Эпик 1", "Описание Эпика 1", "NEW"));
        fileBackedTaskManager.add(new Subtask(1, "Подзадача 1 Эпика 1", "Описание подзадачи 1 Эпика 1", "NEW", 3));
        fileBackedTaskManager.add(new Subtask(1, "Подзадача 2 Эпика 1", "Описание подзадачи 2 Эпика 1", "NEW", 3));
        fileBackedTaskManager.add(new Epic(1, "Эпик 2", "Описание Эпика 2", "NEW"));
        fileBackedTaskManager.add(new Subtask(1, "Подзадача 1 Эпика 2", "Описание подзадачи 1 Эпика 2", "NEW", 6));
        System.out.println("Все задачи: " + '\n' + fileBackedTaskManager.getAllTasks());
        System.out.println("Все эпики: " + '\n' + fileBackedTaskManager.getAllEpics());
        System.out.println("Все подзадачи: " + '\n' + fileBackedTaskManager.getAllSubTasks());
        System.out.println("");
        System.out.println("");
        fileBackedTaskManager.update(new Task(1, "Задача 1", "Исправленное описание задачи 1", "IN_PROGRESS"));
        fileBackedTaskManager.update(new Subtask(4, "Подзадача 1 Эпика 1", "Исправленное Описание подзадачи 1 Эпика 1", "IN_PROGRESS", fileBackedTaskManager.subtasks.get(4).getEpicId()));
        fileBackedTaskManager.update(new Subtask(7, "Подзадача 1 Эпика 2", "Исправленное Описание подзадачи 1 Эпика 2", "DONE", fileBackedTaskManager.subtasks.get(7).getEpicId()));
        System.out.println("Все задачи: " + '\n' + fileBackedTaskManager.getAllTasks());
        System.out.println("Все эпики: " + '\n' + fileBackedTaskManager.getAllEpics());
        System.out.println("Все подзадачи: " + '\n' + fileBackedTaskManager.getAllSubTasks());
        System.out.println(fileBackedTaskManager.getHistory().toString());
        System.out.println("");
        System.out.println("");
        fileBackedTaskManager.deleteOneSubTask(4);
        fileBackedTaskManager.deleteOneEpic(3);
        fileBackedTaskManager.deleteOneTask(1);
        System.out.println("Все задачи: " + '\n' + fileBackedTaskManager.getAllTasks());
        System.out.println("Все эпики: " + '\n' + fileBackedTaskManager.getAllEpics());
        System.out.println("Все подзадачи: " + '\n' + fileBackedTaskManager.getAllSubTasks());
        System.out.println(fileBackedTaskManager.getHistory().toString());
        System.out.println("");
        System.out.println("");
        System.out.println("Просмотр задачи 2: " + '\n' + fileBackedTaskManager.getOneTask(2));
        System.out.println(fileBackedTaskManager.getHistory().toString());
    }


    public void loadFromFile (File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.equals("")) {
                    break;
                }
                Task taskFromString = fromString(line);
                if (taskFromString instanceof Subtask) {
                    add((Subtask) taskFromString);
                } else if (taskFromString instanceof Epic) {
                    add((Epic) taskFromString);
                } else {
                    add(taskFromString);
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
            throw new ManagerSaveException("Не удалось прочитать файл");
        }
    }

    public void save () {
        try {
            if (Files.exists(file.toPath())) {
                Files.createFile(file.toPath());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось получить доступ к файлу");
        }

        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(FIRST_ROW_CSV);

            for (Task task : tasks.values()) {
                fileWriter.write(task.toString() + "\n");
            }
            for (Subtask subtask : subtasks.values()) {
                fileWriter.write(subtask.toString() + "\n");
            }
            for (Epic epic : epics.values()) {
                fileWriter.write(epic.toString() + "\n");
            }

            fileWriter.write("\n");

            fileWriter.write(historyToString(inMemoryHistoryManager));
        }
        catch (IOException e) {
            throw new ManagerSaveException("Не удалось выполнить запись в файл");
        }
    }

    private static Task fromString (String value) {
        String [] values = value.split(",");
        int taskId = Integer.parseInt(values[0]);
        String taskType = values[1];
        String taskName = values[2];
        String taskDescription = values[4];
        String taskStatus = values[3];
        int epicId = taskType.equals("SUBTASKS") ? Integer.parseInt(values[5]) : 0;
        return switch (taskType) {
            case ("EPIC") -> new Epic(taskId, taskName, taskDescription, taskStatus);
            case ("SUBTASK") -> new Subtask(taskId, taskName, taskDescription, taskStatus, epicId);
            default -> new Task(taskId, taskName, taskDescription, taskStatus);
        };
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

    public static String historyToString (HistoryManager<Task> manager){
        manager.getTasks();
        StringBuilder historyInfo = new StringBuilder();
        List<Task> lastViewTasks = manager.getLastViewTask();
        if (lastViewTasks.isEmpty()) {
            return "";
        }
        for (Task lastViewTask : lastViewTasks) {
            historyInfo.append(lastViewTask.taskId).append(",");
        }
        return historyInfo.toString();
    }

    @Override
    public void add(Task task){
        super.add(task);
        save();
    }

    @Override
    public void add(Epic epic){
        super.add(epic);
        save();
    }

    @Override
    public void add(Subtask subtask){
        super.add(subtask);
        save();
    }

    @Override
    public void addSubtaskToEpic(Subtask subtask){
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
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }
}
