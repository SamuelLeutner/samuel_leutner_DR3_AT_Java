package org.example.controller;

import org.example.dao.AlunoDAO;
import org.example.model.Aluno;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class AlunoController {

    private final AlunoDAO alunoDAO;

    public AlunoController(AlunoDAO alunoDAO) {
        this.alunoDAO = alunoDAO;
    }

    public void criar(Context ctx) {
        Aluno aluno = ctx.bodyAsClass(Aluno.class);
        if (aluno.getNome() == null || aluno.getEmail() == null) {
            ctx.status(HttpStatus.BAD_REQUEST).result("Nome e email são obrigatórios.");
            return;
        }
        alunoDAO.save(aluno);
        ctx.status(HttpStatus.CREATED).json(aluno);
    }

    public void buscarTodos(Context ctx) {
        ctx.json(alunoDAO.findAll());
    }

    public void buscarPorId(Context ctx) {
        try {
            long id = Long.parseLong(ctx.pathParam("id"));
            Aluno aluno = alunoDAO.findById(id);

            if (aluno != null) {
                ctx.json(aluno);
            } else {
                ctx.status(HttpStatus.NOT_FOUND).result("Aluno não encontrado.");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST).result("ID inválido.");
        }
    }
}