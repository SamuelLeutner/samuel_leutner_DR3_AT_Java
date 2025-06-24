package org.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Tarefa {
    private long id;
    private String titulo;
    private String descricao;
    private boolean concluida;
    private LocalDateTime dataCriacao;

    public Tarefa() {}

    public Tarefa(long id, String titulo, String descricao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluida = false;
        this.dataCriacao = LocalDateTime.now();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarefa tarefa = (Tarefa) o;
        return id == tarefa.id && concluida == tarefa.concluida && Objects.equals(titulo, tarefa.titulo) && Objects.equals(descricao, tarefa.descricao) && Objects.equals(dataCriacao, tarefa.dataCriacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descricao, concluida, dataCriacao);
    }
}