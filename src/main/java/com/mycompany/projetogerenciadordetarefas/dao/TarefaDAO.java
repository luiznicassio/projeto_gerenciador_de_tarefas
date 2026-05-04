package com.mycompany.projetogerenciadordetarefas.dao;

import com.mycompany.projetogerenciadordetarefas.models.Tarefa;
import com.mycompany.projetogerenciadordetarefas.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    // =========================
    // CRIAR
    // =========================
    public boolean criar(Tarefa t) {

        String sql = "INSERT INTO tarefas " +
                "(titulo, descricao, data_vencimento, usuarios_id, categorias_id, status_id, prioridades_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getTitulo());
            stmt.setString(2, t.getDescricao());
            stmt.setDate(3, Date.valueOf(t.getDataVencimento()));
            stmt.setInt(4, t.getUsuarioId());
            stmt.setInt(5, t.getCategoriaId());
            stmt.setInt(6, t.getStatusId());
            stmt.setInt(7, t.getPrioridadeId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao criar tarefa: " + e.getMessage());
            return false;
        }
    }

    // =========================
    // LISTAR POR USUÁRIO (COM JOIN)
    // =========================
    public List<Tarefa> listarPorUsuario(int usuarioId) {

        List<Tarefa> lista = new ArrayList<>();

        String sql = """
            SELECT 
                t.*,
                c.nome AS categoria,
                c.cor AS categoria_cor,
                s.nome AS status,
                p.nome AS prioridade,
                p.cor AS prioridade_cor
            FROM tarefas t
            LEFT JOIN categorias c ON t.categorias_id = c.id_categorias
            LEFT JOIN status s ON t.status_id = s.id_status
            LEFT JOIN prioridades p ON t.prioridades_id = p.id_prioridades
            WHERE t.usuarios_id = ?
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Tarefa t = new Tarefa();

                // 🔹 Dados principais
                t.setId(rs.getInt("id_tarefas"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescricao(rs.getString("descricao"));

                Date data = rs.getDate("data_vencimento");
                if (data != null) {
                    t.setDataVencimento(data.toLocalDate());
                }

                t.setUsuarioId(rs.getInt("usuarios_id"));

                // 🔹 IDs
                t.setCategoriaId(rs.getInt("categorias_id"));
                t.setStatusId(rs.getInt("status_id"));
                t.setPrioridadeId(rs.getInt("prioridades_id"));

                // 🔹 EXIBIÇÃO (JOIN)
                t.setCategoriaNome(rs.getString("categoria"));
                t.setCategoriaCor(rs.getString("categoria_cor"));

                t.setStatusNome(rs.getString("status"));

                t.setPrioridadeNome(rs.getString("prioridade"));
                
                // se você adicionou no model:
                try {
                    t.setPrioridadeCor(rs.getString("prioridade_cor"));
                } catch (Exception e) {
                    // ignora se ainda não existir no model
                }

                lista.add(t);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar tarefas: " + e.getMessage());
        }

        return lista;
    }
    
    public boolean atualizarStatus(int tarefaId, int novoStatus) {

    String sql = "UPDATE tarefas SET status_id = ? WHERE id_tarefas = ?";

    try (Connection conn = Conexao.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, novoStatus);
        ps.setInt(2, tarefaId);

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("Erro ao atualizar status: " + e.getMessage());
        return false;
    }
    }
}