public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();
        manager.add(new Task(1, "Задача1", "Описание задачи 1", "NEW"));
        manager.add(new Task(1, "Задача2", "Описание задачи 2", "NEW"));
        manager.add(new Epic(1, "Эпик 1", "Описание Эпика 1", "NEW"));
        manager.add(new Subtask(1, "Подзадача 1 Эпика 1", "Описание подзадачи 1 Эпика 1", "NEW", 3));
        manager.add(new Subtask(1, "Подзадача 2 Эпика 1", "Описание подзадачи 2 Эпика 1", "NEW", 3));
        manager.add(new Epic(1, "Эпик 2", "Описание Эпика 2", "NEW"));
        manager.add(new Subtask(1, "Подзадача 1 Эпика 2", "Описание подзадачи 1 Эпика 2", "NEW", 6));
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
        System.out.println("Все подзадачи: " + manager.getAllSubTasks());
        System.out.println("");
        manager.update(new Task(1, "Задача 1", "Исправленное описание задачи 1", "IN_PROGRESS"));
        manager.update(new Subtask(4, "Подзадача 1 Эпика 1", "Исправленное Описание подзадачи 1 Эпика 1", "IN_PROGRESS", manager.subtasks.get(4).getEpicId()));
        manager.update(new Subtask(7, "Подзадача 1 Эпика 2", "Исправленное Описание подзадачи 1 Эпика 2", "DONE", manager.subtasks.get(7).getEpicId()));
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
        System.out.println("Все подзадачи: " + manager.getAllSubTasks());
        System.out.println("");
        manager.deleteOneSubTask(4);
        System.out.println("Все подзадачи: " + manager.getAllSubTasks());
        manager.deleteOneEpic(3);
        manager.deleteOneTask(1);
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
        System.out.println("Все подзадачи: " + manager.getAllSubTasks());
    }
}
