package com.mycompany.projetogerenciadordetarefas.views;

import com.mycompany.projetogerenciadordetarefas.dao.UsuarioDAO;
import com.mycompany.projetogerenciadordetarefas.models.Usuario;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Tela de cadastro de novo usuário.
 *
 * Versão: 1.0
 * Autor: Luiz Fernando
 */
public class TelaCadastro extends JFrame {

    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmarSenha;

    public TelaCadastro() {
        setTitle("Cadastro");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel painelPrincipal = new JPanel(new GridLayout(1, 2));

        // --- Painel esquerdo (visual) ---
        JPanel painelEsquerdo = new JPanel(new GridBagLayout());
        painelEsquerdo.setBackground(new Color(111, 66, 193));
        painelEsquerdo.setBorder(new EmptyBorder(50, 40, 50, 40));

        GridBagConstraints gbcEsq = new GridBagConstraints();
        gbcEsq.gridx = 0;
        gbcEsq.insets = new Insets(10, 10, 10, 10);
        gbcEsq.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("Crie sua conta");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        gbcEsq.gridy = 0;
        painelEsquerdo.add(lblTitulo, gbcEsq);

        JLabel lblTexto = new JLabel(
            "<html><div style='text-align:center;'>Organize suas tarefas,<br>controle sua produtividade.</div></html>"
        );
        lblTexto.setForeground(Color.WHITE);
        lblTexto.setFont(new Font("SansSerif", Font.PLAIN, 15));
        gbcEsq.gridy = 1;
        painelEsquerdo.add(lblTexto, gbcEsq);

        JButton btnVoltar = new JButton("\u2190 Voltar ao Login");
        btnVoltar.setBackground(new Color(80, 40, 160));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBorderPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> voltarLogin());
        gbcEsq.gridy = 2;
        gbcEsq.insets = new Insets(30, 10, 10, 10);
        painelEsquerdo.add(btnVoltar, gbcEsq);

        // --- Painel direito (formulário) ---
        JPanel painelDireito = new JPanel(new GridBagLayout());
        painelDireito.setBackground(Color.WHITE);
        painelDireito.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel lblCadastro = new JLabel("Cadastro");
        lblCadastro.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblCadastro.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        painelDireito.add(lblCadastro, gbc);

        gbc.insets = new Insets(4, 0, 4, 0);

        txtNome = new JTextField();
        txtNome.setPreferredSize(new Dimension(250, 35));
        gbc.gridy = 1;
        painelDireito.add(rotulado("Nome", txtNome), gbc);

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(250, 35));
        gbc.gridy = 2;
        painelDireito.add(rotulado("Email", txtEmail), gbc);

        txtSenha = new JPasswordField();
        txtSenha.setPreferredSize(new Dimension(250, 35));
        gbc.gridy = 3;
        painelDireito.add(rotulado("Senha", txtSenha), gbc);

        txtConfirmarSenha = new JPasswordField();
        txtConfirmarSenha.setPreferredSize(new Dimension(250, 35));
        gbc.gridy = 4;
        painelDireito.add(rotulado("Confirmar Senha", txtConfirmarSenha), gbc);

        JButton btnCadastrar = new JButton("Criar conta");
        btnCadastrar.setBackground(new Color(111, 66, 193));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setPreferredSize(new Dimension(250, 40));
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        gbc.insets = new Insets(16, 0, 0, 0);
        painelDireito.add(btnCadastrar, gbc);

        btnCadastrar.addActionListener(e -> cadastrar());
        txtConfirmarSenha.addActionListener(e -> cadastrar());

        painelPrincipal.add(painelEsquerdo);
        painelPrincipal.add(painelDireito);
        add(painelPrincipal);
    }

    private JPanel rotulado(String titulo, JComponent campo) {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setOpaque(false);
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        p.add(lbl, BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        return p;
    }

    private void cadastrar() {
        String nome     = txtNome.getText().trim();
        String email    = txtEmail.getText().trim();
        String senha    = new String(txtSenha.getPassword()).trim();
        String confirma = new String(txtConfirmarSenha.getPassword()).trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!senha.equals(confirma)) {
            JOptionPane.showMessageDialog(this,
                "As senhas não coincidem.", "Aviso", JOptionPane.WARNING_MESSAGE);
            txtSenha.setText("");
            txtConfirmarSenha.setText("");
            txtSenha.requestFocus();
            return;
        }

        try {
            Usuario novoUsuario = new Usuario(nome, email, senha);
            new UsuarioDAO().cadastrar(novoUsuario);
            JOptionPane.showMessageDialog(this,
                "Conta criada com sucesso! Faça o login.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            voltarLogin();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "Erro de validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltarLogin() {
        new TelaLogin().setVisible(true);
        dispose();
    }
}