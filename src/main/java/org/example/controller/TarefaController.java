package org.example.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.example.dao.TarefaDAO;
import org.example.model.Tarefa;

public class TarefaController {

    private final TarefaDAO tarefaDAO;

    public TarefaController(TarefaDAO tarefaDAO) {
        this.tarefaDAO = tarefaDAO;
    }

    public void criar(Context ctx) {
        Tarefa tarefa = ctx.bodyAsClass(Tarefa.class);
        if (tarefa.getTitulo() == null || tarefa.getTitulo().isBlank()) {
            ctx.status(HttpStatus.BAD_REQUEST).result("O título da tarefa é obrigatório.");
            return;
        }
        tarefa.setConcluida(false);
        tarefaDAO.save(tarefa);
        ctx.status(HttpStatus.CREATED).json(tarefa);
    }

    public void buscarTodas(Context ctx) {
        ctx.json(tarefaDAO.findAll());
    }

    public void buscarPorId(Context ctx) {
        try {
            long id = Long.parseLong(ctx.pathParam("id"));
            Tarefa tarefa = tarefaDAO.findById(id);

            if (tarefa != null) {
                ctx.json(tarefa);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Tarefa não encontrada.");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).result("ID inválido.");
        }
    }
}