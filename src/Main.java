//public class Main {
//
//    public static void main(String[] args) {
//        InMemoryTaskManager<Task> inMemoryTaskManager = new InMemoryTaskManager<>();
//        inMemoryTaskManager.add(new Task(1, "Task 1", "Description Task 1", "NEW"));
//        inMemoryTaskManager.add(new Task(1, "Task 2", "Description Task 2", "NEW"));
//        inMemoryTaskManager.add(new Epic(1, "Epic 1", "Description Epic 1", "NEW"));
//        inMemoryTaskManager.add(new Subtask(1, "Subtask 1 Epic 1", "Description Subtask 1 Epic 1", "NEW", 3));
//        inMemoryTaskManager.add(new Subtask(1, "Subtask 2 Epic 1", "Description Subtask 2 Epic 1", "NEW", 3));
//        inMemoryTaskManager.add(new Epic(1, "Epic 2", "Description Epic 2", "NEW"));
//        inMemoryTaskManager.add(new Subtask(1, "Subtask 1 Epic 2", "Description Subtask 1 Epic 2", "NEW", 6));
//        System.out.println("Все задачи: " + '\n' + inMemoryTaskManager.getAllTasks());
//        System.out.println("Все эпики: " + '\n' + inMemoryTaskManager.getAllEpics());
//        System.out.println("Все подзадачи: " + '\n' + inMemoryTaskManager.getAllSubTasks());
//        inMemoryTaskManager.update(new Task(1, "Task 1", "Changed Description задачи 1", "IN_PROGRESS"));
//        inMemoryTaskManager.update(new Subtask(4, "Subtask 1 Эпика 1", "Changed Description Subtask 1 Epic 1", "IN_PROGRESS", inMemoryTaskManager.subtasks.get(4).getEpicId()));
//        inMemoryTaskManager.update(new Subtask(7, "Subtask 1 Эпика 2", "Changed Description Subtask 1 Epic 2", "DONE", inMemoryTaskManager.subtasks.get(7).getEpicId()));
//        System.out.println("Все задачи: " + '\n' + inMemoryTaskManager.getAllTasks());
//        System.out.println("Все эпики: " + '\n' + inMemoryTaskManager.getAllEpics());
//        System.out.println("Все подзадачи: " + '\n' + inMemoryTaskManager.getAllSubTasks());
//        System.out.println(inMemoryTaskManager.getHistory().toString());
//        inMemoryTaskManager.deleteOneSubTask(4);
//        inMemoryTaskManager.deleteOneEpic(3);
//        inMemoryTaskManager.deleteOneTask(1);
//        System.out.println("Все задачи: " + '\n' + inMemoryTaskManager.getAllTasks());
//        System.out.println("Все эпики: " + '\n' + inMemoryTaskManager.getAllEpics());
//        System.out.println("Все подзадачи: " + '\n' + inMemoryTaskManager.getAllSubTasks());
//        System.out.println(inMemoryTaskManager.getHistory().toString());
//        System.out.println("Просмотр задачи 2: " + '\n' + inMemoryTaskManager.getOneTask(2));
//        System.out.println(inMemoryTaskManager.getHistory().toString());
//    }
//}
