package com.mycompany.projetogerenciadordetarefas.models;

public class Projeto {

    private int id;
    private String titulo;
    private String descricao;
    private String cor;
    private int id_criador;
    private String cargo;

    // cadastro
    public Projeto() {}

    // renderização
    public Projeto(int id, String titulo, String descricao, String cor, int id_criador,String cargo) {

        setId(id);
        setTitulo(titulo);
        setDescricao(descricao);
        setCor(cor);
        setCriador(id_criador);
        setCargo(cargo);
    }

    // set e get
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitulo(String titulo) {

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Titulo precisa ser preenchido.");
        }

        if (titulo.length() < 4) {
            throw new IllegalArgumentException("O titulo deve ter mais de 4 caracteres.");
        }

        this.titulo = titulo.trim();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setDescricao(String descricao) {

        this.descricao = descricao == null
                ? null
                : descricao.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setCor(String cor) {

        if (cor == null || cor.trim().isEmpty()) {
            throw new IllegalArgumentException("Selecione uma cor.");
        }

        this.cor = cor.trim();
    }

    public String getCor() {
        return cor;
    }

    public void setCriador(int id_criador) {

        if (id_criador <= 0) {
            throw new IllegalArgumentException("Id do criador não informado");
        }

        this.id_criador = id_criador;
    }

    public int getCriador() {
        return id_criador;
    }
    
    public  void setCargo(String cargo){
        if(cargo == null || cargo.trim().isEmpty()){
            throw new IllegalArgumentException("O cargo deve ser informado");
        }
        this.cargo = cargo.trim();
    }
    
    public String getCargo(){
        return cargo;
    }
}