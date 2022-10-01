public interface TaskManager<T> {

        void add (Task task);
        void add (Epic epic);
        void add (Subtask subtask);
        void addSubtaskToEpic (Subtask subtask);

        String getAllTasks ();
        String getOneTask (int id);
        String getAllSubTasks ();
        String getOneSubTask (int id);
        String getSubtasksOneEpic (int epicId);
        String getAllEpics ();
        String getOneEpic (int id);
        HistoryManager<Task> getHistory();

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
