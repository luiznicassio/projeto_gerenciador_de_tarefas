package com.mycompany.projetogerenciadordetarefas.dao;

import com.mycompany.projetogerenciadordetarefas.models.Tarefa;
import com.mycompany.projetogerenciadordetarefas.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TarefaDAO {

    //Criar
    public boolean criar(Tarefa t) {
        String sql = "INSERT INTO tarefas " +
                     "(titulo, descricao, data_vencimento, usuarios_id, categorias_id, status_id, prioridades_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try{
            Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
                    
            stmt.setString(1, t.getTitulo());
            stmt.setString(2, t.getDescricao());
            stmt.setDate(3, java.sql.Date.valueOf(t.getDataVencimento()));
            stmt.setInt(4, t.getUsuarioId());
            stmt.setInt(5, t.getCategoriaId());
            stmt.setInt(6, t.getStatusId());
            stmt.setInt(7, t.getPrioridadeId());

            stmt.executeUpdate();
            
            stmt.close();
            conn.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao criar tarefa: " + e.getMessage());
            return false;
        }
    }
    
    //Listar
    
    
    
}