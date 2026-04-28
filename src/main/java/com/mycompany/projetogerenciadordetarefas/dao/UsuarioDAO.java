package com.mycompany.projetogerenciadordetarefas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mycompany.projetogerenciadordetarefas.models.Usuario; 
import com.mycompany.projetogerenciadordetarefas.util.Conexao;
import java.sql.SQLException;

/**
 *
 * @author nicas
 */
public class UsuarioDAO {
    
    //cadastrar 
    public boolean cadastrar(Usuario user){
        String sql = "insert into usuarios(nome,email,senha) values(?,?,?)";
        
        try{
            Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             
               ps.setString(1, user.getNome().trim());             
               ps.setString(2, user.getEmail().trim());
               ps.setString(3, user.getSenha().trim());
             
             ps.executeUpdate();
             
             System.out.println("Usuario cadastrado");
             
             ps.close();
             conn.close();
             return true; 

        }catch(SQLException  e){
            System.out.println(e);
            return false;
        }
        
    }
    //fim cadastrar
    
    
}
