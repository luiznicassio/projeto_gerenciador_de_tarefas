package com.mycompany.projetogerenciadordetarefas.views;

import com.mycompany.projetogerenciadordetarefas.dao.TarefaDAO;
import com.mycompany.projetogerenciadordetarefas.models.Tarefa;
import com.mycompany.projetogerenciadordetarefas.util.Sessao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ModalNovaTarefa extends JDialog {

    private JTextField txtTitulo;
    private JTextArea txtDescricao;
    private JFormattedTextField txtData;
    private JComboBox<String> cbPrioridade;
    private JComboBox<String> cbCategoria;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ModalNovaTarefa(JFrame parent) {
        super(parent, "Nova Tarefa", true);

        setSize(480, 620);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Painel Principal
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));
        painel.setBackground(Color.WHITE);

        // --- LINHA SUPERIOR (Título e Data menores) ---
        JPanel row1 = new JPanel(new GridLayout(1, 2, 15, 0));
        row1.setBackground(Color.WHITE);
        row1.setMaximumSize(new Dimension(1000, 60));

        txtTitulo = new JTextField();
        txtTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        try {
            // Máscara dd/mm/aaaa para facilitar a digitação
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(mascaraData);
            txtData.setText(LocalDate.now().format(formatter)); // Sugere a data de hoje
        } catch (Exception e) {
            txtData = new JFormattedTextField();
        }

        row1.add(criarBlocoInput("TÍTULO", txtTitulo));
        row1.add(criarBlocoInput("DATA (DD/MM/AAAA)", txtData));

        // --- LINHA CENTRAL (Descrição Grande) ---
        txtDescricao = new JTextArea();
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        
        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        scrollDesc.setPreferredSize(new Dimension(0, 280)); 
        scrollDesc.setBorder(new LineBorder(new Color(230, 230, 230)));

        // --- LINHA DE SELETORES (Prioridade e Categoria) ---
        JPanel row3 = new JPanel(new GridLayout(1, 2, 15, 0));
        row3.setBackground(Color.WHITE);
        row3.setMaximumSize(new Dimension(1000, 60));

        cbPrioridade = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
        cbCategoria = new JComboBox<>(new String[]{"Trabalho", "Estudos", "Pessoal"});

        row3.add(criarBlocoInput("PRIORIDADE", cbPrioridade));
        row3.add(criarBlocoInput("CATEGORIA", cbCategoria));

        // Montagem
        painel.add(row1);
        painel.add(Box.createVerticalStrut(20));
        painel.add(label("DESCRIÇÃO DA TAREFA"));
        painel.add(Box.createVerticalStrut(5));
        painel.add(scrollDesc);
        painel.add(Box.createVerticalStrut(20));
        painel.add(row3);

        // --- BOTÃO SALVAR ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(new Color(245, 245, 245));

        JButton btnSalvar = new JButton("Criar Tarefa");
        btnSalvar.setBackground(new Color(0, 102, 204));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSalvar.addActionListener(e -> salvar());
        footer.add(btnSalvar);

        add(painel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel criarBlocoInput(String titulo, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setBackground(Color.WHITE);
        p.add(label(titulo), BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JLabel label(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(new Color(120, 120, 120));
        return l;
    }

    private void salvar() {
        if (txtTitulo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um título.");
            return;
        }

        try {
            Tarefa t = new Tarefa();
            t.setTitulo(txtTitulo.getText().trim());
            t.setDescricao(txtDescricao.getText().trim());
            
            // Converte a data da máscara para LocalDate
            t.setDataVencimento(LocalDate.parse(txtData.getText(), formatter));

            // Mantém sua lógica de prioridade
            String prioridade = cbPrioridade.getSelectedItem().toString();
            if (prioridade.equals("Baixa")) t.setPrioridadeId(1);
            else if (prioridade.equals("Média")) t.setPrioridadeId(2);
            else t.setPrioridadeId(3);

            // Mantém sua lógica de categoria
            String categoria = cbCategoria.getSelectedItem().toString();
            if (categoria.equals("Trabalho")) t.setCategoriaId(1);
            else if (categoria.equals("Estudos")) t.setCategoriaId(2);
            else t.setCategoriaId(3);

            t.setStatusId(1); 
            t.setUsuarioId(Sessao.getUsuario().getId());

            new TarefaDAO().criar(t);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: Verifique se a data está no formato correto (dd/mm/aaaa).");
        }
    }
}