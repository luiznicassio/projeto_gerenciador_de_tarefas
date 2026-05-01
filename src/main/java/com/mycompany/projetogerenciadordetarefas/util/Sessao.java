package com.mycompany.projetogerenciadordetarefas.util;

import com.mycompany.projetogerenciadordetarefas.models.Usuario;
import java.util.prefs.Preferences;

/**
 * Classe responsável por gerenciar a sessão do usuário logado.
 * Armazena temporariamente o usuário autenticado e permite encerrar a sessão.
 * 
 * Versão: 1.0
 * Autor: Luiz Fernando
 * Data: 28/04/2026
 */
public class Sessao {
    
    // Armazena o usuário atualmente logado no sistema
    private static Usuario usuarioLogado;

    /**
     * Define o usuário logado na sessão.
     * 
     * @param usuario Usuário autenticado
     */
    public static void setUsuario(Usuario usuario){
        usuarioLogado = usuario;
    }

    /**
     * Retorna o usuário atualmente logado.
     * 
     * @return usuário logado
     */
    public static Usuario getUsuario(){
        return usuarioLogado;
    }

    /**
     * Encerra a sessão do usuário.
     * Remove o usuário armazenado e apaga o login salvo nas preferências.
     */
    public static void logout(){
        usuarioLogado = null;

        // Remove o ID do usuário salvo localmente
        Preferences prefs = Preferences.userRoot();
        prefs.remove("usuarioLogado");
    }
}