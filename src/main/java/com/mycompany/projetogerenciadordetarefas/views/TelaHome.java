package com.mycompany.projetogerenciadordetarefas.views;

import com.mycompany.projetogerenciadordetarefas.models.Usuario;
import com.mycompany.projetogerenciadordetarefas.util.Sessao;
import java.awt.*;
import javax.swing.*;

public class TelaHome extends JFrame {

    private JLabel lblBemVindo;
    private JButton btnLogout;

    public TelaHome() {
        setTitle("Home");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Usuario usuario = Sessao.getUsuario();

        lblBemVindo = new JLabel("Bem-vindo, " + usuario.getNome() +" || "+ usuario.getEmail(), SwingConstants.CENTER);
        add(lblBemVindo, BorderLayout.CENTER);

        btnLogout = new JButton("Logout");
        add(btnLogout, BorderLayout.SOUTH);

        btnLogout.addActionListener(e -> logout());
    }

    private void logout() {
        Sessao.logout();

        new TelaLogin().setVisible(true);
        dispose();
    }
}