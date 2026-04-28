package com.mycompany.projetogerenciadordetarefas.views;

import com.mycompany.projetogerenciadordetarefas.dao.UsuarioDAO;
import com.mycompany.projetogerenciadordetarefas.models.Usuario;
import com.mycompany.projetogerenciadordetarefas.util.Sessao;
import java.awt.*;
import java.util.prefs.Preferences;
import javax.swing.*;

public class TelaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public TelaLogin() {
        setTitle("Login");
        setSize(650, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        add(txtSenha);

        btnEntrar = new JButton("Entrar");
        add(new JLabel());
        add(btnEntrar);

        btnEntrar.addActionListener(e -> fazerLogin());
    }

    private void fazerLogin() {
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.login(email, senha);

        if(usuario != null){
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