package com.mycompany.projetogerenciadordetarefas.models;

import com.mycompany.projetogerenciadordetarefas.util.SenhaUtil;

public class Usuario {
    private int id;
    private String nome; 
    private String email;
    private String senha; 
    
    public Usuario(){}

    public Usuario(int id, String nome, String email, String senha){
        this.id = id;
        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }
    
    public Usuario(String nome, String email, String senha){
        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }
    
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNome(){
        return nome; 
    }

    public void setNome(String nome){
        nome = nome.trim();

        if(nome.length() < 3){
            throw new IllegalArgumentException("O nome deve ter pelo menos 3 caracteres.");
        }

        this.nome = nome;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        email = email.trim();

        if(email.isEmpty()){
            throw new IllegalArgumentException("O email não pode estar vazio.");
        }

        this.email = email;
    }

    public String getSenha(){
        return senha;
    }

    public void setSenha(String senha){
       

        if(senha.length() < 4){
            throw new IllegalArgumentException("A senha deve ter pelo menos 4 caracteres.");
        }else{
            String senhaHash = SenhaUtil.criptografarSenha(senha);
            this.senha = senhaHash;
        }

        
    }
}