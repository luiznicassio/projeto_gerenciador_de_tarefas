package com.mycompany.projetogerenciadordetarefas;
import com.mycompany.projetogerenciadordetarefas.models.Usuario;
import com.mycompany.projetogerenciadordetarefas.util.Conexao;
import com.mycompany.projetogerenciadordetarefas.dao.UsuarioDAO;
import com.mycompany.projetogerenciadordetarefas.util.SenhaUtil;
/**
 *
 * @author nicas
 */
public class Main {

    public static void main(String[] args) {
       
        Usuario teste1 = new Usuario("teste3","teste3@gmail.com","1234");
        
       
        UsuarioDAO dao = new UsuarioDAO();
        dao.cadastrar(teste1);
        
        
    }
}
