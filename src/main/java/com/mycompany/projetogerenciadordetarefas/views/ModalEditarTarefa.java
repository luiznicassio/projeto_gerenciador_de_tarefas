package com.mycompany.projetogerenciadordetarefas.views;

import com.mycompany.projetogerenciadordetarefas.dao.TarefaDAO;
import com.mycompany.projetogerenciadordetarefas.models.Tarefa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ModalEditarTarefa extends JDialog {

    private JTextField txtTitulo;
    private JTextArea txtDescricao;
    private JFormattedTextField txtData; // Melhor forma de pegar data com máscara
    private JComboBox<String> cbPrioridade;
    private JComboBox<String> cbCategoria;

    private final Tarefa tarefa;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ModalEditarTarefa(JFrame parent, Tarefa tarefa) {
        super(parent, "Editar Registro", true);
        this.tarefa = tarefa;

        setSize(480, 620);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Painel Principal com fundo branco
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));
        painel.setBackground(Color.WHITE);

        // --- LINHA SUPERIOR (Título e Data menores) ---
        JPanel row1 = new JPanel(new GridLayout(1, 2, 15, 0));
        row1.setBackground(Color.WHITE);
        row1.setMaximumSize(new Dimension(1000, 60));

        txtTitulo = new JTextField(tarefa.getTitulo());
        txtTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(mascaraData);
            // Formata a data atual do objeto para o campo
            txtData.setText(tarefa.getDataVencimento().format(formatter));
        } catch (Exception e) {
            txtData = new JFormattedTextField();
        }

        row1.add(criarBlocoInput("TÍTULO", txtTitulo));
        row1.add(criarBlocoInput("DATA (DD/MM/AAAA)", txtData));

        // --- LINHA CENTRAL (Descrição Grande) ---
        txtDescricao = new JTextArea(tarefa.getDescricao());
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        
        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        scrollDesc.setPreferredSize(new Dimension(0, 280)); // Área de texto maior
        scrollDesc.setBorder(new LineBorder(new Color(230, 230, 230)));

        // --- LINHA DE SELETORES (Prioridade e Categoria) ---
        JPanel row3 = new JPanel(new GridLayout(1, 2, 15, 0));
        row3.setBackground(Color.WHITE);
        row3.setMaximumSize(new Dimension(1000, 60));

        cbPrioridade = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
        cbCategoria = new JComboBox<>(new String[]{"Trabalho", "Estudos", "Pessoal"});
        
        // Mantém a funcionalidade de setar os índices originais
        cbPrioridade.setSelectedIndex(tarefa.getPrioridadeId() - 1);
        cbCategoria.setSelectedIndex(tarefa.getCategoriaId() - 1);

        row3.add(criarBlocoInput("PRIORIDADE", cbPrioridade));
        row3.add(criarBlocoInput("CATEGORIA", cbCategoria));

        // Adicionando tudo ao painel
        painel.add(row1);
        painel.add(Box.createVerticalStrut(20));
        painel.add(label("DESCRIÇÃO"));
        painel.add(Box.createVerticalStrut(5));
        painel.add(scrollDesc);
        painel.add(Box.createVerticalStrut(20));
        painel.add(row3);

        // --- BOTÕES ---
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botoes.setBackground(new Color(245, 245, 245));

        JButton btnExcluir = new JButton("Excluir");
        estilizarBotao(btnExcluir, new Color(220, 53, 69), true); // Transparente com texto vermelho

        JButton btnSalvar = new JButton("Salvar Alterações");
        estilizarBotao(btnSalvar, new Color(0, 102, 204), false); // Azul sólido

        btnSalvar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());

        botoes.add(btnExcluir);
        botoes.add(btnSalvar);

        add(painel, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
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

    private void estilizarBotao(JButton btn, Color cor, boolean ghost) {
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        if (ghost) {
            btn.setForeground(cor);
            btn.setContentAreaFilled(false);
            btn.setBorder(BorderFactory.createLineBorder(cor));
        } else {
            btn.setBackground(cor);
            btn.setForeground(Color.WHITE);
            btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        }
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void atualizar() {
        try {
            // Mantém a funcionalidade original de atualizar o objeto
            tarefa.setTitulo(txtTitulo.getText().trim());
            tarefa.setDescricao(txtDescricao.getText().trim());
           
            // Converte a string da máscara dd/MM/yyyy para LocalDate
            tarefa.setDataVencimento(LocalDate.parse(txtData.getText(), formatter));

            tarefa.setPrioridadeId(cbPrioridade.getSelectedIndex() + 1);
            tarefa.setCategoriaId(cbCategoria.getSelectedIndex() + 1);
            
            // Descomente a linha abaixo para persistir no banco:
            new TarefaDAO().atualizar(tarefa);

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: Verifique se a data está completa.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir esta tarefa permanentemente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // new TarefaDAO().deletar(tarefa.getId());
            new TarefaDAO().apagar(tarefa.getId());
            dispose();
        }
    }
}