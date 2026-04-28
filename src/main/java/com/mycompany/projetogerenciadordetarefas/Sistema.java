package com.mycompany.projetogerenciadordetarefas;

import com.mycompany.projetogerenciadordetarefas.dao.UsuarioDAO;
import com.mycompany.projetogerenciadordetarefas.models.Usuario;
import com.mycompany.projetogerenciadordetarefas.util.Sessao;
import com.mycompany.projetogerenciadordetarefas.views.TelaHome;
import com.mycompany.projetogerenciadordetarefas.views.TelaLogin;
import java.util.prefs.Preferences;

/**
 * Classe responsável por iniciar o sistema.
 * Verifica se há um usuário salvo na sessão e direciona
 * para a tela inicial ou para a tela de login.
 * 
 * Versão: 1.0
 * Autor: Luiz Fernando
 * Data: 28/04/2026
 */
public class Sistema {

    /**
     * Construtor da classe.
     * Ao criar o sistema, chama o método de inicialização.
     */
    public Sistema() {
        iniciar();
    }

    /**
     * Inicializa o sistema verificando se existe um usuário logado.
     * Caso exista, abre a tela principal.
     * Caso contrário, abre a tela de login.
     */
    private void iniciar() {

        // Cria acesso ao banco de usuários
        UsuarioDAO dao = new UsuarioDAO();

        // Acessa as preferências salvas localmente
        Preferences prefs = Preferences.userRoot();

        // Recupera o ID do usuário logado, ou -1 caso não exista
        int idUsuario = prefs.getInt("usuarioLogado", -1);

        // Verifica se existe usuário salvo
        if(idUsuario != -1){
            Usuario usuario = dao.buscarPorId(idUsuario);

            // Se o usuário existir, salva na sessão e abre a tela inicial
            if(usuario != null){
                Sessao.setUsuario(usuario);
                new TelaHome().setVisible(true);
                return;
            }
        }

        // Caso não exista usuário logado, abre a tela de login
        new TelaLogin().setVisible(true);
    }
}