package com.mycompany.projetogerenciadordetarefas.views;

import com.mycompany.projetogerenciadordetarefas.dao.UsuarioDAO;
import com.mycompany.projetogerenciadordetarefas.models.Usuario;
import com.mycompany.projetogerenciadordetarefas.util.SenhaUtil;
import com.mycompany.projetogerenciadordetarefas.util.Sessao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Modal de perfil do usuário logado.
 * Permite editar nome/email e trocar a senha.
 *
 * Versão: 1.0
 * Autor: Luiz Fernando
 */
public class ModalPerfil extends JDialog {

    private final UsuarioDAO dao = new UsuarioDAO();

    // --- Aba Dados ---
    private JTextField txtNome;
    private JTextField txtEmail;

    // --- Aba Senha ---
    private JPasswordField txtSenhaAtual;
    private JPasswordField txtNovaSenha;
    private JPasswordField txtConfirmarSenha;

    public ModalPerfil(JFrame parent) {
        super(parent, "Meu Perfil", true);
        setSize(460, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        Usuario usuario = Sessao.getUsuario();

        // ==============================
        // Cabeçalho com avatar e nome
        // ==============================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(111, 66, 193));
        header.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblAvatar = new JLabel(iniciais(usuario.getNome()));
        lblAvatar.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblAvatar.setForeground(Color.WHITE);
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        lblAvatar.setPreferredSize(new Dimension(52, 52));
        lblAvatar.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 2));
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(new Color(90, 48, 170));

        JPanel infoHeader = new JPanel(new GridLayout(2, 1, 0, 2));
        infoHeader.setOpaque(false);
        infoHeader.setBorder(new EmptyBorder(0, 14, 0, 0));

        JLabel lblNomeHeader = new JLabel(usuario.getNome());
        lblNomeHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNomeHeader.setForeground(Color.WHITE);

        JLabel lblEmailHeader = new JLabel(usuario.getEmail());
        lblEmailHeader.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEmailHeader.setForeground(new Color(210, 200, 240));

        infoHeader.add(lblNomeHeader);
        infoHeader.add(lblEmailHeader);

        header.add(lblAvatar, BorderLayout.WEST);
        header.add(infoHeader, BorderLayout.CENTER);

        // ==============================
        // Abas (Dados e Senha)
        // ==============================
        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        abas.addTab("Dados pessoais", buildAbaDados(usuario));
        abas.addTab("Trocar senha",   buildAbaSenha());

        // ==============================
        // Montagem final
        // ==============================
        add(header, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);
    }

    // ============================================================
    // Aba: Dados pessoais
    // ============================================================
    private JPanel buildAbaDados(Usuario usuario) {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(20, 25, 20, 25));
        painel.setBackground(Color.WHITE);

        txtNome = new JTextField(usuario.getNome());
        txtEmail = new JTextField(usuario.getEmail());

        painel.add(bloco("Nome", txtNome));
        painel.add(Box.createVerticalStrut(12));
        painel.add(bloco("Email", txtEmail));
        painel.add(Box.createVerticalStrut(20));

        JButton btnSalvar = botaoPrimario("Salvar alterações");
        btnSalvar.addActionListener(e -> salvarDados());
        painel.add(alinhado(btnSalvar));

        return painel;
    }

    // ============================================================
    // Aba: Trocar senha
    // ============================================================
    private JPanel buildAbaSenha() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(20, 25, 20, 25));
        painel.setBackground(Color.WHITE);

        txtSenhaAtual      = new JPasswordField();
        txtNovaSenha       = new JPasswordField();
        txtConfirmarSenha  = new JPasswordField();

        painel.add(bloco("Senha atual", txtSenhaAtual));
        painel.add(Box.createVerticalStrut(12));
        painel.add(bloco("Nova senha", txtNovaSenha));
        painel.add(Box.createVerticalStrut(12));
        painel.add(bloco("Confirmar nova senha", txtConfirmarSenha));
        painel.add(Box.createVerticalStrut(20));

        JButton btnTrocar = botaoPrimario("Trocar senha");
        btnTrocar.addActionListener(e -> trocarSenha());
        painel.add(alinhado(btnTrocar));

        return painel;
    }

    // ============================================================
    // Ações
    // ============================================================
    private void salvarDados() {
        String novoNome  = txtNome.getText().trim();
        String novoEmail = txtEmail.getText().trim();

        if (novoNome.isEmpty() || novoEmail.isEmpty()) {
            erro("Preencha todos os campos.");
            return;
        }

        try {
            Usuario usuario = Sessao.getUsuario();
            usuario.setNome(novoNome);
            usuario.setEmail(novoEmail);

            boolean ok = dao.atualizar(usuario);
            if (ok) {
                Sessao.setUsuario(usuario);
                JOptionPane.showMessageDialog(this,
                    "Dados atualizados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                erro("Não foi possível atualizar. Tente novamente.");
            }
        } catch (IllegalArgumentException ex) {
            erro(ex.getMessage());
        }
    }

    private void trocarSenha() {
        String senhaAtual    = new String(txtSenhaAtual.getPassword());
        String novaSenha     = new String(txtNovaSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());

        if (senhaAtual.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            erro("Preencha todos os campos.");
            return;
        }

        Usuario usuario = Sessao.getUsuario();

        // Verifica a senha atual
        if (!SenhaUtil.verificarSenha(senhaAtual, usuario.getSenha())) {
            erro("Senha atual incorreta.");
            txtSenhaAtual.setText("");
            txtSenhaAtual.requestFocus();
            return;
        }

        if (novaSenha.length() < 4) {
            erro("A nova senha deve ter pelo menos 4 caracteres.");
            return;
        }

        if (!novaSenha.equals(confirmarSenha)) {
            erro("As novas senhas não coincidem.");
            txtNovaSenha.setText("");
            txtConfirmarSenha.setText("");
            txtNovaSenha.requestFocus();
            return;
        }

        boolean ok = dao.atualizarSenha(usuario.getId(), novaSenha);
        if (ok) {
            // Atualiza o hash na sessão para que verificações futuras funcionem
            usuario.setSenha(SenhaUtil.criptografarSenha(novaSenha));
            Sessao.setUsuario(usuario);
            JOptionPane.showMessageDialog(this,
                "Senha alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            txtSenhaAtual.setText("");
            txtNovaSenha.setText("");
            txtConfirmarSenha.setText("");
        } else {
            erro("Erro ao alterar a senha. Tente novamente.");
        }
    }

    // ============================================================
    // Utilitários de layout
    // ============================================================
    private JPanel bloco(String titulo, JComponent campo) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lbl = new JLabel(titulo.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(130, 130, 130));

        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220)),
            new EmptyBorder(6, 10, 6, 10)
        ));

        p.add(lbl, BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        return p;
    }

    private JButton botaoPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(111, 66, 193));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 38));
        return btn;
    }

    private JPanel alinhado(JButton btn) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        p.add(btn);
        return p;
    }

    private String iniciais(String nome) {
        String[] partes = nome.trim().split("\\s+");
        if (partes.length >= 2) {
            return String.valueOf(partes[0].charAt(0)).toUpperCase()
                 + String.valueOf(partes[1].charAt(0)).toUpperCase();
        }
        return nome.substring(0, Math.min(2, nome.length())).toUpperCase();
    }

    private void erro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}