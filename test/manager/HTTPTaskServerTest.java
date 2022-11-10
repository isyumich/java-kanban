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

    Task firstTask = new Task(1, "T1 ", "Desc T1", TasksStatus.NEW.toString(), 1440L, Managers.createDate(1, 10));
    Epic firstEpic = new Epic(1, "E1 ", "Desc E1", TasksStatus.NEW.toString(), 1440L, Managers.createDate(5, 10));
    Subtask firstSubtaskFirstEpic = new Subtask(1, "S1 E1", "Desc S1 E1", TasksStatus.NEW.toString(), 1440L, Managers.createDate(3, 10), 2);
    Task secondTask = new Task(1, "T2", "Desc T2", TasksStatus.NEW.toString(), 1440L, Managers.createDate(7, 10));
    Epic secondEpic = new Epic(1, "E2", "Desc E2", TasksStatus.NEW.toString(), 1440L, Managers.createDate(11, 10));
    Subtask firstSubtaskSecondEpic = new Subtask(1, "S1 E2", "Desc S1 E2", TasksStatus.NEW.toString(), 1440L, Managers.createDate(9, 10), 5);

    @BeforeEach
    void beforeEach() throws IOException {
        taskManager = new FileBackedTaskManager("src\\Manager\\TaskManager.csv");
        httpTaskServer = new HTTPTaskServer(taskManager);


        taskManager.add(firstTask);
        taskManager.add(firstEpic);
        taskManager.add(firstSubtaskFirstEpic);

        taskManager.add(secondTask);
        taskManager.add(secondEpic);
        taskManager.add(firstSubtaskSecondEpic);

        httpTaskServer.startServer();
    }
    @AfterEach
    void afterEach() {
        httpTaskServer.stopServer();
    }

    protected HttpResponse<String> createPostResponse(Task task, String addUrl) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + addUrl);
        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
    protected HttpResponse<String> createGetResponse(String addUrl) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + addUrl);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
    protected HttpResponse<String> createDeleteResponse(String addUrl) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI url = URI.create(HOST + addUrl);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void shouldAddTask() throws IOException, InterruptedException {
        Task newTask = new Task(7, "Task", "Description Task", TasksStatus.NEW.toString(), 1440L, Managers.createDate(15, 10));
        HttpResponse<String> response = createPostResponse(newTask, "task/");
        tasks = taskManager.getTasksMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(3, tasks.size(), "Задача не была добавлена");
    }
    @Test
    void shouldAddSubtask() throws IOException, InterruptedException {
        Subtask newSubtask = new Subtask(7, "Subtask for Epic ", "Description Subtask for Epic", TasksStatus.NEW.toString(), 1440L, Managers.createDate(15, 10), 2);
        HttpResponse<String> response = createPostResponse(newSubtask, "subtask/");
        subtasks = taskManager.getSubtasksMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(3, subtasks.size(), "Подзадача не была добавлена");
    }
    @Test
    void shouldAddEpic() throws IOException, InterruptedException {
        Epic newEpic = new Epic(7, "Epic", "Description Epic", TasksStatus.NEW.toString(), 1440L, Managers.createDate(15, 10));
        HttpResponse<String> response = createPostResponse(newEpic, "epic/");
        epics = taskManager.getEpicsMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(3, epics.size(), "Эпик не был добавлен");
    }

    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {
        Task newTask = new Task(1, "Task", "Changed Description Task", TasksStatus.NEW.toString(), 1440L, Managers.createDate(15, 10));
        HttpResponse<String> response = createPostResponse(newTask, "task/");
        tasks = taskManager.getTasksMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(2, tasks.size(), "Задача была добавлена, а не обновлена");
        assertEquals("Changed Description Task", tasks.get(1).getDescription(), "Статус задачи не был обновлен");
    }
    @Test
    void shouldUpdateSubtask() throws IOException, InterruptedException {
        Subtask newSubtask = new Subtask(3, "Subtask for Epic ", "Changed Description Subtask for Epic", TasksStatus.NEW.toString(), 1440L, Managers.createDate(15, 10), 2);
        HttpResponse<String> response = createPostResponse(newSubtask, "subtask/");
        subtasks = taskManager.getSubtasksMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(2, subtasks.size(), "Подзадача была добавлена, а не обновлена");
        assertEquals("Changed Description Subtask for Epic", subtasks.get(3).getDescription(), "Статус подзадачи не был обновлен");
    }
    @Test
    void shouldUpdateEpic() throws IOException, InterruptedException {
        Epic newEpic = new Epic(2, "Epic", "Changed Description Epic", TasksStatus.NEW.toString(), 1440L, Managers.createDate(15, 10));
        HttpResponse<String> response = createPostResponse(newEpic, "epic/");
        epics = taskManager.getEpicsMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(2, epics.size(), "Эпик был добавлен, а не обновлен");
        assertEquals("Changed Description Epic", epics.get(2).getDescription(), "Статус подзадачи не был обновлен");
    }

    @Test
    void shouldGetAllTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = createGetResponse("task/");
        tasks = taskManager.getTasksMap();
        HashMap<Integer, Task> tasksReceived = gson.fromJson(response.body(), new TypeToken<HashMap<Integer, Task>>() {
        }.getType());

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertNotEquals("", response.body(), "Вернулся пустой список с задачами");
        assertEquals(tasks.size(), tasksReceived.size(), "Подгрузились не все задачи");
    }
    @Test
    void shouldGetAllSubtasks() throws IOException, InterruptedException {
        HttpResponse<String> response = createGetResponse("subtask/");
        subtasks = taskManager.getSubtasksMap();
        HashMap<Integer, Subtask> subtasksReceived = gson.fromJson(response.body(), new TypeToken<HashMap<Integer, Subtask>>() {
        }.getType());

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertNotEquals("", response.body(), "Вернулся пустой список с подзадачами");
        assertEquals(subtasks.size(), subtasksReceived.size(), "Подгрузились не все подзадачи");
    }
    @Test
    void shouldGetAllEpics() throws IOException, InterruptedException {
        HttpResponse<String> response = createGetResponse("epic/");
        epics = taskManager.getEpicsMap();
        HashMap<Integer, Epic> epicsReceived = gson.fromJson(response.body(), new TypeToken<HashMap<Integer, Epic>>() {
        }.getType());

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertNotEquals("", response.body(), "Вернулся пустой список с эпиками");
        assertEquals(epics.size(), epicsReceived.size(), "Подгрузились не все эпики");
    }
    @Test
    void shouldGetOneTask() throws IOException, InterruptedException {
        HttpResponse<String> response = createGetResponse("task/" + "?id=1");
        Task taskReceived = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertNotEquals("", response.body(), "Вернулся пустой список с задачами");
        assertEquals(1, taskReceived.getTaskId(), "Подгрузились не все задачи");
    }
    @Test
    void shouldGetOneSubTask() throws IOException, InterruptedException {
        HttpResponse<String> response = createGetResponse("subtask/" + "?id=3");
        Subtask subtaskReceived = gson.fromJson(response.body(), Subtask.class);

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertNotEquals("", response.body(), "Вернулся пустой список с задачами");
        assertEquals(3, subtaskReceived.getTaskId(), "Подгрузились не все задачи");
    }
    @Test
    void shouldGetOneEpic() throws IOException, InterruptedException {
        HttpResponse<String> response = createGetResponse("epic/" + "?id=2");
        Epic epicReceived = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertNotEquals("", response.body(), "Вернулся пустой список с задачами");
        assertEquals(2, epicReceived.getTaskId(), "Подгрузились не все задачи");
    }
    @Test
    void shouldGetAllSubTasksForOneEpic() throws IOException, InterruptedException {
        HttpResponse<String> response = createGetResponse("/subtask/epic/" + "?id=2");
        List<Subtask> subtasksReceived = gson.fromJson(response.body(), new TypeToken<List<Subtask>>() {
        }.getType());

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertNotEquals("", response.body(), "Вернулся пустой список с задачами");
        assertEquals(3, subtasksReceived.get(0).getTaskId(), "Подгрузились не все задачи");
    }

    @Test
    void shouldDeleteAllTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = createDeleteResponse("task/");
        tasks = taskManager.getTasksMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertTrue(tasks.isEmpty(), "Задачи не были удалены");
    }

    @Test
    void shouldDeleteAllSubtasks() throws IOException, InterruptedException {
        HttpResponse<String> response = createDeleteResponse("subtask/");
        subtasks = taskManager.getSubtasksMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertTrue(subtasks.isEmpty(), "Подзадачи не были удалены");
    }

    @Test
    void shouldDeleteAllEpics() throws IOException, InterruptedException {
        HttpResponse<String> response = createDeleteResponse("epic/");
        epics = taskManager.getEpicsMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertTrue(epics.isEmpty(), "Эпики не были удалены");
    }

    @Test
    void shouldDeleteOneTask() throws IOException, InterruptedException {
        HttpResponse<String> response = createDeleteResponse("task/" + "?id=1");
        tasks = taskManager.getTasksMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(1, tasks.size(), "Задача не была удалена");
    }

    @Test
    void shouldDeleteOneSubtask() throws IOException, InterruptedException {
        HttpResponse<String> response = createDeleteResponse("subtask/" + "?id=3");
        subtasks = taskManager.getSubtasksMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(1, subtasks.size(), "Подзадача не была удалена");
    }

    @Test
    void shouldDeleteOneEpic() throws IOException, InterruptedException {
        HttpResponse<String> response = createDeleteResponse("epic/" + "?id=2");
        epics = taskManager.getEpicsMap();

        assertEquals(201, response.statusCode(), "Код ответа не равен 201");
        assertEquals(1, epics.size(), "Подзадача не была удалена");
    }

    @Test
    void shouldGetHistory() throws IOException, InterruptedException {
        HttpResponse<String> response = createGetResponse("history/");

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertNotEquals("", response.body(), "Запрос ничего не вернул");
    }

    @Test
    void shouldGetPrioritizedTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = createGetResponse("");

        assertEquals(200, response.statusCode(), "Код ответа не равен 200");
        assertNotEquals("", response.body(), "Запрос ничего не вернул");
    }
}
