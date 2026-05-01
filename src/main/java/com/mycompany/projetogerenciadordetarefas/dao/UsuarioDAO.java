package com.mycompany.projetogerenciadordetarefas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mycompany.projetogerenciadordetarefas.models.Usuario; 
import com.mycompany.projetogerenciadordetarefas.util.Conexao;
import com.mycompany.projetogerenciadordetarefas.util.SenhaUtil;
import java.sql.SQLException;

/**
 * Classe responsável por realizar operações de acesso ao banco
 * relacionadas aos usuários, como cadastro, login e busca por ID.
 * 
 * Versão: 1.0
 * Autor: Luiz Fernando
 * Data: 28/04/2026
 */
public class UsuarioDAO {
    
    /**
     * Cadastra um novo usuário no banco de dados.
     * A senha é criptografada antes de ser armazenada.
     * 
     * @param user Objeto contendo os dados do usuário
     * @return true se o cadastro for realizado com sucesso, false caso contrário
     */
    public boolean cadastrar(Usuario user){
        String sql = "INSERT INTO usuarios(nome,email,senha) VALUES(?,?,?)";
        
        
            // CORREÇÃO AQUI: Passar o getEmail() em vez de getSenha()
            boolean emailRes = emailExiste(user.getEmail().trim()); 
    
             if (emailRes) {
                throw new IllegalArgumentException("Email ja cadastrado. Utilize outro");
             }
            if(user.getSenha().length()< 4){
                 throw new IllegalArgumentException("A senha deve ter pelo menos 4 caracteres.");
            }
            
        try{
            // Abre conexão com o banco
            Connection conn = Conexao.conectar();
            PreparedStatement ps = conn.prepareStatement(sql);
             
            // Define os valores do usuário
            ps.setString(1, user.getNome().trim());             
            ps.setString(2, user.getEmail().trim());
            
            //Verificando email existe 
         
            
            // Criptografa a senha antes de salvar
            ps.setString(3, SenhaUtil.criptografarSenha(user.getSenha().trim()));
             
            // Executa o cadastro
            ps.executeUpdate();
             
            System.out.println("Usuario cadastrado");
             
            // Fecha conexão
            ps.close();
            conn.close();
            return true; 

        }catch(SQLException e){
            System.out.println(e);
            return false;
        }
    }
    /**
     * Realiza o login do usuário verificando email e senha.
     * 
     * @param email Email informado no login
     * @param senha Senha informada no login
     * @return Objeto Usuario se o login for válido, ou null caso contrário
     */
    public Usuario login(String email, String senha) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try {
            // Abre conexão com o banco
            Connection conn = Conexao.conectar();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, email.trim());

            ResultSet rs = ps.executeQuery();

            // Verifica se o usuário foi encontrado
            if (rs.next()) {
                String senhaHash = rs.getString("senha");

                // Confere se a senha digitada corresponde à senha criptografada
                if (SenhaUtil.verificarSenha(senha, senhaHash)) {
                    Usuario user = new Usuario();

                    // Preenche os dados do usuário
                    user.setId(rs.getInt("id_usuarios"));
                    user.setNome(rs.getString("nome"));
                    user.setEmail(rs.getString("email"));
                    
                    user.setSenha(rs.getString("senha"));
                    

                    rs.close();
                    ps.close();
                    conn.close();

                    return user;
                }
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }

    /**
     * Busca um usuário no banco pelo ID.
     * 
     * @param id ID do usuário
     * @return Objeto Usuario encontrado ou null se não existir
     */
    public Usuario buscarPorId(int id) {

        String sql = "SELECT * FROM usuarios WHERE id_usuarios = ?";

        try {
            // Abre conexão com o banco
            Connection conn = Conexao.conectar();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            // Se encontrar o usuário, preenche o objeto
            if(rs.next()){
                Usuario user = new Usuario();

                user.setId(rs.getInt("id_usuarios"));
                user.setNome(rs.getString("nome"));
                user.setEmail(rs.getString("email"));
                user.setSenha(rs.getString("senha"));

                rs.close();
                ps.close();
                conn.close();

                return user;
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }
    
    /**
    * Verifica se o email informado no cadastro já está cadastrado no banco de dados.
    * 
    * @param email Email do usuário
    * @return true se o email já existir, false caso contrário
    */
    protected boolean emailExiste(String email){
        String sql = "select 1 from usuarios where email = ?";
        
        try{
            Connection conn = Conexao.conectar();
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, email.trim());
            
            ResultSet rs = ps.executeQuery();
            
            boolean existe  = rs.next();
            
            rs.close();
            ps.close();
            conn.close();
            
            return existe;
           
        }catch(SQLException e){
            System.out.println(e);
            return false; 
        }
       
    }
    
    
    
    /**
     * Atualiza os dados do usuário no banco de dados.
    * Antes da atualização, verifica se o novo email informado já está sendo usado
    * por outro usuário.
    * 
    * @param user Objeto com os novos dados do usuário
    *  @return true se a atualização for realizada com sucesso, false caso contrário
    */
    public boolean atualizar(Usuario user) {
     String sql = "UPDATE usuarios SET nome = ?, email = ? WHERE id_usuarios = ?";
    
        // Busca os dados atuais do usuário no banco para comparar o email atual com o novo
        Usuario usuarioAtual = buscarPorId(user.getId());

        // Verifica se o email informado foi alterado
        if (!usuarioAtual.getEmail().equalsIgnoreCase(user.getEmail().trim())) {
        
        // Caso o email tenha sido alterado, verifica se o novo email já está cadastrado
        if (emailExiste(user.getEmail().trim())) {
            throw new IllegalArgumentException("Este novo email já está sendo usado por outro usuário.");
        }
        }

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        
            // Define os novos valores no comando SQL
        ps.setString(1, user.getNome().trim());
        ps.setString(2, user.getEmail().trim());
        ps.setInt(3, user.getId());

            // Executa a atualização e retorna true se alguma linha foi alterada
            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar: " + e.getMessage());
            return false;
        }
    }
    
    
    
    //adicionar deletar
    //adicionar mudar senha
}//fim 
