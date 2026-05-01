package com.mycompany.projetogerenciadordetarefas.models;

import java.time.LocalDate;

/**
 *
 * @author nicas
 */
public class Tarefa {
    private int id;
    private String titulo;
    private String descricao;
    private LocalDate dataVencimento;
    private int usuarioId;    
    private int categoriaId;
    private int statusId;
    private int prioridadeId;
    
    // Construtor vazio
    public Tarefa(){}

    // Construtor para cadastro (sem id)
    public Tarefa(String titulo, String descricao, LocalDate dataVencimento,
        int usuarioId, int categoriaId, int statusId, int prioridadeId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataVencimento = dataVencimento;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.statusId = statusId;
        this.prioridadeId = prioridadeId;
    }
    // Construtor completo
    public Tarefa(int id, String titulo, String descricao, LocalDate dataVencimento,
                  int usuarioId, int categoriaId, int statusId, int prioridadeId) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataVencimento = dataVencimento;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.statusId = statusId;
        this.prioridadeId = prioridadeId;
    }

    // GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getPrioridadeId() {
        return prioridadeId;
    }

    public void setPrioridadeId(int prioridadeId) {
        this.prioridadeId = prioridadeId;
    }

}