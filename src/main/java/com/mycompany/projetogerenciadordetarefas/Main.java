package com.mycompany.projetogerenciadordetarefas;
import com.mycompany.projetogerenciadordetarefas.models.Tarefa;
import com.mycompany.projetogerenciadordetarefas.dao.TarefaDAO;
import java.util.List;
import java.time.LocalDate;
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
    //Cria uma nova instância do sistema e inicia a aplicação
    new Sistema();
    
      TarefaDAO dao = new TarefaDAO();

       
    

//        // =========================
//        // 2. TESTE DE LISTAGEM
//        // =========================
//        List<Tarefa> lista = dao.listarPorUsuario(1);
//
//        for (Tarefa t : lista) {
//
//            System.out.println("----------- TAREFA -----------");
//
//            System.out.println("Título: " + t.getTitulo());
//            System.out.println("Descrição: " + t.getDescricao());
//            System.out.println("Data: " + t.getDataVencimento());
//
//            System.out.println("Categoria: " + t.getCategoriaNome());
//            System.out.println("Cor Categoria: " + t.getCategoriaCor());
//
//            System.out.println("Status: " + t.getStatusNome());
//
//            System.out.println("Prioridade: " + t.getPrioridadeNome());
//
//            // se você adicionou no model
//            try {
//                System.out.println("Cor Prioridade: " + t.getPrioridadeCor());
//            } catch (Exception e) {}
//
//            System.out.println("-------------------------------\n");
//        };
    }
}