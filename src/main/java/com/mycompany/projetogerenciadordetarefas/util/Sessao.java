package com.mycompany.projetogerenciadordetarefas.util;

import com.mycompany.projetogerenciadordetarefas.models.Usuario;
import java.util.prefs.Preferences;

public class Sessao {
    
    private static Usuario usuarioLogado;

    public static void setUsuario(Usuario usuario){
        usuarioLogado = usuario;
    }

    public static Usuario getUsuario(){
        return usuarioLogado;
    }

    public static void logout(){
        usuarioLogado = null;

        // ALTERADO: apagar id salvo
        Preferences prefs = Preferences.userRoot();
        prefs.remove("usuarioLogado");
    }
}