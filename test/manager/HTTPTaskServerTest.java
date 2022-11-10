package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.HTTPTaskServer;
import task.Epic;
import task.Subtask;
import task.Task;
import taskinfo.TasksStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HTTPTaskServerTest {
    protected HTTPTaskServer httpTaskServer;
    protected TaskManager taskManager;
    protected final Gson gson = new GsonBuilder().create();
    protected final String HOST = "http://localhost:8080/tasks/";

    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;

    @BeforeEach
    void beforeEach() throws IOException {
        taskManager = new FileBackedTaskManager("src\\Manager\\TaskManager.csv");
        httpTaskServer = new HTTPTaskServer(taskManager);

        taskManager.add(new Task(1, "Task1 ", "Description Task 1", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 1, 12, 0)));
        taskManager.add(new Epic(1, "Epic1 ", "Description Epic", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 5, 12, 0)));
        taskManager.add(new Subtask(1, "Subtask 1 for Epic 1", "Description Subtask 1 for Epic 1", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 3, 12, 0), 2));

        taskManager.add(new Task(1, "Task 2", "Description Task 2", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 7, 12, 0)));
        taskManager.add(new Epic(1, "Epic 2", "Description Epic 2", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 11, 12, 0)));
        taskManager.add(new Subtask(1, "Subtask 1 for Epic 2", "Description Subtask 1 for Epic 2 ", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 9, 12, 0), 2));

        httpTaskServer.startServer();
    }

    @AfterEach
    void afterEach() {
        httpTaskServer.stopServer();
    }

    @Test
    void shouldAddTask () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "task/");
        Task newTask = new Task(7, "Task", "Description Task", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 1, 12, 0));
        String json = gson.toJson(newTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        tasks = taskManager.getTasksMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertEquals(3, tasks.size(), "������ �� ���� ���������");
    }

    @Test
    void shouldAddSubtask () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "subtask/");
        Subtask newSubtask = new Subtask(7, "Subtask for Epic ", "Description Subtask for Epic", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 3, 12, 0), 2);
        String json = gson.toJson(newSubtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        subtasks = taskManager.getSubtasksMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertEquals(3, subtasks.size(), "��������� �� ���� ���������");
    }

    @Test
    void shouldAddEpic () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "epic/");
        Epic newEpic = new Epic(7, "Epic", "Description Epic", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 5, 12, 0));
        String json = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        epics = taskManager.getEpicsMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertEquals(3, epics.size(), "���� �� ��� ��������");
    }

    @Test
    void shouldUpdateTask () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "task/");
        Task newTask = new Task(1, "Task", "Changed Description Task", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 1, 12, 0));
        String json = gson.toJson(newTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        tasks = taskManager.getTasksMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertEquals(2, tasks.size(), "������ ���� ���������, � �� ���������");
        assertEquals("Changed Description Task", tasks.get(1).getDescription(), "������ ������ �� ��� ��������");
    }

    @Test
    void shouldUpdateSubtask () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "subtask/");
        Subtask newSubtask = new Subtask(3, "Subtask for Epic ", "Changed Description Subtask for Epic", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 3, 12, 0), 2);
        String json = gson.toJson(newSubtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        subtasks = taskManager.getSubtasksMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertEquals(2, subtasks.size(), "��������� ���� ���������, � �� ���������");
        assertEquals("Changed Description Subtask for Epic", subtasks.get(3).getDescription(), "������ ��������� �� ��� ��������");
    }

    @Test
    void shouldUpdateEpic () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "epic/");
        Epic newEpic = new Epic(2, "Epic", "Changed Description Epic", TasksStatus.NEW.toString(), 1440L, LocalDateTime.of(2022, 10, 5, 12, 0));
        String json = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        epics = taskManager.getEpicsMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertEquals(2, epics.size(), "���� ��� ��������, � �� ��������");
        assertEquals("Changed Description Epic", epics.get(2).getDescription(), "������ ��������� �� ��� ��������");
    }

    @Test
    void shouldGetAllTasks () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        tasks = taskManager.getTasksMap();
        HashMap<Integer, Task> tasksReceived = gson.fromJson(response.body(), new TypeToken<HashMap<Integer, Task>>(){}.getType());

        assertEquals(200, response.statusCode(), "��� ������ �� ����� 200");
        assertNotEquals("", response.body(), "�������� ������ ������ � ��������");
        assertEquals(tasks.size(), tasksReceived.size(), "������������ �� ��� ������");
    }

    @Test
    void shouldGetAllSubtasks () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        subtasks = taskManager.getSubtasksMap();
        HashMap<Integer, Subtask> subtasksReceived = gson.fromJson(response.body(), new TypeToken<HashMap<Integer, Subtask>>(){}.getType());

        assertEquals(200, response.statusCode(), "��� ������ �� ����� 200");
        assertNotEquals("", response.body(), "�������� ������ ������ � �����������");
        assertEquals(subtasks.size(), subtasksReceived.size(), "������������ �� ��� ���������");
    }

    @Test
    void shouldGetAllEpics () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        epics = taskManager.getEpicsMap();
        HashMap<Integer, Epic> epicsReceived = gson.fromJson(response.body(), new TypeToken<HashMap<Integer, Epic>>(){}.getType());

        assertEquals(200, response.statusCode(), "��� ������ �� ����� 200");
        assertNotEquals("", response.body(), "�������� ������ ������ � �������");
        assertEquals(epics.size(), epicsReceived.size(), "������������ �� ��� �����");
    }

    @Test
    void shouldGetOneTask () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "task/" + "?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskReceived = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode(), "��� ������ �� ����� 200");
        assertNotEquals("", response.body(), "�������� ������ ������ � ��������");
        assertEquals(1, taskReceived.getTaskId(), "������������ �� ��� ������");
    }

    @Test
    void shouldGetOneSubTask () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "subtask/" + "?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskReceived = gson.fromJson(response.body(), Subtask.class);

        assertEquals(200, response.statusCode(), "��� ������ �� ����� 200");
        assertNotEquals("", response.body(), "�������� ������ ������ � ��������");
        assertEquals(3, subtaskReceived.getTaskId(), "������������ �� ��� ������");
    }

    @Test
    void shouldGetOneEpic () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "epic/" + "?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicReceived = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode(), "��� ������ �� ����� 200");
        assertNotEquals("", response.body(), "�������� ������ ������ � ��������");
        assertEquals(2, epicReceived.getTaskId(), "������������ �� ��� ������");
    }

    @Test
    void shouldGetAllSubTasksForOneEpic () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "/subtask/epic/" + "?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subtasksReceived = gson.fromJson(response.body(), new TypeToken<List<Subtask>>(){}.getType());

        assertEquals(200, response.statusCode(), "��� ������ �� ����� 200");
        assertNotEquals("", response.body(), "�������� ������ ������ � ��������");
        assertEquals(3, subtasksReceived.get(0).getTaskId(), "������������ �� ��� ������");
    }

    @Test
    void shouldDeleteAllTasks () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        tasks = taskManager.getTasksMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertTrue(tasks.isEmpty(), "������ �� ���� �������");
    }

    @Test
    void shouldDeleteAllSubtasks () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        subtasks = taskManager.getSubtasksMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertTrue(subtasks.isEmpty(), "��������� �� ���� �������");
    }

    @Test
    void shouldDeleteAllEpics () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        epics = taskManager.getEpicsMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertTrue(epics.isEmpty(), "����� �� ���� �������");
    }

    @Test
    void shouldDeleteOneTask () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "task/" + "?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        tasks = taskManager.getTasksMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertEquals(1, tasks.size(), "������ �� ���� �������");
    }

    @Test
    void shouldDeleteOneSubtask () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "subtask/" + "?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        subtasks = taskManager.getSubtasksMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertEquals(1, subtasks.size(), "��������� �� ���� �������");
    }

    @Test
    void shouldDeleteOneEpic () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "epic/" + "?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        epics = taskManager.getEpicsMap();

        assertEquals(201, response.statusCode(), "��� ������ �� ����� 201");
        assertEquals(1, epics.size(), "��������� �� ���� �������");
    }

    @Test
    void shouldGetHistory () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + "history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "��� ������ �� ����� 200");
        assertNotEquals("", response.body(), "������ ������ �� ������");
    }

    @Test
    void shouldGetPrioritizedTasks () throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "��� ������ �� ����� 200");
        assertNotEquals("", response.body(), "������ ������ �� ������");
    }
}
