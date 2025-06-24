package org.example.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:7000";

    public static void main(String[] args) throws Exception {
        System.out.println("--- 1. Criando nova tarefa (POST /tarefas) ---");
        postTarefa();

        System.out.println("\n--- 2. Listando todas as tarefas (GET /tarefas) ---");
        getTarefas();

        System.out.println("\n--- 3. Buscando tarefa pelo ID=1 (GET /tarefas/1) ---");
        getTarefaPorId("1");

        System.out.println("\n--- 4. Buscando status da API (GET /status) ---");
        getStatus();
    }

    public static void postTarefa() throws Exception {
        URL url = new URL(BASE_URL + "/tarefas");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        String jsonInputString = "{\"titulo\": \"Aprender Javalin\", \"descricao\": \"Criar uma API REST funcional.\"}";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        System.out.println("Status da Resposta: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            System.out.println("Resposta: " + readResponse(con));
        }
        con.disconnect();
    }

    public static void getTarefas() throws Exception {
        URL url = new URL(BASE_URL + "/tarefas");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        System.out.println("Status da Resposta: " + con.getResponseCode());
        System.out.println("Resposta: " + readResponse(con));
        con.disconnect();
    }

    public static void getTarefaPorId(String id) throws Exception {
        URL url = new URL(BASE_URL + "/tarefas/" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        System.out.println("Status da Resposta: " + con.getResponseCode());
        System.out.println("Resposta: " + readResponse(con));
        con.disconnect();
    }

    public static void getStatus() throws Exception {
        URL url = new URL(BASE_URL + "/status");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        System.out.println("Status da Resposta: " + con.getResponseCode());
        System.out.println("Resposta: " + readResponse(con));
        con.disconnect();
    }

    private static String readResponse(HttpURLConnection con) throws Exception {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        }
    }
}