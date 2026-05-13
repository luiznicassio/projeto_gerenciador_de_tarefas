package com.mycompany.projetogerenciadordetarefas.views.modal;

import com.mycompany.projetogerenciadordetarefas.dao.ProjetoDAO;
import com.mycompany.projetogerenciadordetarefas.models.Projeto;
import com.mycompany.projetogerenciadordetarefas.util.Sessao;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModalNovoProjeto extends JDialog {
    private JTextField txtTitulo;
    private JTextArea txtDescricao;
    private JButton btnCor, btnCriar;
    private Color corSelecionada = new Color(99, 102, 241);

    // Paleta de Cores Coerente
    private final Color BG_MODAL = new Color(15, 20, 26);
    private final Color INPUT_BG = new Color(30, 39, 58);
    private final Color TEXT_PRIMARY = new Color(243, 244, 246);
    private final Color TEXT_SECONDARY = new Color(156, 163, 175);
    private final Color BORDER_COLOR = new Color(50, 55, 70);

    //Variavel para dar reload na lista de tarefas 
    private Runnable onProjetoCriado;
    
    public ModalNovoProjeto(JFrame parent, Runnable onProjetoCriado) {
        super(parent, "Novo Projeto", true);
        configurarJanela(parent);
        this.onProjetoCriado = onProjetoCriado;//O estada da pagina 

        JPanel painelPrincipal = new JPanel(new BorderLayout(0, 20));
        painelPrincipal.setBackground(BG_MODAL);
        painelPrincipal.setBorder(new EmptyBorder(30, 30, 30, 30));
        add(painelPrincipal);

        // Header
        JLabel lblTituloModal = new JLabel("Criar Novo Projeto");
        lblTituloModal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloModal.setForeground(TEXT_PRIMARY);
        painelPrincipal.add(lblTituloModal, BorderLayout.NORTH);

        // Formulário
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        txtTitulo = criarInputModerno("Título do Projeto");
        txtDescricao = criarTextAreaModerna();
        JScrollPane scrollDesc = configurarScroll(txtDescricao);

        btnCor = criarBotaoCor();

        form.add(criarLabel("Título"));
        form.add(Box.createVerticalStrut(8));
        form.add(txtTitulo);
        form.add(Box.createVerticalStrut(20));
        form.add(criarLabel("Descrição"));
        form.add(Box.createVerticalStrut(8));
        form.add(scrollDesc);
        form.add(Box.createVerticalStrut(20));
        form.add(criarLabel("Cor de Identidade"));
        form.add(Box.createVerticalStrut(8));
        form.add(btnCor);

        painelPrincipal.add(form, BorderLayout.CENTER);

        // Botão de Ação
        btnCriar = criarBotaoPrincipal();
        painelPrincipal.add(btnCriar, BorderLayout.SOUTH);
    }

    private void configurarJanela(JFrame parent) {
        setSize(500, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        // Remove a borda nativa feia se desejar, mas manteremos por padrão para portabilidade
    }

    /* =====================================================
       COMPONENTES CUSTOMIZADOS
    ===================================================== */

    private JTextField criarInputModerno(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setBackground(new Color(0,0,0,0));
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(10, 15, 10, 15));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return field;
    }

    private JTextArea criarTextAreaModerna() {
        JTextArea area = new JTextArea(4, 20);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setBackground(INPUT_BG);
        area.setForeground(TEXT_PRIMARY);
        area.setCaretColor(TEXT_PRIMARY);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
        return area;
    }

    private JScrollPane configurarScroll(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        return scroll;
    }

    private JButton criarBotaoCor() {
        JButton btn = new JButton("Selecionar Cor do Card") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(corSelecionada);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.addActionListener(e -> selecionarCor());
        return btn;
    }

    private JButton criarBotaoPrincipal() {
        JButton btn = new JButton("Criar Projeto") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(37, 99, 235));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setPreferredSize(new Dimension(0, 50));
        btn.addActionListener(e -> criarProjeto());
        return btn;
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(TEXT_SECONDARY);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }

    /* =====================================================
       LÓGICA
    ===================================================== */

    private void selecionarCor() {
        Color novaCor = JColorChooser.showDialog(this, "Identidade do Projeto", corSelecionada);
        if (novaCor != null) {
            corSelecionada = novaCor;
            btnCor.repaint();
        }
    }

    private void criarProjeto() {
        if (txtTitulo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O título é obrigatório.");
            return;
        }

        try {
            Projeto projeto = new Projeto();
            projeto.setTitulo(txtTitulo.getText());
            projeto.setDescricao(txtDescricao.getText());
            projeto.setCor(String.format("#%02x%02x%02x", corSelecionada.getRed(), corSelecionada.getGreen(), corSelecionada.getBlue()));
            projeto.setCriador(Sessao.getUsuario().getId());

            if (new ProjetoDAO().cadastrar(projeto)) {
                if(onProjetoCriado != null){
                    onProjetoCriado.run();
                 }
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
}