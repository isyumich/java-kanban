import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

        int add (Task task);
        int add (Epic epic);
        int add (Subtask subtask);
        void addSubtaskToEpic (Subtask subtask);

        String getAllTasks ();
        String getOneTask (int id);
        String getAllSubTasks ();
        String getOneSubTask (int id);
        String getSubtasksOneEpic (int epicId);
        String getAllEpics ();
        String getOneEpic (int id);
        HistoryManager getHistory();

        void deleteAllTasks ();
        void deleteOneTask (int id);
        void deleteAllSubTasks ();
        void deleteOneSubTask (int id);
        void deleteSubtaskFromEpic (int id);
        void deleteAllEpics ();
        void deleteOneEpic (int id);

        void update (Task task);
        void update (Subtask subtask);
        void updateEpicStatus (Epic epic);
}
