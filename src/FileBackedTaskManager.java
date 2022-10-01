import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    protected FileReader fileReader;
    protected BufferedReader br;
    public FileBackedTaskManager(FileReader fileReader) {
        this.fileReader = fileReader;
        br = new BufferedReader(this.fileReader);
    }

    public void save () {
        System.out.println("Пока не придумал логику");
    }

    @Override
    public void add (Task task) {
        super.add(task);
        save();
    }
    @Override
    public void add (Epic epic) {
        super.add(epic);
        save();
    }
    @Override
    public void add (Subtask subtask) {
        super.add(subtask);
        save();
    }
    @Override
    public void addSubtaskToEpic (Subtask subtask) {
        super.addSubtaskToEpic(subtask);
        save();
    }
}
