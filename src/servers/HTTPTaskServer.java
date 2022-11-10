package servers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class HTTPTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final TaskManager taskManager;
    private final HttpServer httpServer;

    public HTTPTaskServer(TaskManager taskManager) throws IOException {
        this.httpServer = HttpServer.create();
        this.taskManager = taskManager;
    }

    public void startServer() throws IOException {
        httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();
    }

    public void stopServer() {
        httpServer.stop(1);
    }

    class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                URI requestURI = httpExchange.getRequestURI();
                String path = requestURI.getPath();
                String query = requestURI.getRawQuery();
                String method = httpExchange.getRequestMethod();
                switch (method) {
                    case "POST":
                        InputStream inputStream = httpExchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        if (path.endsWith("/task/")) {

                            Task task = gson.fromJson(taskBody, Task.class);
                            int taskId = task.getTaskId();
                            if (!taskManager.getTasksMap().containsKey(taskId)) {
                                taskManager.add(task);
                            } else {
                                taskManager.update(task);
                            }
                        } else if (path.endsWith("/subtask/")) {
                            Subtask subtask = gson.fromJson(taskBody, Subtask.class);
                            int subtaskId = subtask.getTaskId();
                            if (!taskManager.getSubtasksMap().containsKey(subtaskId)) {
                                taskManager.add(subtask);
                            } else {
                                taskManager.update(subtask);
                            }
                        } else if (path.endsWith("/epic/")) {
                            Epic epic = gson.fromJson(taskBody, Epic.class);
                            int epicId = epic.getTaskId();
                            if (!taskManager.getEpicsMap().containsKey(epicId)) {
                                taskManager.add(epic);
                            } else {
                                taskManager.update(epic);
                            }
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        httpExchange.close();
                        break;
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        if (query != null) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (path.endsWith("/subtask/epic/")) {
                                ArrayList<Integer> allSubtasksIds = taskManager.getEpicsMap().get(id).getSubtasksIds();
                                ArrayList<Subtask> allSubtasksList = new ArrayList<>();
                                for (int number : allSubtasksIds) {
                                    for (int subtaskValue : taskManager.getSubtasksMap().keySet()) {
                                        if (number == subtaskValue) {
                                            allSubtasksList.add(taskManager.getSubtasksMap().get(subtaskValue));
                                        }
                                    }
                                }
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(allSubtasksList).getBytes());
                                }
                            } else if (path.endsWith("/subtask/")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getSubtasksMap().get(id)).getBytes());
                                }
                            } else if (path.endsWith("/epic/")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getEpicsMap().get(id)).getBytes());
                                }
                            } else if (path.endsWith("/task/")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getTasksMap().get(id)).getBytes());
                                }
                            }
                        } else {
                            if (path.endsWith("/task/")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getTasksMap()).getBytes());
                                }
                            } else if (path.endsWith("/subtask/")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getSubtasksMap()).getBytes());
                                }
                            } else if (path.endsWith("/epic/")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getEpicsMap()).getBytes());
                                }
                            } else if (path.endsWith("/history/")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getHistory()).getBytes());
                                }
                            } else if (path.endsWith("/tasks/")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getPrioritizedTasks()).getBytes());
                                }
                            }
                        }
                        httpExchange.close();
                        break;
                    case "DELETE":
                        if (query != null) {
                            int id = Integer.parseInt(query.split("=")[1]);
                            if (path.endsWith("/task/")) {
                                taskManager.deleteOneTask(id);
                            } else if (path.endsWith("/epic/")) {
                                taskManager.deleteOneEpic(id);
                            } else if (path.endsWith("/subtask/")) {
                                taskManager.deleteOneSubTask(id);
                            }
                        } else {
                            if (path.endsWith("/task/")) {
                                taskManager.deleteAllTasks();
                            } else if (path.endsWith("/subtask/")) {
                                taskManager.deleteAllSubTasks();
                            } else if (path.endsWith("/epic/")) {
                                taskManager.deleteAllEpics();
                            }
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        httpExchange.close();
                        break;
                    default:
                        break;
                }

            } catch (IOException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.");
            }

        }
    }
}
