package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private static HttpTaskServer server;
    private static final Gson gson = new GsonBuilder().create();

    @BeforeAll
    static void startServer() throws IOException {
        server = new HttpTaskServer(new InMemoryTaskManager());
        server.start();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void shouldAddAndReturnTask() throws IOException {
        // Arrange: создаём задачу
        Task task = new Task("Test", "Description", TaskStatus.NEW);
        String json = gson.toJson(task);

        // Act: отправляем POST-запрос
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/tasks").openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        assertEquals(201, connection.getResponseCode());

        // Проверка: GET-запрос для получения всех задач
        HttpURLConnection getConn = (HttpURLConnection) new URL("http://localhost:8080/tasks").openConnection();
        getConn.setRequestMethod("GET");

        assertEquals(200, getConn.getResponseCode());

        String response = readResponse(getConn);
        assertTrue(response.contains("Test"));
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        try (InputStream is = connection.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }
}
