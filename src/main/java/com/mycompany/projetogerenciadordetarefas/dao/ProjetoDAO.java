package com.mycompany.projetogerenciadordetarefas.dao;

import com.mycompany.projetogerenciadordetarefas.models.Projeto;
import com.mycompany.projetogerenciadordetarefas.util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO dos Projetos 
 * Funcionalidades: cadastrar , listar , editar , apagar 
 * 
 * Versão: 1.0
 * Autor: Luiz Fernando
 * Data: 11/05/2026
 */
public class ProjetoDAO {
   
    
   // criar
   // Método responsável por cadastrar um novo projeto
    public boolean cadastrar(Projeto p) {

        // SQL para inserir o projeto na tabela projetos
        String sqlProjeto = """
            INSERT INTO projetos
            (titulo, descricao, cor, criador_id)
            VALUES (?, ?, ?, ?)
        """;

        // SQL para adicionar o criador do projeto
        // na tabela projeto_usuarios
        String sqlMembro = """
            INSERT INTO projeto_usuarios
            (usuario_id, projeto_id, cargo)
            VALUES (?, ?, ?)
        """;

        // abre conexão com banco
        try (Connection conn = Conexao.conectar()) {

            // desativa auto commit
            // agora só salva no banco quando chamar commit()
            conn.setAutoCommit(false);

            try (

                // cria PreparedStatement
                // RETURN_GENERATED_KEYS permite pegar
                // o ID do projeto criado automaticamente
                PreparedStatement psProjeto =
                    conn.prepareStatement(
                        sqlProjeto,
                        Statement.RETURN_GENERATED_KEYS
                    );

            ) {

                // seta os valores do projeto
                psProjeto.setString(1, p.getTitulo());
                psProjeto.setString(2, p.getDescricao());
                psProjeto.setString(3, p.getCor());
                psProjeto.setInt(4, p.getCriador());

                // executa INSERT
                psProjeto.executeUpdate();

                // pega o ID gerado automaticamente
                ResultSet rs = psProjeto.getGeneratedKeys();

                // verifica se conseguiu pegar o ID
                if (!rs.next()) {

                    // lança erro caso não consiga
                    throw new SQLException(
                        "Erro ao obter ID do projeto."
                    );
                }

                // salva ID do projeto criado
                int idProjeto = rs.getInt(1);

                // adiciona o criador do projeto
                // na tabela projeto_usuarios
                try (
                    PreparedStatement psMembro =
                        conn.prepareStatement(sqlMembro);
                ) {

                    // usuário que criou o projeto
                    psMembro.setInt(1, p.getCriador());

                    // ID do projeto recém criado
                    psMembro.setInt(2, idProjeto);

                    // criador recebe cargo ADMIN
                    psMembro.setString(3, "ADMIN");

                    // executa INSERT
                    psMembro.executeUpdate();
                }

                // confirma tudo no banco
                conn.commit();

                System.out.println("Projeto criado.");

                return true;

            } catch (SQLException e) {

                // se der erro:
                // desfaz todas operações
                conn.rollback();

                System.out.println("Erro: " + e);

                return false;
            }

        } catch (SQLException e) {

            // erro de conexão
            System.out.println("Erro conexão: " + e);

            return false;
        }
    }  
   
    
    public List<Projeto> listarProjetos(int usuarioId){

        List<Projeto> lista = new ArrayList<>();

        String sql = """
            SELECT p.*, pu.cargo
            FROM projetos p
            INNER JOIN projeto_usuarios pu
            ON p.id_projeto = pu.projeto_id
            WHERE pu.usuario_id = ?
        """;

        try(

            Connection conn = Conexao.conectar();

            PreparedStatement ps =
                conn.prepareStatement(sql);

        ){

            // seta id usuário
            ps.setInt(1, usuarioId);

            // executa SELECT
            ResultSet rs = ps.executeQuery();

            // percorre resultados
            while(rs.next()){

                Projeto p = new Projeto(

                    rs.getInt("id_projeto"),

                    rs.getString("titulo"),

                    rs.getString("descricao"),

                    rs.getString("cor"),

                    rs.getInt("criador_id"),

                    rs.getString("cargo")
                );

                lista.add(p);
            }

        }catch(SQLException e){

            System.out.println("Erro: " + e);
        }

        return lista;
    } 
    
    
    //editar 
    public boolean atualizar(Projeto p){
         if("ADMIN".equals(p.getCargo())){
            String sql = "update projetos set titulo = ?, descricao = ?, cor = ? where  id_projeto = ?";
            
            try(Connection conn = Conexao.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)){
                
                ps.setString(1, p.getTitulo());
                ps.setString(2, p.getDescricao());
                ps.setString(3, p.getCor());
                ps.setInt(4, p.getId());
                
                
                int linhas = ps.executeUpdate();

                return linhas > 0;
            }catch(SQLException e){
                   throw new RuntimeException("Erro ao atualizar projeto: " + e.getMessage());
            }
 
        }
        
        throw new IllegalArgumentException("Usuario não tem permição.");
    }
    
  
    //deletar 
    public boolean apagar(Projeto p){
        
      if("ADMIN".equals(p.getCargo())){
            String sql = "delete from projetos where id_projeto = ?";
        
            try(Connection conn = Conexao.conectar();
                PreparedStatement ps = conn.prepareStatement(sql);){
            
                ps.setInt(1, p.getId());
            
                ps.executeUpdate();
                System.out.println("Projeto apagado.");
            
            }catch(SQLException e){
                System.out.println("erro:"+e);
                return false;
            }
            
        }
            return true;

    }
}
