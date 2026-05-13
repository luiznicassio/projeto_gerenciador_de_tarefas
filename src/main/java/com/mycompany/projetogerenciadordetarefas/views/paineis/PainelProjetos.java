package com.mycompany.projetogerenciadordetarefas.views.paineis;

/**
 * @author luiz fernando 
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import com.mycompany.projetogerenciadordetarefas.models.Projeto;
import com.mycompany.projetogerenciadordetarefas.dao.ProjetoDAO;
import com.mycompany.projetogerenciadordetarefas.util.Sessao;
import com.mycompany.projetogerenciadordetarefas.views.components.ProjetoCard;
import com.mycompany.projetogerenciadordetarefas.views.modal.ModalNovoProjeto;
import com.mycompany.projetogerenciadordetarefas.util.WrapLayout;

public class PainelProjetos extends JPanel {

    // Cores do Dark Theme combinando com o card
    private final Color BG_APP = new Color(15, 20, 30); // Fundo da tela (mais escuro que o card)
    private final Color COMPONENT_BG = new Color(30, 39, 58); // Fundo de inputs e cards
    private final Color TEXT_PRIMARY = new Color(243, 244, 246);
    private final Color TEXT_SECONDARY = new Color(156, 163, 175);
    private final Color BRAND_BLUE = new Color(37, 99, 235); // Azul do botão

    //criando grid 
    private ScrollablePanel grid = new ScrollablePanel(new WrapLayout(FlowLayout.LEFT, 20, 20));
    public PainelProjetos() {

        setLayout(new BorderLayout(0, 20));
        setBackground(BG_APP);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        /* ===================================================================
           1. TOPO: Títulos, Pesquisa e Botões
           =================================================================== */
        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);

        // --- Container para Título e Subtítulo ---
        JPanel titlesPanel = new JPanel();
        titlesPanel.setLayout(new BoxLayout(titlesPanel, BoxLayout.Y_AXIS));
        titlesPanel.setOpaque(false);

        JLabel titulo = new JLabel("Meus Projetos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(TEXT_PRIMARY);

        JLabel subtitulo = new JLabel("Projetos dos quais você faz parte");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(TEXT_SECONDARY);

        titlesPanel.add(titulo);
        titlesPanel.add(Box.createVerticalStrut(5));
        titlesPanel.add(subtitulo);

        // --- Container para Pesquisa e Ações ---
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionsPanel.setOpaque(false);

        // Campo de pesquisa estilizado
        JTextField txtPesquisa = new JTextField(" Buscar projetos...");
        txtPesquisa.setPreferredSize(new Dimension(250, 40));
        txtPesquisa.setBackground(COMPONENT_BG);
        txtPesquisa.setForeground(TEXT_PRIMARY);
        txtPesquisa.setCaretColor(Color.WHITE);
        txtPesquisa.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(50, 55, 70), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));

        // Botão + Novo Projeto
        JButton btnNovoProjeto = new JButton("+ Novo Projeto");
        btnNovoProjeto.setPreferredSize(new Dimension(140, 40));
        btnNovoProjeto.setBackground(BRAND_BLUE);
        btnNovoProjeto.setForeground(Color.WHITE);
        btnNovoProjeto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNovoProjeto.setFocusPainted(false);
        btnNovoProjeto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNovoProjeto.setBorder(BorderFactory.createEmptyBorder());

        actionsPanel.add(txtPesquisa);
        actionsPanel.add(btnNovoProjeto);

        topo.add(titlesPanel, BorderLayout.WEST);
        topo.add(actionsPanel, BorderLayout.EAST);

        add(topo, BorderLayout.NORTH);
        
        //logica do botao
        btnNovoProjeto.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

            new ModalNovoProjeto(frame,() -> carregarProjetos()).setVisible(true);
            
        });
        /* ===================================================================
           2. GRID: Lista de Projetos
           =================================================================== */
        
        // Agora usamos a nossa classe customizada que entende o Scrollable
       
        grid.setOpaque(false);

          carregarProjetos();
        /* ===================================================================
           3. SCROLLPANE: Rolagem estilizada
           =================================================================== */
        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        
        // Garante que a barra horizontal nunca apareça e a vertical sim
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }
    
    private void exibirAvisoVazio(JPanel container) {
        JLabel lbl = new JLabel("Nenhum projeto encontrado. Que tal criar um?");
        lbl.setForeground(TEXT_SECONDARY);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lbl.setBorder(new EmptyBorder(50, 0, 0, 0));
        container.add(lbl);
    }
    
    /* ===================================================================
       CLASSE AUXILIAR PARA ROLAGEM
       =================================================================== */
    private class ScrollablePanel extends JPanel implements Scrollable {
        public ScrollablePanel(LayoutManager layout) {
            super(layout);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 32;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true; // Força a quebra de linha do WrapLayout
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
    
    //logica para criar a lista de projetos 
    public void carregarProjetos(){
          grid.removeAll();

        try {
            ProjetoDAO dao = new ProjetoDAO();

            List<Projeto> projetos = dao.listarProjetos(Sessao.getUsuario().getId());

            if(projetos.isEmpty()){
                exibirAvisoVazio(grid);
            }else{
                for(Projeto projeto : projetos){
                    grid.add(new ProjetoCard(projeto));
                }
            }

        } catch (Exception e) {
                System.out.println("Erro ao carregar projetos: " + e.getMessage());
        }

            grid.repaint();
            grid.revalidate();
    }
    
}