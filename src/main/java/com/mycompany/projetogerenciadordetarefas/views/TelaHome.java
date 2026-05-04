package com.mycompany.projetogerenciadordetarefas.views;

import com.mycompany.projetogerenciadordetarefas.dao.TarefaDAO;
import com.mycompany.projetogerenciadordetarefas.models.Tarefa;
import com.mycompany.projetogerenciadordetarefas.util.Sessao;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TelaHome extends JFrame {

    private JPanel colPendente, colAndamento, colConcluido;

    public TelaHome() {
        setTitle("Gerenciador de Tarefas");
        setSize(1500, 950);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        if (Sessao.getUsuario() == null) {
            new TelaLogin().setVisible(true);
            dispose();
            return;
        }

        // --- MENU LATERAL ---
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(250, getHeight()));
        menu.setBackground(new Color(33, 37, 43));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel lblUser = new JLabel("<html><center><font size='5' color='white'><b>" + Sessao.getUsuario().getNome() + "</b></font><br><font color='#abb2bf'>" + Sessao.getUsuario().getEmail() + "</font></center></html>");
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton btnNovaTarefa = criarBotaoMenu("Nova Tarefa");
        JButton btnPerfil = criarBotaoMenu("Perfil");
        JButton btnLogout = criarBotaoMenu("Logout");
        btnLogout.addActionListener(e -> logout());

        menu.add(lblUser);
        menu.add(Box.createRigidArea(new Dimension(0, 30)));
        menu.add(btnNovaTarefa);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(btnPerfil);
        menu.add(Box.createVerticalGlue());
        menu.add(btnLogout);

        add(menu, BorderLayout.WEST);

        // --- PAINEL KANBAN ---
        JPanel kanban = new JPanel(new GridLayout(1, 3, 15, 0));
        kanban.setBorder(new EmptyBorder(15, 15, 15, 15));
        kanban.setBackground(new Color(240, 242, 245));

        colPendente = criarLista(1);
        colAndamento = criarLista(2);
        colConcluido = criarLista(3);

        kanban.add(criarColuna("Pendente", colPendente));
        kanban.add(criarColuna("Em andamento", colAndamento));
        kanban.add(criarColuna("Concluído", colConcluido));

        add(kanban, BorderLayout.CENTER);
        carregarTarefas();
    }

    private JPanel criarLista(int statusId) {
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(new Color(240, 242, 245)); // Cor levemente diferente para contraste
        lista.setBorder(new EmptyBorder(10, 10, 10, 10));

        lista.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) { return true; }
            @Override
            public boolean importData(TransferSupport support) {
                try {
                    String idTarefa = (String) support.getTransferable().getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
                    new TarefaDAO().atualizarStatus(Integer.parseInt(idTarefa), statusId);
                    carregarTarefas();
                    return true;
                } catch (Exception e) { return false; }
            }
        });
        return lista;
    }

    private JPanel criarColuna(String titulo, JPanel lista) {
        JPanel coluna = new JPanel(new BorderLayout());
        coluna.setBackground(Color.WHITE);
        coluna.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        JLabel lbl = new JLabel(titulo, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setBorder(new EmptyBorder(15, 0, 15, 0));
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        coluna.add(lbl, BorderLayout.NORTH);
        coluna.add(scroll, BorderLayout.CENTER);
        return coluna;
    }

    private JPanel criarCard(Tarefa t) {
        int alturaFixa = 225;

        // O Wrapper é essencial. Usamos BorderLayout para ele forçar o card a ocupar toda a largura.
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, 0, 15, 0)); // Espaçamento inferior entre cards
        
        // 🔥 IMPORTANTE: BoxLayout exige que componentes tenham o mesmo AlignmentX para não quebrar
        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Travando altura, mas deixando a largura livre para o container
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, alturaFixa));
        wrapper.setMinimumSize(new Dimension(200, alturaFixa));
        wrapper.setPreferredSize(new Dimension(400, alturaFixa));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Barra Superior
        JPanel barra = new JPanel();
        barra.setPreferredSize(new Dimension(0, 8));
        try { barra.setBackground(Color.decode(t.getCategoriaCor())); } catch (Exception e) { barra.setBackground(Color.GRAY); }

        // Conteúdo
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBorder(new EmptyBorder(20, 20, 20, 20));
        conteudo.setOpaque(false);

        JLabel lblTitulo = new JLabel(t.getTitulo());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblData = new JLabel("Prazo: " + (t.getDataVencimento() != null ? t.getDataVencimento() : "Sem data"));
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblData.setForeground(new Color(120, 120, 120));
        lblData.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPrioridade = new JLabel(t.getPrioridadeNome() != null ? t.getPrioridadeNome().toUpperCase() : "NORMAL");
        lblPrioridade.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPrioridade.setAlignmentX(Component.LEFT_ALIGNMENT);
        try { 
            lblPrioridade.setForeground(Color.decode(t.getPrioridadeCor())); 
        } catch (Exception e) { 
            lblPrioridade.setForeground(Color.DARK_GRAY); 
        }

        conteudo.add(lblTitulo);
        conteudo.add(Box.createVerticalStrut(10));
        conteudo.add(lblData);
        conteudo.add(Box.createVerticalGlue());
        conteudo.add(lblPrioridade);

        card.add(barra, BorderLayout.NORTH);
        card.add(conteudo, BorderLayout.CENTER);
        wrapper.add(card, BorderLayout.CENTER);

        // Lógica de Drag
        card.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JComponent comp = (JComponent) e.getSource();
                TransferHandler th = new TransferHandler() {
                    @Override
                    protected Transferable createTransferable(JComponent c) {
                        return new StringSelection(String.valueOf(t.getId()));
                    }
                    @Override
                    public int getSourceActions(JComponent c) { return COPY; }
                };
                comp.setTransferHandler(th);
                th.exportAsDrag(comp, e, TransferHandler.COPY);
            }
        });

        return wrapper;
    }

    private void carregarTarefas() {
        colPendente.removeAll();
        colAndamento.removeAll();
        colConcluido.removeAll();

        TarefaDAO dao = new TarefaDAO();
        List<Tarefa> tarefas = dao.listarPorUsuario(Sessao.getUsuario().getId());
        
        for (Tarefa t : tarefas) {
            JPanel card = criarCard(t);
            if (t.getStatusId() == 1) colPendente.add(card);
            else if (t.getStatusId() == 2) colAndamento.add(card);
            else colConcluido.add(card);
        }

        // Mantém os cards no topo
        colPendente.add(Box.createVerticalGlue());
        colAndamento.add(Box.createVerticalGlue());
        colConcluido.add(Box.createVerticalGlue());

        repaint();
        revalidate();
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 58, 64));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Efeito Hover simples
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(70, 78, 86)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(52, 58, 64)); }
        });
        
        return btn;
    }

    private void logout() {
        Sessao.logout();
        new TelaLogin().setVisible(true);
        dispose();
    }
}