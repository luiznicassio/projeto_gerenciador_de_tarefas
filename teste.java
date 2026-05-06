package com.mycompany.sistemadeestoque.views;

import com.mycompany.sistemadeestoque.dao.ProdutoDAO;
import com.mycompany.sistemadeestoque.models.Produto;
import com.mycompany.sistemadeestoque.models.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HomeView extends JFrame {

    private Usuario usuarioLogado;
    private JPanel painelConteudo;
    private JPanel painelResultadosBusca; // Painel para os cards verticais
    private JScrollPane scrollGaleria;    // Scroll que controla a visibilidade
    private Produto produtoSelecionadoDash = null;

    public HomeView(Usuario usuario) {
        this.usuarioLogado = usuario;

        setTitle("Sistema de Estoque");
        setSize(1200, 750); // Aumentei um pouco a altura para caber a lista
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔹 Sidebar (Navegação)
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(180, getHeight()));
        sidebar.setBackground(new Color(33, 37, 41));
        sidebar.setLayout(new GridLayout(10, 1, 5, 5));

        JButton btnDashboard = criarBotao("Dashboard");
        JButton btnProdutos = criarBotao("Lista de Produtos");
        JButton btnCadastrarProduto = criarBotao("Cadastrar Produto");
        JButton btnSair = criarBotao("Sair");

        sidebar.add(btnDashboard);
        sidebar.add(btnProdutos);
        sidebar.add(btnCadastrarProduto);
        sidebar.add(new JLabel());
        sidebar.add(btnSair);
        add(sidebar, BorderLayout.WEST);

        // 🔹 Topo (Header)
        JPanel topo = new JPanel();
        topo.setPreferredSize(new Dimension(getWidth(), 50));
        topo.setBackground(new Color(52, 58, 64));
        topo.setLayout(new BorderLayout());

        JLabel lblUsuario = new JLabel("Usuário: " + usuarioLogado.getUsuario());
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        topo.add(lblUsuario, BorderLayout.WEST);
        add(topo, BorderLayout.NORTH);

        // 🔹 Conteúdo Principal
        painelConteudo = new JPanel();
        painelConteudo.setLayout(new BorderLayout());
        add(painelConteudo, BorderLayout.CENTER);

        // Eventos
        btnDashboard.addActionListener(e -> mostrarDashboard());
        btnProdutos.addActionListener(e -> mostrarProdutos());
        btnCadastrarProduto.addActionListener(e -> mostrarCadastroProduto());
        btnSair.addActionListener(e -> sair());

        mostrarDashboard();
    }

    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52, 58, 64));
        btn.setForeground(Color.WHITE);
        return btn;
    }

    private void mostrarDashboard() {
        painelConteudo.removeAll();
        ProdutoDAO dao = new ProdutoDAO();

        JPanel mainDash = new JPanel(new BorderLayout(20, 20));
        mainDash.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- 1. CARDS DE INDICADORES (Sua lógica original) ---
        List<Produto> listaTodos = dao.listar();
        int totalProdutos = listaTodos.size();
        int estoqueBaixo = 0;
        double valorTotalSoma = 0;
        for (Produto p : listaTodos) {
            valorTotalSoma += (p.getValor() * p.getQuantidadeEstoque());
            if (p.getQuantidadeEstoque() <= 5) estoqueBaixo++;
        }

        JPanel painelCards = new JPanel(new GridLayout(1, 3, 15, 15));
        painelCards.add(criarCard("Total de Itens", String.valueOf(totalProdutos)));
        JPanel cardBaixo = criarCard("Estoque Baixo (<=5)", String.valueOf(estoqueBaixo));
        if (estoqueBaixo > 0) cardBaixo.setBackground(new Color(255, 235, 238));
        painelCards.add(cardBaixo);
        painelCards.add(criarCard("Valor em Estoque", String.format("R$ %.2f", valorTotalSoma)));

        // --- 2. SEÇÃO DE BUSCA E RESULTADOS VERTICAIS ---
        JPanel painelBuscaEGaleria = new JPanel(new BorderLayout(10, 10));
        
        // Barra de pesquisa
        JPanel barraBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtBuscaNome = new JTextField(25);
        JButton btnPesquisar = new JButton("Buscar Produto");
        barraBusca.add(new JLabel("Pesquisa Rápida:"));
        barraBusca.add(txtBuscaNome);
        barraBusca.add(btnPesquisar);

        // Container de Resultados (BoxLayout Vertical)
        painelResultadosBusca = new JPanel();
        painelResultadosBusca.setLayout(new BoxLayout(painelResultadosBusca, BoxLayout.Y_AXIS));
        
        scrollGaleria = new JScrollPane(painelResultadosBusca);
        scrollGaleria.setBorder(BorderFactory.createTitledBorder("Resultados encontrados"));
        scrollGaleria.setVisible(false); // 🔥 MODIFICAÇÃO: Invisível antes da pesquisa

        btnPesquisar.addActionListener(e -> {
            String busca = txtBuscaNome.getText().trim();
            if (!busca.isEmpty()) {
                List<Produto> encontrados = dao.buscarPorNome(busca);
                scrollGaleria.setVisible(true); // 🔥 MODIFICAÇÃO: Aparece ao pesquisar
                renderizarCardsBusca(encontrados);
            }
        });

        painelBuscaEGaleria.add(barraBusca, BorderLayout.NORTH);
        painelBuscaEGaleria.add(scrollGaleria, BorderLayout.CENTER);

        // Montagem Final
        mainDash.add(painelCards, BorderLayout.NORTH);
        mainDash.add(painelBuscaEGaleria, BorderLayout.CENTER);

        painelConteudo.add(mainDash, BorderLayout.CENTER);
        atualizarTela();
    }

    private void renderizarCardsBusca(List<Produto> produtos) {
        painelResultadosBusca.removeAll();

        if (produtos.isEmpty()) {
            painelResultadosBusca.add(new JLabel("Nenhum produto encontrado."));
        } else {
            for (Produto p : produtos) {
                // Card Horizontal (Um embaixo do outro)
                JPanel linha = new JPanel(new BorderLayout(10, 0));
                linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
                linha.setBackground(Color.WHITE);
                linha.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));

                JLabel lblInfo = new JLabel("<html><b>" + p.getNome() + "</b> | R$ " + p.getValor() + " | Saldo: " + p.getQuantidadeEstoque() + "</html>");
                
                JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                acoes.setOpaque(false);
                JTextField txtQtd = new JTextField("1", 3);
                JButton btnBaixar = new JButton("Baixar");
                btnBaixar.setBackground(new Color(33, 37, 41));
                btnBaixar.setForeground(Color.WHITE);

                btnBaixar.addActionListener(e -> {
                    try {
                        int qtd = Integer.parseInt(txtQtd.getText());
                        if(qtd <= p.getQuantidadeEstoque()) {
                            // dao.baixarEstoque(p.getID(), qtd); // Chame seu método aqui
                            JOptionPane.showMessageDialog(null, "Baixa de " + qtd + " realizada!");
                            mostrarDashboard();
                        } else {
                            JOptionPane.showMessageDialog(null, "Estoque insuficiente!");
                        }
                    } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Quantidade inválida"); }
                });

                acoes.add(new JLabel("Qtd:"));
                acoes.add(txtQtd);
                acoes.add(btnBaixar);

                linha.add(lblInfo, BorderLayout.WEST);
                linha.add(acoes, BorderLayout.EAST);

                painelResultadosBusca.add(linha);
                painelResultadosBusca.add(Box.createRigidArea(new Dimension(0, 2)));
            }
        }
        atualizarTela();
    }

    // Métodos mostrarProdutos, mostrarCadastroProduto e sair continuam os mesmos da sua versão...
    private void mostrarProdutos() { 
         painelConteudo.removeAll();
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {"ID", "Nome", "Tipo", "Valor", "Quantidade"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> lista = dao.listar();

        for (Produto p : lista) {
            Object[] linha = { p.getID(), p.getNome(), p.getTipo(), "R$ " + p.getValor(), p.getQuantidadeEstoque() };
            model.addRow(linha);
        }

        JTable tabela = new JTable(model);
        tabela.setRowHeight(30);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabela);

        JPanel painelInferior = new JPanel(new BorderLayout());
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblSelecionado = new JLabel("Nenhum produto selecionado");
        lblSelecionado.setFont(new Font("SansSerif", Font.ITALIC, 13));

        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(220, 53, 69));
        btnExcluir.setForeground(Color.WHITE);

        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = tabela.getSelectedRow();
                if (linha != -1) {
                    lblSelecionado.setText("Produto Selecionado: " + tabela.getValueAt(linha, 1).toString());
                    lblSelecionado.setForeground(new Color(0, 123, 255));
                } else {
                    lblSelecionado.setText("Nenhum produto selecionado");
                    lblSelecionado.setForeground(Color.GRAY);
                }
            }
        });

        btnEditar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(null, "Selecione um produto!");
                return;
            }
            Produto p = new Produto();
            p.setID((int) tabela.getValueAt(linha, 0));
            p.setNome(tabela.getValueAt(linha, 1).toString());
            p.setTipo(tabela.getValueAt(linha, 2).toString());
            String valorLimpo = tabela.getValueAt(linha, 3).toString().replace("R$ ", "").replace(",", ".");
            p.setValor(Float.parseFloat(valorLimpo));
            p.setQuantidadeEstoque(Integer.parseInt(tabela.getValueAt(linha, 4).toString()));

            EditarProdutoView telaEdit = new EditarProdutoView(p);
            telaEdit.setVisible(true);
            mostrarProdutos();
        });

        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1) return;
            int id = (int) tabela.getValueAt(linha, 0);
            if (JOptionPane.showConfirmDialog(null, "Excluir ID: " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if(dao.apagar(id)) mostrarProdutos();
            }
        });

        painelInferior.add(lblSelecionado, BorderLayout.WEST);
        painelInferior.add(painelBotoes, BorderLayout.EAST);
        container.add(scroll, BorderLayout.CENTER);
        container.add(painelInferior, BorderLayout.SOUTH);
        painelConteudo.add(container, BorderLayout.CENTER);
        atualizarTela();
    }
    private void mostrarCadastroProduto() {
        painelConteudo.removeAll();
        JPanel formWrapper = new JPanel(new GridBagLayout());
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNome = new JTextField(20);
        JTextField txtTipo = new JTextField(20);
        JTextField txtValor = new JTextField(20);
        JTextField txtQuantidade = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; form.add(txtNome, gbc);
        gbc.gridx = 0; gbc.gridy++; form.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; form.add(txtTipo, gbc);
        gbc.gridx = 0; gbc.gridy++; form.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1; form.add(txtValor, gbc);
        gbc.gridx = 0; gbc.gridy++; form.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1; form.add(txtQuantidade, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JButton btnSalvar = new JButton("Salvar Produto");
        btnSalvar.setBackground(new Color(40, 167, 69));
        btnSalvar.setForeground(Color.WHITE);
        form.add(btnSalvar, gbc);

        JLabel lblMsg = new JLabel("", SwingConstants.CENTER);
        gbc.gridy++; form.add(lblMsg, gbc);

        btnSalvar.addActionListener(e -> {
            try {
                Produto p = new Produto();
                p.setNome(txtNome.getText());
                p.setTipo(txtTipo.getText());
                p.setValor(Float.parseFloat(txtValor.getText().trim()));
                p.setQuantidadeEstoque(Integer.parseInt(txtQuantidade.getText().trim()));
                
                new ProdutoDAO().cadastrar(p);
                lblMsg.setText("✅ Cadastrado!");
                txtNome.setText(""); txtTipo.setText(""); txtValor.setText(""); txtQuantidade.setText("");
            } catch (Exception ex) {
                lblMsg.setText("Erro nos dados.");
            }
        });

        formWrapper.add(form);
        painelConteudo.add(formWrapper, BorderLayout.CENTER);
        atualizarTela();

    }

    private void sair() {
        dispose();
        new UsuarioLoginView().setVisible(true);
    }

    private JPanel criarCard(String titulo, String valor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(248, 249, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("Arial", Font.BOLD, 22));
        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    private void atualizarTela() {
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }
}