package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import org.example.controller.TarefaController;
import org.example.dao.TarefaDAO;
import java.time.Instant;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
        }).start(7000);

        TarefaDAO tarefaDAO = new TarefaDAO();
        TarefaController tarefaController = new TarefaController(tarefaDAO);

        System.out.println("Servidor Javalin iniciado em http://localhost:7000");

        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));

        app.get("/status", ctx -> {
            Map<String, String> response = Map.of(
                    "status", "ok",
                    "timestamp", Instant.now().toString()
            );
            ctx.json(response);
        });

        app.get("/saudacao/{nome}", ctx -> {
            String nome = ctx.pathParam("nome");
            Map<String, String> response = Map.of("mensagem", "Olá, " + nome + "!");
            ctx.json(response);
        });

        app.post("/tarefas", tarefaController::criar);
        app.get("/tarefas", tarefaController::buscarTodas);
        app.get("/tarefas/{id}", tarefaController::buscarPorId);
    }
}