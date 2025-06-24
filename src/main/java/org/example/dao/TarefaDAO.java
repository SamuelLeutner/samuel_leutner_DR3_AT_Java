package org.example.dao;

import org.example.model.Tarefa;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TarefaDAO {
    private final ConcurrentHashMap<Long, Tarefa> tarefas = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public TarefaDAO() {
        save(new Tarefa(0, "Estudar para o DR4", "Revisar todo o conteúdo de Java e Spring Boot."));
        save(new Tarefa(0, "Fazer compras", "Comprar pão, leite e ovos."));
    }

    public Tarefa save(Tarefa tarefa) {
        long id = sequence.incrementAndGet();
        tarefa.setId(id);
        if (tarefa.getDataCriacao() == null) {
            tarefa.setDataCriacao(LocalDateTime.now());
        }
        tarefas.put(id, tarefa);
        return tarefa;
    }

    public Collection<Tarefa> findAll() {
        return tarefas.values();
    }

    public Tarefa findById(long id) {
        return tarefas.get(id);
    }
}