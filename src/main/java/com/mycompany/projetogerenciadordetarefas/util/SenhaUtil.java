package com.mycompany.projetogerenciadordetarefas.util;


import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {

    public static String criptografarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }
    public static boolean verificarSenha(String senhaDigitada, String senhaHash) {
        return BCrypt.checkpw(senhaDigitada, senhaHash);
    }
}