package com.mycompany.projetogerenciadordetarefas.models;

/**
 * Classe modelo que representa um usuário do sistema.
 * Armazena os dados de identificação e acesso,
 * além de validar nome, email e senha.
 * 
 * Versão: 1.0
 * Autor: Luiz Fernando
 * Data: 28/04/2026
 */
public class Usuario {
    
    // Identificador único do usuário
    private int id;
    
    // Nome do usuário
    private String nome; 
    
    // Email do usuário
    private String email;
    
    // Senha do usuário
    private String senha; 
    
    /**
     * Construtor vazio.
     */
    public Usuario(){}

    /**
     * Construtor completo com ID.
     * 
     * @param id identificador do usuário
     * @param nome nome do usuário
     * @param email email do usuário
     * @param senha senha do usuário
     */
    public Usuario(int id, String nome, String email, String senha){
        this.id = id;
        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }
    
    /**
     * Construtor sem ID.
     * Utilizado geralmente no cadastro.
     * 
     * @param nome nome do usuário
     * @param email email do usuário
     * @param senha senha do usuário
     */
    public Usuario(String nome, String email, String senha){
        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }
    
    /**
     * Retorna o ID do usuário.
     * 
     * @return id do usuário
     */
    public int getId(){
        return id;
    }

    /**
     * Define o ID do usuário.
     * 
     * @param id identificador do usuário
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Retorna o nome do usuário.
     * 
     * @return nome do usuário
     */
    public String getNome(){
        return nome; 
    }

    /**
     * Define o nome do usuário.
     * Remove espaços e valida se possui pelo menos 3 caracteres.
     * 
     * @param nome nome do usuário
     */
    public void setNome(String nome){
        nome = nome.trim();

        if(nome.length() < 3){
            throw new IllegalArgumentException("O nome deve ter pelo menos 3 caracteres.");
        }

        this.nome = nome;
    }

    /**
     * Retorna o email do usuário.
     * 
     * @return email do usuário
     */
    public String getEmail(){
        return email;
    }

    /**
     * Define o email do usuário.
     * Remove espaços e verifica se não está vazio.
     * 
     * @param email email do usuário
     */
    public void setEmail(String email){
        email = email.trim();

        //Verifica se não esta vazio 
        if(email.isEmpty()){
            throw new IllegalArgumentException("O email não pode estar vazio.");
        }
        //Verifica se tem @
        if(!email.contains("@")){
            throw new IllegalArgumentException("Email inválido");
        }

        this.email = email;
    }

    /**
     * Retorna a senha do usuário.
     * 
     * @return senha do usuário
     */
    public String getSenha(){
        return senha;
    }

    /**
     * Define a senha do usuário.
     * Valida se possui pelo menos 4 caracteres.
     * 
     * @param senha senha do usuário
     */
    public void setSenha(String senha){
        this.senha = senha.trim();
    }
}