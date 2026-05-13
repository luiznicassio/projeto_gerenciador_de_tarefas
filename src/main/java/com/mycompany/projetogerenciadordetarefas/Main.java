package com.mycompany.projetogerenciadordetarefas;
import com.mycompany.projetogerenciadordetarefas.models.Tarefa;
import com.mycompany.projetogerenciadordetarefas.dao.TarefaDAO;
import com.mycompany.projetogerenciadordetarefas.dao.UsuarioDAO;
import com.mycompany.projetogerenciadordetarefas.models.Usuario;
import java.util.List;
import java.time.LocalDate;
import com.mycompany.projetogerenciadordetarefas.models.Projeto;
import com.mycompany.projetogerenciadordetarefas.dao.ProjetoDAO;
import java.sql.SQLException;

/**
 * Classe principal do sistema Gerenciador de Tarefas.
 * Responsável por iniciar a execução da aplicação.
 * 
 * Versão: 1.0
 * Autor: Luiz Fernando
 * Data: 28/04/2026
 */
public class Main {
    public static void main(String[] args) {
       new Sistema();
      
      
     try{
       
         
        
        
      }catch(Exception e){
           System.out.println("erro: "+e.getMessage());
        }
      
    }
}