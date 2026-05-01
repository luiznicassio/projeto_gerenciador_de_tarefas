package com.mycompany.projetogerenciadordetarefas.views;

import com.mycompany.projetogerenciadordetarefas.dao.UsuarioDAO;
import com.mycompany.projetogerenciadordetarefas.models.Usuario;
import com.mycompany.projetogerenciadordetarefas.util.Sessao;

import java.awt.*;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TelaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public TelaLogin() {
        setTitle("Login");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel painelPrincipal = new JPanel(new GridLayout(1, 2));

       // Painel esquerdo
        JPanel painelEsquerdo = new JPanel(new GridBagLayout());
        painelEsquerdo.setBackground(new Color(111, 66, 193));
        painelEsquerdo.setBorder(new EmptyBorder(50, 40, 50, 40));

        GridBagConstraints gbcEsq = new GridBagConstraints();
        gbcEsq.gridx = 0;
        gbcEsq.insets = new Insets(10, 10, 10, 10);
        gbcEsq.anchor = GridBagConstraints.CENTER;

        JLabel lblTituloEsquerdo = new JLabel("Bem-vindo!");
        lblTituloEsquerdo.setForeground(Color.WHITE);
        lblTituloEsquerdo.setFont(new Font("SansSerif", Font.BOLD, 30));

        gbcEsq.gridy = 0;
        painelEsquerdo.add(lblTituloEsquerdo, gbcEsq);

        JLabel lblTexto = new JLabel(
             "<html><div style='text-align:center;'>Faça login para acessar<br>seu gerenciador de tarefas.</div></html>"
        );
        lblTexto.setForeground(Color.WHITE);
        lblTexto.setFont(new Font("SansSerif", Font.PLAIN, 16));

        gbcEsq.gridy = 1;
        painelEsquerdo.add(lblTexto, gbcEsq);

        // Painel direito
        JPanel painelDireito = new JPanel(new GridBagLayout());
        painelDireito.setBackground(Color.WHITE);
        painelDireito.setBorder(new EmptyBorder(50, 60, 50, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 0;
        painelDireito.add(lblLogin, gbc);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridy = 1;
        painelDireito.add(lblEmail, gbc);

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(250, 35));
        gbc.gridy = 2;
        painelDireito.add(txtEmail, gbc);

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridy = 3;
        painelDireito.add(lblSenha, gbc);

        txtSenha = new JPasswordField();
        txtSenha.setPreferredSize(new Dimension(250, 35));
        gbc.gridy = 4;
        painelDireito.add(txtSenha, gbc);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(new Color(111, 66, 193));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setPreferredSize(new Dimension(250, 40));

        gbc.gridy = 5;
        gbc.insets = new Insets(20, 0, 0, 0);
        painelDireito.add(btnEntrar, gbc);

        painelPrincipal.add(painelEsquerdo);
        painelPrincipal.add(painelDireito);

        add(painelPrincipal);
        
        //fazer login
        btnEntrar.addActionListener(e -> fazerLogin());
    }

    private void fazerLogin() {
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.login(email, senha);

        if (usuario != null) {
            Sessao.setUsuario(usuario);

            Preferences prefs = Preferences.userRoot();
            prefs.putInt("usuarioLogado", usuario.getId());

            new TelaHome().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha inválidos.");
        }
    }
}