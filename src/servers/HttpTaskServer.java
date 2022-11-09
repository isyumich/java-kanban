package servers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final TaskManager taskManager = Managers.getDefault();
    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        this.httpServer = HttpServer.create();
    }

    public void startServer() throws IOException {
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();
    }

    public void stopServer() {
        httpServer.stop(1);
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                URI requestURI = httpExchange.getRequestURI();
                String path = requestURI.getPath();
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
                            if (!taskManager.getSubtasksMap().containsKey(epicId)) {
                                taskManager.add(epic);
                            } else {
                                taskManager.update(epic);
                            }
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write("".getBytes());
                        }
                        httpExchange.close();
                        break;
                    case "GET":
                        httpExchange.sendResponseHeaders(200, 0);
                        if (path.contains("?")) {
                            int id = Integer.parseInt(path.split("\\?")[1].split("=")[1]);
                            if (path.contains("/subtask/epic?id=")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getSubtasksOneEpic(id)).getBytes());
                                }
                            } else if (path.contains("/subtask/?id=")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getOneSubTask(id)).getBytes());
                                }
                            } else if (path.contains("/epic/?id=")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getOneEpic(id)).getBytes());
                                }
                            } else if (path.contains("/task/?id=")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getOneTask(id)).getBytes());
                                }
                            }
                        } else {
                            if (path.endsWith("/task/")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getAllTasks()).getBytes());
                                }
                            } else if (path.endsWith("/subtask/")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getAllSubtasks()).getBytes());
                                }
                            } else if (path.endsWith("/epic/")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getAllEpics()).getBytes());
                                }
                            } else if (path.endsWith("/history")) {
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(gson.toJson(taskManager.getHistory()).getBytes());
                                }
                            }
                        }
                        httpExchange.close();
                        break;
                    case "DELETE":
                        if (path.contains("?")) {
                            int id = Integer.parseInt(path.split("\\?")[1].split("=")[1]);
                            if (path.contains("/subtask/?id=")) {
                                taskManager.deleteOneTask(id);
                            } else if (path.contains("/epic/?id=")) {
                                taskManager.deleteOneEpic(id);
                            } else if (path.contains("/task/?id=")) {
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
