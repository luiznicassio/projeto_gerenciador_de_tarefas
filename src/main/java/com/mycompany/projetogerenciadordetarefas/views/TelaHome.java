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
        setSize(1500, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        if (Sessao.getUsuario() == null) {
            new TelaLogin().setVisible(true);
            dispose();
            return;
        }

        // 🔥 SIDEBAR MODERNA
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(260, getHeight()));
        menu.setBackground(new Color(24, 26, 32));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel lblUser = new JLabel("<html><b style='font-size:16px; color:white'>" 
                + Sessao.getUsuario().getNome() + "</b><br>"
                + "<span style='color:#9aa4b2'>" 
                + Sessao.getUsuario().getEmail() + "</span></html>");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnNovaTarefa = criarBotaoMenu("＋ Nova Tarefa");
        JButton btnPerfil = criarBotaoMenu("Perfil");
        JButton btnLogout = criarBotaoMenu("Logout");

        btnLogout.addActionListener(e -> logout());

        menu.add(lblUser);
        menu.add(Box.createRigidArea(new Dimension(0, 40)));
        menu.add(btnNovaTarefa);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(btnPerfil);
        menu.add(Box.createVerticalGlue());
        menu.add(btnLogout);

        add(menu, BorderLayout.WEST);

        // 🔥 KANBAN MAIS LIMPO
        JPanel kanban = new JPanel(new GridLayout(1, 3, 20, 0));
        kanban.setBorder(new EmptyBorder(20, 20, 20, 20));
        kanban.setBackground(new Color(245, 247, 250));

        colPendente = criarLista(1);
        colAndamento = criarLista(2);
        colConcluido = criarLista(3);

        kanban.add(criarColuna("Pendente", colPendente));
        kanban.add(criarColuna("Em andamento", colAndamento));
        kanban.add(criarColuna("Concluído", colConcluido));

        add(kanban, BorderLayout.CENTER);
        btnNovaTarefa.addActionListener(e -> {
            new ModalNovaTarefa(this).setVisible(true);
            carregarTarefas(); // atualiza depois
         });
        carregarTarefas();
    }

    private JPanel criarLista(int statusId) {
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(new Color(245, 247, 250));
        lista.setBorder(new EmptyBorder(10, 10, 10, 10));

        lista.setTransferHandler(new TransferHandler() {
            public boolean canImport(TransferSupport support) { return true; }

            public boolean importData(TransferSupport support) {
                try {
                    String id = (String) support.getTransferable()
                            .getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);

                    new TarefaDAO().atualizarStatus(Integer.parseInt(id), statusId);
                    carregarTarefas();
                    return true;

                } catch (Exception e) {
                    return false;
                }
            }
        });

        return lista;
    }

    private JPanel criarColuna(String titulo, JPanel lista) {

        JPanel coluna = new JPanel(new BorderLayout());
        coluna.setBackground(Color.WHITE);
        coluna.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setBorder(new EmptyBorder(5, 5, 10, 5));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        coluna.add(lbl, BorderLayout.NORTH);
        coluna.add(scroll, BorderLayout.CENTER);

        return coluna;
    }

    private JPanel criarCard(Tarefa t) {

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, 0, 15, 0));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(0, 0, 0, 0)
        ));

        // 🔥 SOMBRA (fake)
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(0, 0, 0, 0)
        ));

        JPanel barra = new JPanel();
        barra.setPreferredSize(new Dimension(0, 6));
        try {
            barra.setBackground(Color.decode(t.getCategoriaCor()));
        } catch (Exception e) {
            barra.setBackground(Color.GRAY);
        }

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBorder(new EmptyBorder(15, 15, 15, 15));
        conteudo.setOpaque(false);

        JLabel titulo = new JLabel(t.getTitulo());
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel data = new JLabel("Prazo: " +
                (t.getDataVencimento() != null ? t.getDataVencimento() : "Sem data"));
        data.setForeground(new Color(120, 120, 120));

        JLabel prioridade = new JLabel(t.getPrioridadeNome());
        prioridade.setFont(new Font("Segoe UI", Font.BOLD, 12));

        try {
            prioridade.setForeground(Color.decode(t.getPrioridadeCor()));
        } catch (Exception e) {}

        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(data);
        conteudo.add(Box.createVerticalGlue());
        conteudo.add(prioridade);

        card.add(barra, BorderLayout.NORTH);
        card.add(conteudo, BorderLayout.CENTER);

        wrapper.add(card);

        // 🔥 HOVER SUAVE (SaaS feel)
       card.addMouseListener(new MouseAdapter() {

    private Point inicioDrag;
    private boolean arrastando = false;

    @Override
    public void mousePressed(MouseEvent e) {
        inicioDrag = e.getPoint();
        arrastando = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        // 🔥 SE NÃO ARRASTOU = CLICK
        if (!arrastando) {
            new ModalEditarTarefa(
                    (JFrame) SwingUtilities.getWindowAncestor(card),
                    t
            ).setVisible(true);

            carregarTarefas();
        }
    }
});

card.addMouseMotionListener(new MouseAdapter() {

    @Override
    public void mouseDragged(MouseEvent e) {

        // 🔥 DETECTA SE VIROU DRAG
        int dx = Math.abs(e.getX() - 0);
        int dy = Math.abs(e.getY() - 0);

        if (dx > 5 || dy > 5) {

            TransferHandler th = new TransferHandler() {
                protected Transferable createTransferable(JComponent c) {
                    return new StringSelection(String.valueOf(t.getId()));
                }

                public int getSourceActions(JComponent c) {
                    return COPY;
                }
            };

            card.setTransferHandler(th);
            th.exportAsDrag(card, e, TransferHandler.COPY);
        }
    }
});
        return wrapper;
    }

    private void carregarTarefas() {

        colPendente.removeAll();
        colAndamento.removeAll();
        colConcluido.removeAll();

        List<Tarefa> tarefas = new TarefaDAO()
                .listarPorUsuario(Sessao.getUsuario().getId());

        for (Tarefa t : tarefas) {
            JPanel card = criarCard(t);

            if (t.getStatusId() == 1) colPendente.add(card);
            else if (t.getStatusId() == 2) colAndamento.add(card);
            else colConcluido.add(card);
        }

        colPendente.add(Box.createVerticalGlue());
        colAndamento.add(Box.createVerticalGlue());
        colConcluido.add(Box.createVerticalGlue());

        repaint();
        revalidate();
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(40, 44, 52));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(60, 66, 78));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(40, 44, 52));
            }
        });

        return btn;
    }

    private void logout() {
        Sessao.logout();
        new TelaLogin().setVisible(true);
        dispose();
    }
    
    
}