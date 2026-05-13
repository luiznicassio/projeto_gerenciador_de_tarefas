package com.mycompany.projetogerenciadordetarefas.views;

import com.mycompany.projetogerenciadordetarefas.util.Sessao;
import com.mycompany.projetogerenciadordetarefas.views.components.Sidebar;
import com.mycompany.projetogerenciadordetarefas.views.paineis.PainelProjetos;

import javax.swing.*;

import java.awt.*;


public class TelaHome extends JFrame {

    private JPanel painelConteudo;

    public TelaHome(){

        setTitle("FlowPro");//nome do projeto (Provisório)

        setSize(1500, 800);//tamanho da view 

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // sidebar
        Sidebar sidebar = new Sidebar();
         sidebar.btnLogout.addActionListener(e -> logout());
        add(sidebar, BorderLayout.WEST);

        // painel conteúdo
        painelConteudo = new JPanel(new BorderLayout());
        add( painelConteudo,BorderLayout.CENTER);

        // abre projetos inicialmente
        mostrarProjetos();
        
        setVisible(true);
    }

    private void mostrarProjetos(){
        painelConteudo.removeAll();
        painelConteudo.add(new PainelProjetos(),BorderLayout.CENTER);
        painelConteudo.repaint();
        painelConteudo.revalidate();
    }
    
     private void logout() {
        int resposta = JOptionPane.showConfirmDialog(this,
            "Deseja sair da conta?",
            "Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {
            // limpa sessão
            Sessao.logout();
            // abre tela login
            SwingUtilities.invokeLater(() -> {
                new TelaLogin().setVisible(true);
            });
            // fecha TelaHome
            dispose();
        }   
        }
    }