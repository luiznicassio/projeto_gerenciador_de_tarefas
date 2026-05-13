package com.mycompany.projetogerenciadordetarefas.views.components;

import com.mycompany.projetogerenciadordetarefas.models.Projeto;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * @author luiz fernando 
 */
public class ProjetoCard extends JPanel {
    private Projeto projeto;

    // Definição de Cores
    private final Color BG_DARK = new Color(30, 39, 58);
    private final Color BORDER_COLOR = new Color(50, 55, 70);
    private final Color TEXT_PRIMARY = new Color(243, 244, 246);
    private final Color TEXT_SECONDARY = new Color(156, 163, 175);

    public ProjetoCard(Projeto projeto) {
        this.projeto = projeto;
        
        // Configurações do Painel
        setOpaque(false);
        setBackground(BG_DARK);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(360, 240));
        setLayout(new BorderLayout(0, 18));
        setBorder(new EmptyBorder(18, 18, 18, 18));

        // Lógica de Cor do Projeto
        Color corProjeto = new Color(99, 102, 241);
        try {
            if (projeto.getCor() != null && !projeto.getCor().isEmpty()) {
                corProjeto = Color.decode(projeto.getCor());
            }
        } catch (Exception e) {
            System.err.println("Erro ao converter cor: " + e.getMessage());
        }

        /* --- HEADER --- */
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));

        // Ícone
        JPanel iconPanel = createIconPanel(corProjeto, projeto.getTitulo());
        Dimension iconSize = new Dimension(52, 52);
        iconPanel.setPreferredSize(iconSize);
        iconPanel.setMinimumSize(iconSize);
        iconPanel.setMaximumSize(iconSize);
        iconPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        // Título e Tag
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel titulo = new JLabel("<html><body style='width:190px'><b>" + projeto.getTitulo() + "</b></body></html>");
        titulo.setForeground(TEXT_PRIMARY);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel tag = createTag(projeto.getCargo(), corProjeto);
        tag.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titulo);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(tag);

        // Menu (Dots)
        JLabel menuDots = new JLabel("\u2807");
        menuDots.setForeground(TEXT_SECONDARY);
        menuDots.setFont(new Font("Segoe UI", Font.BOLD, 20));
        menuDots.setAlignmentY(Component.TOP_ALIGNMENT);

        headerPanel.add(iconPanel);
        headerPanel.add(Box.createHorizontalStrut(14));
        headerPanel.add(titlePanel);
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(menuDots);

        /* --- BODY --- */
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setOpaque(false);

        String descricaoTexto = (projeto.getDescricao() == null || projeto.getDescricao().isBlank()) 
                                ? "Sem descrição." : projeto.getDescricao();

        String hexColor = String.format("#%02x%02x%02x", TEXT_SECONDARY.getRed(), TEXT_SECONDARY.getGreen(), TEXT_SECONDARY.getBlue());
        JLabel descricao = new JLabel("<html><body style='width:290px; color:" + hexColor + "; line-height: 1.5'>" + descricaoTexto + "</body></html>");
        descricao.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bodyPanel.add(descricao, BorderLayout.NORTH);

        /* --- FOOTER (Progresso) --- */
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);

        JLabel lblProgresso = new JLabel("Progresso");
        lblProgresso.setForeground(TEXT_SECONDARY);
        lblProgresso.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblPorcentagem = new JLabel("65%");
        lblPorcentagem.setForeground(TEXT_PRIMARY);
        lblPorcentagem.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(65);
        progressBar.setPreferredSize(new Dimension(170, 7));
        progressBar.setForeground(corProjeto);
        progressBar.setBackground(new Color(20, 25, 35));
        progressBar.setBorderPainted(false);

        JPanel progressContainer = new JPanel(new BorderLayout(12, 0));
        progressContainer.setOpaque(false);
        progressContainer.add(lblProgresso, BorderLayout.WEST);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(progressBar);

        progressContainer.add(center, BorderLayout.CENTER);
        progressContainer.add(lblPorcentagem, BorderLayout.EAST);
        footerPanel.add(progressContainer, BorderLayout.NORTH);

        // Adicionando seções ao card
        add(headerPanel, BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Movido para o início para limpar o fundo corretamente
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fundo Arredondado
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 22, 22));

        // Borda Suave
        g2.setColor(BORDER_COLOR);
        g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, getHeight() - 1, 22, 22));
        g2.dispose();
    }

    private JPanel createIconPanel(Color cor, String titulo) {
        JPanel icon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 22));

                String inicial = (titulo != null && !titulo.isEmpty()) ? titulo.substring(0, 1).toUpperCase() : "P";
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(inicial)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

                g2.drawString(inicial, x, y);
                g2.dispose();
            }
        };
        Dimension size = new Dimension(50, 50);
        icon.setPreferredSize(size);
        icon.setMinimumSize(size);
        icon.setMaximumSize(size);
        icon.setOpaque(false);
        return icon;
    }

    private JPanel createTag(String texto, Color baseColor) {
        JPanel tag = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 40);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };

        tag.setOpaque(false);
        tag.setBorder(new EmptyBorder(4, 12, 4, 12));

        JLabel label = new JLabel(texto != null ? texto : "Sem Cargo");
        label.setForeground(baseColor.brighter());
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.add(label, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setOpaque(false);
        wrapper.add(tag);
        return wrapper;
    }

    public Projeto getProjeto() { return projeto; }
}