package clients;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url = "http://localhost:8080";
    protected String apiToken;

    public KVTaskClient() {
        register();
    }

    private void register() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Accept", "application/json").GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                apiToken = response.body();
            } else {
                throw new RuntimeException("�� ������� �������� API_TOKEN. ��� ������: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) { // ��������� ������ �������� �������
            System.out.println("�� ����� ���������� ������� ������� �� URL-������: '" + url + "' �������� ������.\n" +
                    "���������, ����������, ����� � ��������� �������.");
        }
    }

    public void put(String key, String json) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN" + json))
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("�� ����� ���������� ��������� ������");
            throw new RuntimeException();
        }
    }

    public String load(String key) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN" + apiToken))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            System.out.println("�� ������� ��������� ������");
            throw new RuntimeException();
        }
    }
}
