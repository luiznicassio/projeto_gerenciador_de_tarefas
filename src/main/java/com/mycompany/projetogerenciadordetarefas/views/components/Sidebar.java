package com.mycompany.projetogerenciadordetarefas.views.components;

import com.mycompany.projetogerenciadordetarefas.util.Sessao;
import com.mycompany.projetogerenciadordetarefas.views.TelaLogin;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Sidebar extends JPanel {

    public JButton btnProjetos;
    public JButton btnTarefas;
    public JButton btnCalendario;
    public JButton btnEquipe;
    public JButton btnLogout;

    public Sidebar() {

        setPreferredSize(new Dimension(240, 0));
        setBackground(new Color(15, 23, 42));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 15, 20, 15));

        // logo
        JLabel logo = new JLabel("FlowPro");

        logo.setForeground(Color.WHITE);

        logo.setFont(
            new Font("Segoe UI", Font.BOLD, 28)
        );

        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(logo);

        add(Box.createRigidArea(
            new Dimension(0, 40)
        ));

        // botões
        btnProjetos = criarBotao("Projetos");

        btnTarefas = criarBotao("Tarefas");

        btnCalendario = criarBotao("Calendário");

        btnEquipe = criarBotao("Equipe");

        btnLogout = criarBotao("Logout");

        add(btnProjetos);

        add(Box.createRigidArea(
            new Dimension(0, 10)
        ));

        add(btnTarefas);

        add(Box.createRigidArea(
            new Dimension(0, 10)
        ));

        add(btnCalendario);

        add(Box.createRigidArea(
            new Dimension(0, 10)
        ));

        add(btnEquipe);

        add(Box.createVerticalGlue());

        add(btnLogout);
        
     

    }

    private JButton criarBotao(String texto){

        JButton btn = new JButton(texto);

        btn.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                45
            )
        );

        btn.setFocusPainted(false);

        btn.setBackground(
            new Color(30, 41, 59)
        );

        btn.setForeground(Color.WHITE);

        btn.setBorderPainted(false);

        btn.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                14
            )
        );

        btn.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        return btn;
    }
  
}