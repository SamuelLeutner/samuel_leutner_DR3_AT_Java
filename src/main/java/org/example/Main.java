package org.example;

import io.javalin.Javalin;
import org.example.controller.AlunoController;
import org.example.dao.AlunoDAO;

import java.time.Instant;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new io.javalin.json.JavalinJackson());
            config.showJavalinBanner = false;
        }).start(7000);

        AlunoDAO alunoDAO = new AlunoDAO();
        AlunoController alunoController = new AlunoController(alunoDAO);

        System.out.println("Servidor Javalin iniciado em http://localhost:7000");

        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));

        app.get("/status", ctx -> {
            Map<String, String> response = Map.of(
                    "status", "ok",
                    "timestamp", Instant.now().toString()
            );
            ctx.json(response);
        });

        app.post("/echo", ctx -> {
            Map<String, Object> jsonRequest = ctx.bodyAsClass(Map.class);
            ctx.json(jsonRequest);
        });

        app.get("/saudacao/{nome}", ctx -> {
            String nome = ctx.pathParam("nome");
            Map<String, String> response = Map.of("mensagem", "Olá, " + nome + "!");
            ctx.json(response);
        });

        app.post("/alunos", alunoController::criar);

        app.get("/alunos", alunoController::buscarTodos);

        app.get("/alunos/{id}", alunoController::buscarPorId);
    }
}