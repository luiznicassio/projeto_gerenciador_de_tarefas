package com.mycompany.projetogerenciadordetarefas.models;

import java.time.LocalDate;

/**
 * Classe que representa uma tarefa do sistema.
 * Contém dados da tarefa, relacionamentos (IDs)
 * e também dados auxiliares para exibição (JOIN).
 * 
 * @author nicas
 */
public class Tarefa {

    // 🔹 Dados principais
    private int id;
    private String titulo;
    private String descricao;
    private LocalDate dataVencimento;

    // 🔹 Relacionamentos (banco)
    private int usuarioId;    
    private int categoriaId;
    private int statusId;
    private int prioridadeId;

    // 🔹 Dados para EXIBIÇÃO (JOIN)
    private String categoriaNome;
    private String categoriaCor;

    private String statusNome;

    private String prioridadeNome;
    private String prioridadeCor;

    // 🔹 Construtor vazio
    public Tarefa(){}

    // 🔹 Construtor para cadastro (sem id)
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

    // 🔹 Construtor completo
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

    // =========================
    // GETTERS E SETTERS
    // =========================

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
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode estar vazio.");
        }
        this.titulo = titulo.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao != null ? descricao.trim() : null;
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

    // =========================
    // GETTERS/SETTERS EXIBIÇÃO
    // =========================

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public String getCategoriaCor() {
        return categoriaCor;
    }

    public void setCategoriaCor(String categoriaCor) {
        this.categoriaCor = categoriaCor;
    }

    public String getStatusNome() {
        return statusNome;
    }

    public void setStatusNome(String statusNome) {
        this.statusNome = statusNome;
    }

    public String getPrioridadeNome() {
        return prioridadeNome;
    }

    public void setPrioridadeNome(String prioridadeNome) {
        this.prioridadeNome = prioridadeNome;
    }
    public String getPrioridadeCor() {
        return prioridadeCor;
    }

    public void setPrioridadeCor(String prioridadeCor) {
        this.prioridadeCor = prioridadeCor;
    }
    
}