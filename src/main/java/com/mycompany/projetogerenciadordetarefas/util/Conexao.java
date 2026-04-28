package com.mycompany.projetogerenciadordetarefas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
   
    private static final String URL = "jdbc:mysql://localhost:3306/gerenciador_tarefas_db";
    private static final String USER = "root";
    private static final String PASSWORD = "1011";

    public static Connection conectar() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            /*
             //Testar conexao
            System.out.println("Conexão com o banco de dados realizada com sucesso!");
            */
            return conn;
        } catch (SQLException e) {
           
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
 
}