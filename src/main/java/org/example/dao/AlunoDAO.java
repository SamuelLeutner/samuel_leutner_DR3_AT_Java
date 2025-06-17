package org.example.dao;

import org.example.model.Aluno;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AlunoDAO {
    private final ConcurrentHashMap<Long, Aluno> alunos = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public AlunoDAO() {
        save(new Aluno(0, "Ada Lovelace", "ada@example.com"));
        save(new Aluno(0, "Grace Hopper", "grace@example.com"));
    }

    public Aluno save(Aluno aluno) {
        long id = sequence.incrementAndGet();
        aluno.setId(id);
        alunos.put(id, aluno);
        return aluno;
    }

    public Collection<Aluno> findAll() {
        return alunos.values();
    }

    public Aluno findById(long id) {
        return alunos.get(id);
    }
}