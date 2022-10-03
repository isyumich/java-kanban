//public class Main {
//
//    public static void main(String[] args) {
//        InMemoryTaskManager<Task> inMemoryTaskManager = new InMemoryTaskManager<>();
//        inMemoryTaskManager.add(new Task(1, "Задача 1", "Описание задачи 1", "NEW"));
//        inMemoryTaskManager.add(new Task(1, "Задача 2", "Описание задачи 2", "NEW"));
//        inMemoryTaskManager.add(new Epic(1, "Эпик 1", "Описание Эпика 1", "NEW"));
//        inMemoryTaskManager.add(new Subtask(1, "Подзадача 1 Эпика 1", "Описание подзадачи 1 Эпика 1", "NEW", 3));
//        inMemoryTaskManager.add(new Subtask(1, "Подзадача 2 Эпика 1", "Описание подзадачи 2 Эпика 1", "NEW", 3));
//        inMemoryTaskManager.add(new Epic(1, "Эпик 2", "Описание Эпика 2", "NEW"));
//        inMemoryTaskManager.add(new Subtask(1, "Подзадача 1 Эпика 2", "Описание подзадачи 1 Эпика 2", "NEW", 6));
//        System.out.println("Все задачи: " + '\n' + inMemoryTaskManager.getAllTasks());
//        System.out.println("Все эпики: " + '\n' + inMemoryTaskManager.getAllEpics());
//        System.out.println("Все подзадачи: " + '\n' + inMemoryTaskManager.getAllSubTasks());
//        System.out.println("");
//        System.out.println("");
//        inMemoryTaskManager.update(new Task(1, "Задача 1", "Исправленное описание задачи 1", "IN_PROGRESS"));
//        inMemoryTaskManager.update(new Subtask(4, "Подзадача 1 Эпика 1", "Исправленное Описание подзадачи 1 Эпика 1", "IN_PROGRESS", inMemoryTaskManager.subtasks.get(4).getEpicId()));
//        inMemoryTaskManager.update(new Subtask(7, "Подзадача 1 Эпика 2", "Исправленное Описание подзадачи 1 Эпика 2", "DONE", inMemoryTaskManager.subtasks.get(7).getEpicId()));
//        System.out.println("Все задачи: " + '\n' + inMemoryTaskManager.getAllTasks());
//        System.out.println("Все эпики: " + '\n' + inMemoryTaskManager.getAllEpics());
//        System.out.println("Все подзадачи: " + '\n' + inMemoryTaskManager.getAllSubTasks());
//        System.out.println(inMemoryTaskManager.getHistory().toString());
//        System.out.println("");
//        System.out.println("");
//        inMemoryTaskManager.deleteOneSubTask(4);
//        inMemoryTaskManager.deleteOneEpic(3);
//        inMemoryTaskManager.deleteOneTask(1);
//        System.out.println("Все задачи: " + '\n' + inMemoryTaskManager.getAllTasks());
//        System.out.println("Все эпики: " + '\n' + inMemoryTaskManager.getAllEpics());
//        System.out.println("Все подзадачи: " + '\n' + inMemoryTaskManager.getAllSubTasks());
//        System.out.println(inMemoryTaskManager.getHistory().toString());
//        System.out.println("");
//        System.out.println("");
//        System.out.println("Просмотр задачи 2: " + '\n' + inMemoryTaskManager.getOneTask(2));
//        System.out.println(inMemoryTaskManager.getHistory().toString());
//    }
//}
