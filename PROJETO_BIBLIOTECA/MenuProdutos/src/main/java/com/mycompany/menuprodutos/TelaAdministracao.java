/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.regex.Pattern;
import novoPack.BancoDeDados;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class TelaAdministracao extends JFrame {

    private JTabbedPane tabbedPane;
    private JTable tabelaUsuarios, tabelaPedidos, tabelaItensPedido, tabelaPerfilUsuario, tabelaProdutos;
    private JTextField campoPesquisaUsuarios, campoPesquisaPedidos, campoPesquisaItens, campoPesquisaPerfil, campoPesquisaProdutos;
    private JButton btnAtualizar, btnEditarPedido, btnSalvarPedido, btnCancelarEdicao, btnAnalise; 
     
    public TelaAdministracao() {
        setTitle("Area Administrativa - Livraria Entre Palavras");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
       
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Abre maximizado
        
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("src/assets/images/Logo.png");
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            setIconImage(icon.getImage());
        }

       
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(249, 237, 202));
        painelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

       
        painelPrincipal.add(criarHeaderPanel(), BorderLayout.NORTH);

      
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(249, 237, 202));
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        
        tabbedPane.addTab("Usuarios", criarPainelUsuarios());
        tabbedPane.addTab("Pedidos", criarPainelPedidos());
        tabbedPane.addTab("Itens dos Pedidos", criarPainelItensPedido());
        tabbedPane.addTab("Perfis de Usuario", criarPainelPerfilUsuario());
        tabbedPane.addTab("Produtos", criarPainelProdutos());

        painelPrincipal.add(tabbedPane, BorderLayout.CENTER);

       
        painelPrincipal.add(criarFooterPanel(), BorderLayout.SOUTH);

        add(painelPrincipal);
        
       
        BancoDeDados.verificarEstruturaBanco();
        carregarDados();
        
       
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setExtendedState(JFrame.NORMAL);
                }
            }
        });
        setFocusable(true);
    }

    private JPanel criarHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(new Color(115, 103, 47));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

       
        ImageIcon logoIcon = new ImageIcon("src/assets/images/Logo.png");
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image logoImage = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
            headerPanel.add(logoLabel, BorderLayout.WEST);
        }

        
        JPanel tituloPanel = new JPanel(new BorderLayout());
        tituloPanel.setBackground(new Color(115, 103, 47));

        JLabel titulo = new JLabel("Area Administrativa");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitulo = new JLabel("Gerenciamento do Sistema");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(200, 200, 200));
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        tituloPanel.add(titulo, BorderLayout.CENTER);
        tituloPanel.add(subtitulo, BorderLayout.SOUTH);

        headerPanel.add(tituloPanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel criarPainelUsuarios() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(249, 237, 202));
        painel.setBorder(new EmptyBorder(15, 15, 15, 15));

        
        JPanel cabecalho = new JPanel(new BorderLayout(10, 10));
        cabecalho.setBackground(new Color(249, 237, 202));

        JLabel titulo = new JLabel("Usuarios Cadastrados no Sistema");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(89, 55, 30));

        
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelPesquisa.setBackground(new Color(249, 237, 202));

        JLabel labelPesquisa = new JLabel("Pesquisar:");
        labelPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelPesquisa.setForeground(new Color(89, 55, 30));

        campoPesquisaUsuarios = new JTextField(20);
        campoPesquisaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campoPesquisaUsuarios.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 120, 80), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setBackground(new Color(184, 134, 11));
        btnPesquisar.setForeground(Color.WHITE);
        btnPesquisar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPesquisar.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 110, 0), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnPesquisar.addActionListener(e -> pesquisarUsuarios());

        JButton btnLimparPesquisa = new JButton("Limpar");
        btnLimparPesquisa.setBackground(new Color(160, 120, 80));
        btnLimparPesquisa.setForeground(Color.WHITE);
        btnLimparPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimparPesquisa.setBorder(new CompoundBorder(
            new LineBorder(new Color(140, 100, 60), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnLimparPesquisa.addActionListener(e -> {
            campoPesquisaUsuarios.setText("");
            pesquisarUsuarios();
        });

        painelPesquisa.add(labelPesquisa);
        painelPesquisa.add(campoPesquisaUsuarios);
        painelPesquisa.add(btnPesquisar);
        painelPesquisa.add(btnLimparPesquisa);

        cabecalho.add(titulo, BorderLayout.WEST);
        cabecalho.add(painelPesquisa, BorderLayout.EAST);

        String[] colunasUsuarios = {"ID", "Nome", "Email", "Telefone", "Tipo", "Data Cadastro"};
        DefaultTableModel modelUsuarios = new DefaultTableModel(colunasUsuarios, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        tabelaUsuarios = new JTable(modelUsuarios);
        estilizarTabela(tabelaUsuarios);
        JScrollPane scrollUsuarios = new JScrollPane(tabelaUsuarios);
        scrollUsuarios.setBorder(new LineBorder(new Color(160, 120, 80), 1));

        painel.add(cabecalho, BorderLayout.NORTH);
        painel.add(scrollUsuarios, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelPedidos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(249, 237, 202));
        painel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel cabecalho = new JPanel(new BorderLayout(10, 10));
        cabecalho.setBackground(new Color(249, 237, 202));

        JLabel titulo = new JLabel("Todos os Pedidos Realizados");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(89, 55, 30));

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelPesquisa.setBackground(new Color(249, 237, 202));

        JLabel labelPesquisa = new JLabel("Pesquisar:");
        labelPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelPesquisa.setForeground(new Color(89, 55, 30));

        campoPesquisaPedidos = new JTextField(20);
        campoPesquisaPedidos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campoPesquisaPedidos.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 120, 80), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setBackground(new Color(184, 134, 11));
        btnPesquisar.setForeground(Color.WHITE);
        btnPesquisar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPesquisar.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 110, 0), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnPesquisar.addActionListener(e -> pesquisarPedidos());

        JButton btnLimparPesquisa = new JButton("Limpar");
        btnLimparPesquisa.setBackground(new Color(160, 120, 80));
        btnLimparPesquisa.setForeground(Color.WHITE);
        btnLimparPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimparPesquisa.setBorder(new CompoundBorder(
            new LineBorder(new Color(140, 100, 60), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnLimparPesquisa.addActionListener(e -> {
            campoPesquisaPedidos.setText("");
            pesquisarPedidos();
        });

        painelPesquisa.add(labelPesquisa);
        painelPesquisa.add(campoPesquisaPedidos);
        painelPesquisa.add(btnPesquisar);
        painelPesquisa.add(btnLimparPesquisa);

        cabecalho.add(titulo, BorderLayout.WEST);
        cabecalho.add(painelPesquisa, BorderLayout.EAST);

        String[] colunasPedidos = {"Codigo", "Cliente", "Email", "Total", "Status", "Data Pedido"};
        DefaultTableModel modelPedidos = new DefaultTableModel(colunasPedidos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                
                return column == 4;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) { 
                    return String.class;
                }
                return Object.class;
            }
        };

        tabelaPedidos = new JTable(modelPedidos);
        estilizarTabela(tabelaPedidos);
        
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{
            "Pendente", "Pago", "Preparando", "Enviado", "Entregue", "Cancelado"
        });
        tabelaPedidos.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusComboBox));
        
        JScrollPane scrollPedidos = new JScrollPane(tabelaPedidos);
        scrollPedidos.setBorder(new LineBorder(new Color(160, 120, 80), 1));

        // Painel de botões para CRUD
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelBotoes.setBackground(new Color(249, 237, 202));
        
        btnEditarPedido = new JButton("Editar Status");
        btnEditarPedido.setBackground(new Color(184, 134, 11));
        btnEditarPedido.setForeground(Color.WHITE);
        btnEditarPedido.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditarPedido.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 110, 0), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnEditarPedido.addActionListener(e -> habilitarEdicaoPedidos());
        
        btnSalvarPedido = new JButton("Salvar Alteracoes");
        btnSalvarPedido.setBackground(new Color(34, 139, 34));
        btnSalvarPedido.setForeground(Color.WHITE);
        btnSalvarPedido.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvarPedido.setBorder(new CompoundBorder(
            new LineBorder(new Color(30, 120, 30), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnSalvarPedido.addActionListener(e -> salvarAlteracoesPedidos());
        btnSalvarPedido.setEnabled(false);
        
        btnCancelarEdicao = new JButton("Cancelar");
        btnCancelarEdicao.setBackground(new Color(205, 92, 92));
        btnCancelarEdicao.setForeground(Color.WHITE);
        btnCancelarEdicao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelarEdicao.setBorder(new CompoundBorder(
            new LineBorder(new Color(180, 70, 70), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnCancelarEdicao.addActionListener(e -> cancelarEdicaoPedidos());
        btnCancelarEdicao.setEnabled(false);
        
        painelBotoes.add(btnEditarPedido);
        painelBotoes.add(btnSalvarPedido);
        painelBotoes.add(btnCancelarEdicao);

        painel.add(cabecalho, BorderLayout.NORTH);
        painel.add(scrollPedidos, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelItensPedido() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(249, 237, 202));
        painel.setBorder(new EmptyBorder(15, 15, 15, 15));

      
        JPanel cabecalho = new JPanel(new BorderLayout(10, 10));
        cabecalho.setBackground(new Color(249, 237, 202));

        JLabel titulo = new JLabel("Itens de Todos os Pedidos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(89, 55, 30));

       
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelPesquisa.setBackground(new Color(249, 237, 202));

        JLabel labelPesquisa = new JLabel("Pesquisar:");
        labelPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelPesquisa.setForeground(new Color(89, 55, 30));

        campoPesquisaItens = new JTextField(20);
        campoPesquisaItens.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campoPesquisaItens.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 120, 80), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setBackground(new Color(184, 134, 11));
        btnPesquisar.setForeground(Color.WHITE);
        btnPesquisar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPesquisar.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 110, 0), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnPesquisar.addActionListener(e -> pesquisarItens());

        JButton btnLimparPesquisa = new JButton("Limpar");
        btnLimparPesquisa.setBackground(new Color(160, 120, 80));
        btnLimparPesquisa.setForeground(Color.WHITE);
        btnLimparPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimparPesquisa.setBorder(new CompoundBorder(
            new LineBorder(new Color(140, 100, 60), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnLimparPesquisa.addActionListener(e -> {
            campoPesquisaItens.setText("");
            pesquisarItens();
        });

        painelPesquisa.add(labelPesquisa);
        painelPesquisa.add(campoPesquisaItens);
        painelPesquisa.add(btnPesquisar);
        painelPesquisa.add(btnLimparPesquisa);

        cabecalho.add(titulo, BorderLayout.WEST);
        cabecalho.add(painelPesquisa, BorderLayout.EAST);

       
        String[] colunasItens = {"Codigo Pedido", "Produto", "Quantidade", "Preco Unitario", "Preco Total"};
        DefaultTableModel modelItens = new DefaultTableModel(colunasItens, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Apenas visualização
            }
        };

        tabelaItensPedido = new JTable(modelItens);
        estilizarTabela(tabelaItensPedido);
        JScrollPane scrollItens = new JScrollPane(tabelaItensPedido);
        scrollItens.setBorder(new LineBorder(new Color(160, 120, 80), 1));

        painel.add(cabecalho, BorderLayout.NORTH);
        painel.add(scrollItens, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelPerfilUsuario() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(249, 237, 202));
        painel.setBorder(new EmptyBorder(15, 15, 15, 15));

      
        JPanel cabecalho = new JPanel(new BorderLayout(10, 10));
        cabecalho.setBackground(new Color(249, 237, 202));

        JLabel titulo = new JLabel("Perfis de Usuario");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(89, 55, 30));

       
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelPesquisa.setBackground(new Color(249, 237, 202));

        JLabel labelPesquisa = new JLabel("Pesquisar:");
        labelPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelPesquisa.setForeground(new Color(89, 55, 30));

        campoPesquisaPerfil = new JTextField(20);
        campoPesquisaPerfil.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campoPesquisaPerfil.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 120, 80), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setBackground(new Color(184, 134, 11));
        btnPesquisar.setForeground(Color.WHITE);
        btnPesquisar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPesquisar.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 110, 0), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnPesquisar.addActionListener(e -> pesquisarPerfis());

        JButton btnLimparPesquisa = new JButton("Limpar");
        btnLimparPesquisa.setBackground(new Color(160, 120, 80));
        btnLimparPesquisa.setForeground(Color.WHITE);
        btnLimparPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimparPesquisa.setBorder(new CompoundBorder(
            new LineBorder(new Color(140, 100, 60), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnLimparPesquisa.addActionListener(e -> {
            campoPesquisaPerfil.setText("");
            pesquisarPerfis();
        });

        painelPesquisa.add(labelPesquisa);
        painelPesquisa.add(campoPesquisaPerfil);
        painelPesquisa.add(btnPesquisar);
        painelPesquisa.add(btnLimparPesquisa);

        cabecalho.add(titulo, BorderLayout.WEST);
        cabecalho.add(painelPesquisa, BorderLayout.EAST);

        
        String[] colunasPerfil = {"ID Perfil", "ID Usuario", "Nome do Usuario", "Email", "Tamanho da Imagem (KB)", "Data Atualizacao"};
        DefaultTableModel modelPerfil = new DefaultTableModel(colunasPerfil, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Apenas visualização
            }
        };

        tabelaPerfilUsuario = new JTable(modelPerfil);
        estilizarTabela(tabelaPerfilUsuario);
        JScrollPane scrollPerfil = new JScrollPane(tabelaPerfilUsuario);
        scrollPerfil.setBorder(new LineBorder(new Color(160, 120, 80), 1));

     
        JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelInfo.setBackground(new Color(249, 237, 202));
        
        JLabel infoLabel = new JLabel("Informacao: Esta tabela mostra os perfis de usuario com imagens cadastradas.");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(new Color(89, 55, 30));
        painelInfo.add(infoLabel);

        painel.add(cabecalho, BorderLayout.NORTH);
        painel.add(scrollPerfil, BorderLayout.CENTER);
        painel.add(painelInfo, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelProdutos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(249, 237, 202));
        painel.setBorder(new EmptyBorder(15, 15, 15, 15));

       
        JPanel cabecalho = new JPanel(new BorderLayout(10, 10));
        cabecalho.setBackground(new Color(249, 237, 202));

        JLabel titulo = new JLabel("Gerenciamento de Produtos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(89, 55, 30));

   
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelPesquisa.setBackground(new Color(249, 237, 202));

        JLabel labelPesquisa = new JLabel("Pesquisar:");
        labelPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelPesquisa.setForeground(new Color(89, 55, 30));

        campoPesquisaProdutos = new JTextField(20);
        campoPesquisaProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campoPesquisaProdutos.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 120, 80), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setBackground(new Color(184, 134, 11));
        btnPesquisar.setForeground(Color.WHITE);
        btnPesquisar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPesquisar.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 110, 0), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnPesquisar.addActionListener(e -> pesquisarProdutos());

        JButton btnLimparPesquisa = new JButton("Limpar");
        btnLimparPesquisa.setBackground(new Color(160, 120, 80));
        btnLimparPesquisa.setForeground(Color.WHITE);
        btnLimparPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimparPesquisa.setBorder(new CompoundBorder(
            new LineBorder(new Color(140, 100, 60), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnLimparPesquisa.addActionListener(e -> {
            campoPesquisaProdutos.setText("");
            pesquisarProdutos();
        });

        painelPesquisa.add(labelPesquisa);
        painelPesquisa.add(campoPesquisaProdutos);
        painelPesquisa.add(btnPesquisar);
        painelPesquisa.add(btnLimparPesquisa);

        cabecalho.add(titulo, BorderLayout.WEST);
        cabecalho.add(painelPesquisa, BorderLayout.EAST);

      
        String[] colunasProdutos = {"ID", "Descricao", "Genero", "Estoque", "Preco", "Tipo", "Categoria"};
        DefaultTableModel modelProdutos = new DefaultTableModel(colunasProdutos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
              
                return column == 1 || column == 2 || column == 3 || column == 4;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) { 
                    return Integer.class;
                }
                if (columnIndex == 4) { 
                    return Double.class;
                }
                return String.class;
            }
        };

        tabelaProdutos = new JTable(modelProdutos);
        estilizarTabela(tabelaProdutos);
        JScrollPane scrollProdutos = new JScrollPane(tabelaProdutos);
        scrollProdutos.setBorder(new LineBorder(new Color(160, 120, 80), 1));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelBotoes.setBackground(new Color(249, 237, 202));
        
        JButton btnEditarProduto = new JButton("Editar Produto");
        btnEditarProduto.setBackground(new Color(184, 134, 11));
        btnEditarProduto.setForeground(Color.WHITE);
        btnEditarProduto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditarProduto.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 110, 0), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnEditarProduto.addActionListener(e -> habilitarEdicaoProdutos());
        
        JButton btnSalvarProduto = new JButton("Salvar Alteracoes");
        btnSalvarProduto.setBackground(new Color(34, 139, 34));
        btnSalvarProduto.setForeground(Color.WHITE);
        btnSalvarProduto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvarProduto.setBorder(new CompoundBorder(
            new LineBorder(new Color(30, 120, 30), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnSalvarProduto.addActionListener(e -> salvarAlteracoesProdutos());
        
        JButton btnExcluirProduto = new JButton("Excluir Produto");
        btnExcluirProduto.setBackground(new Color(205, 92, 92));
        btnExcluirProduto.setForeground(Color.WHITE);
        btnExcluirProduto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluirProduto.setBorder(new CompoundBorder(
            new LineBorder(new Color(180, 70, 70), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnExcluirProduto.addActionListener(e -> excluirProdutoSelecionado());
        
        JButton btnAtualizarLista = new JButton("Atualizar Lista");
        btnAtualizarLista.setBackground(new Color(30, 144, 255));
        btnAtualizarLista.setForeground(Color.WHITE);
        btnAtualizarLista.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAtualizarLista.setBorder(new CompoundBorder(
            new LineBorder(new Color(25, 120, 220), 1),
            new EmptyBorder(5, 15, 5, 15)
        ));
        btnAtualizarLista.addActionListener(e -> carregarProdutos());

        painelBotoes.add(btnEditarProduto);
        painelBotoes.add(btnSalvarProduto);
        painelBotoes.add(btnExcluirProduto);
        painelBotoes.add(btnAtualizarLista);

        painel.add(cabecalho, BorderLayout.NORTH);
        painel.add(scrollProdutos, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footerPanel.setBackground(new Color(249, 237, 202));
        footerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        btnAtualizar = new JButton("Atualizar Dados");
        btnAtualizar.setBackground(new Color(34, 139, 34));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAtualizar.setBorder(new CompoundBorder(
            new LineBorder(new Color(30, 120, 30), 2),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btnAtualizar.addActionListener(e -> carregarDados());

        JButton btnCadastrarProduto = new JButton("Cadastrar Produto");
        btnCadastrarProduto.setBackground(new Color(75, 0, 130));
        btnCadastrarProduto.setForeground(Color.WHITE);
        btnCadastrarProduto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCadastrarProduto.setBorder(new CompoundBorder(
            new LineBorder(new Color(60, 0, 100), 2),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btnCadastrarProduto.addActionListener(e -> {
            new CadastrarProduto().setVisible(true);
        });

        btnAnalise = new JButton("Ver Analise");
        btnAnalise.setBackground(new Color(184, 134, 11));
        btnAnalise.setForeground(Color.WHITE);
        btnAnalise.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAnalise.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 110, 0), 2),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btnAnalise.addActionListener(e -> mostrarAnalise());

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(205, 92, 92));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSair.setBorder(new CompoundBorder(
            new LineBorder(new Color(180, 70, 70), 2),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btnSair.addActionListener(e -> dispose());

        footerPanel.add(btnAtualizar);
        footerPanel.add(btnCadastrarProduto); 
        footerPanel.add(btnAnalise); 
        footerPanel.add(btnSair);

        return footerPanel;
    }
    
    private void pesquisarPerfis() {
        String pesquisa = campoPesquisaPerfil.getText().trim();
        if (pesquisa.isEmpty()) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabelaPerfilUsuario.getModel());
            tabelaPerfilUsuario.setRowSorter(sorter);
            sorter.setRowFilter(null);
            return;
        }

        try {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabelaPerfilUsuario.getModel());
            tabelaPerfilUsuario.setRowSorter(sorter);
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + Pattern.quote(pesquisa));
            sorter.setRowFilter(filter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na pesquisa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void estilizarTabela(JTable tabela) {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.getTableHeader().setBackground(new Color(160, 120, 80));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.setSelectionBackground(new Color(184, 134, 11));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(new Color(200, 200, 200));
        tabela.setShowGrid(true);
    }

    private void carregarDados() {
        carregarUsuarios();
        carregarPedidos();
        carregarItensPedido();
        carregarPerfisUsuario();
        carregarProdutos();
        JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!", "Atualizacao", JOptionPane.INFORMATION_MESSAGE);
    }

    private void carregarUsuarios() {
        DefaultTableModel model = (DefaultTableModel) tabelaUsuarios.getModel();
        model.setRowCount(0);

        try {
            ResultSet rs = BancoDeDados.getTodosUsuarios();
            if (rs != null) {
                int count = 0;
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("id"),
                        rs.getString("nome_completo"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("tipo_usuario"),
                        rs.getTimestamp("data_cadastro")
                    };
                    model.addRow(row);
                    count++;
                }
                System.out.println("Usuarios carregados: " + count);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Nao foi possivel carregar os usuarios.\nResultSet retornou nulo.", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar usuarios: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void carregarPedidos() {
        DefaultTableModel model = (DefaultTableModel) tabelaPedidos.getModel();
        model.setRowCount(0);

        try {
            ResultSet rs = BancoDeDados.getTodosPedidos();
            if (rs != null) {
                int count = 0;
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("codigo_pedido"),
                        rs.getString("nome_cliente"),
                        rs.getString("email"),
                        String.format("R$ %.2f", rs.getDouble("preco_total")),
                        rs.getString("status"),
                        rs.getTimestamp("data_pedido")
                    };
                    model.addRow(row);
                    count++;
                }
                System.out.println("Pedidos carregados: " + count);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar pedidos: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void carregarItensPedido() {
        DefaultTableModel model = (DefaultTableModel) tabelaItensPedido.getModel();
        model.setRowCount(0);

        try {
            ResultSet rs = BancoDeDados.getTodosItensPedido();
            if (rs != null) {
                int count = 0;
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("codigo_pedido"),
                        rs.getString("nome_produto"),
                        rs.getInt("quantidade"),
                        String.format("R$ %.2f", rs.getDouble("preco_unitario")),
                        String.format("R$ %.2f", rs.getDouble("preco_total"))
                    };
                    model.addRow(row);
                    count++;
                }
                System.out.println("Itens carregados: " + count);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar itens: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void carregarPerfisUsuario() {
        DefaultTableModel model = (DefaultTableModel) tabelaPerfilUsuario.getModel();
        model.setRowCount(0);

        try {
            ResultSet rs = BancoDeDados.getTodosPerfisUsuario();
            if (rs != null) {
                int count = 0;
                while (rs.next()) {
                    
                    byte[] imagemBytes = rs.getBytes("imagem_perfil");
                    String tamanhoImagem = "N/A";
                    if (imagemBytes != null) {
                        double tamanhoKB = imagemBytes.length / 1024.0;
                        tamanhoImagem = String.format("%.2f KB", tamanhoKB);
                    }

                    Object[] row = {
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getString("nome_completo"),
                        rs.getString("email"),
                        tamanhoImagem,
                        rs.getTimestamp("data_atualizacao")
                    };
                    model.addRow(row);
                    count++;
                }
                System.out.println("Perfis de usuario carregados: " + count);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Nao foi possivel carregar os perfis de usuario.\nResultSet retornou nulo.", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar perfis de usuario: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void carregarProdutos() {
        DefaultTableModel model = (DefaultTableModel) tabelaProdutos.getModel();
        model.setRowCount(0);

        try {
           
            java.util.List<Produto> produtos = BancoDeDados.carregarProdutosDoBanco();
            
            if (produtos != null && !produtos.isEmpty()) {
                for (Produto produto : produtos) {
                    Object[] row = {
                        produto.getId(),
                        produto.getDescricao(),
                        produto.getGenero(),
                        produto.getEstoque(),
                        produto.getPreco(),
                        produto.getTipoProduto(),
                        produto.getCategoria()
                    };
                    model.addRow(row);
                }
                System.out.println("Produtos carregados: " + produtos.size());
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Nenhum produto encontrado no banco de dados.", 
                    "Informacao", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar produtos: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void habilitarEdicaoPedidos() {
      
        DefaultTableModel model = (DefaultTableModel) tabelaPedidos.getModel();
        
      
        for (int i = 0; i < model.getRowCount(); i++) {
            tabelaPedidos.setValueAt(model.getValueAt(i, 4), i, 4); 
        }
        
        btnEditarPedido.setEnabled(false);
        btnSalvarPedido.setEnabled(true);
        btnCancelarEdicao.setEnabled(true);
        
        JOptionPane.showMessageDialog(this, 
            "Modo de edicao ativado!\nClique na celula de Status para alterar o status do pedido.",
            "Edicao Ativada", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void salvarAlteracoesPedidos() {
        DefaultTableModel model = (DefaultTableModel) tabelaPedidos.getModel();
        int alteracoes = 0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String codigoPedido = (String) model.getValueAt(i, 0);
            String novoStatus = (String) model.getValueAt(i, 4);
            
          
            if (novoStatus != null && !novoStatus.isEmpty()) {
                boolean sucesso = BancoDeDados.atualizarStatusPedido(codigoPedido, novoStatus);
                if (sucesso) {
                    alteracoes++;
                    System.out.println("Status do pedido " + codigoPedido + " atualizado para: " + novoStatus);
                }
            }
        }
        
       
        btnEditarPedido.setEnabled(true);
        btnSalvarPedido.setEnabled(false);
        btnCancelarEdicao.setEnabled(false);
        
        if (alteracoes > 0) {
            JOptionPane.showMessageDialog(this, 
                alteracoes + " pedido(s) atualizado(s) com sucesso!",
                "Alteracoes Salvas", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Nenhuma alteracao foi realizada.",
                "Sem Alteracoes", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cancelarEdicaoPedidos() {
   
        carregarPedidos();
        
        
        btnEditarPedido.setEnabled(true);
        btnSalvarPedido.setEnabled(false);
        btnCancelarEdicao.setEnabled(false);
        
        JOptionPane.showMessageDialog(this, 
            "Edicao cancelada. Alteracoes nao foram salvas.",
            "Edicao Cancelada", 
            JOptionPane.INFORMATION_MESSAGE);
    }

   
    private void habilitarEdicaoProdutos() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um produto para editar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        tabelaProdutos.editCellAt(selectedRow, 1);
        JOptionPane.showMessageDialog(this, 
            "Modo de edicao ativado!\nClique duas vezes na celula que deseja editar.",
            "Edicao Ativada", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void salvarAlteracoesProdutos() {
        DefaultTableModel model = (DefaultTableModel) tabelaProdutos.getModel();
        int alteracoes = 0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                int id = (Integer) model.getValueAt(i, 0);
                String descricao = (String) model.getValueAt(i, 1);
                String genero = (String) model.getValueAt(i, 2);
                int estoque = (Integer) model.getValueAt(i, 3);
                double preco = (Double) model.getValueAt(i, 4);
                
                boolean sucesso = atualizarProdutoNoBanco(id, descricao, genero, estoque, preco);
                if (sucesso) {
                    alteracoes++;
                }
            } catch (Exception e) {
                System.err.println("Erro ao atualizar produto na linha " + i + ": " + e.getMessage());
            }
        }
        
        if (alteracoes > 0) {
            JOptionPane.showMessageDialog(this, 
                alteracoes + " produto(s) atualizado(s) com sucesso!",
                "Alteracoes Salvas", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Nenhuma alteracao foi realizada.",
                "Sem Alteracoes", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void excluirProdutoSelecionado() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um produto para excluir.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProduto = (Integer) tabelaProdutos.getValueAt(selectedRow, 0);
        String descricao = (String) tabelaProdutos.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja excluir o produto:\n" + descricao + " (ID: " + idProduto + ")?",
            "Confirmar Exclusao",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean sucesso = BancoDeDados.excluirProduto(idProduto);
            if (sucesso) {
                ((DefaultTableModel) tabelaProdutos.getModel()).removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, 
                    "Produto excluido com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir produto.", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean atualizarProdutoNoBanco(int id, String descricao, String genero, int estoque, double preco) {
       
        try {
            
            System.out.println("Atualizando produto ID: " + id + 
                             " - Descricao: " + descricao + 
                             " - Estoque: " + estoque + 
                             " - Preco: " + preco);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            return false;
        }
    }

    private void pesquisarUsuarios() {
        String pesquisa = campoPesquisaUsuarios.getText().trim();
        if (pesquisa.isEmpty()) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabelaUsuarios.getModel());
            tabelaUsuarios.setRowSorter(sorter);
            sorter.setRowFilter(null);
            return;
        }

        try {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabelaUsuarios.getModel());
            tabelaUsuarios.setRowSorter(sorter);
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + Pattern.quote(pesquisa));
            sorter.setRowFilter(filter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na pesquisa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesquisarPedidos() {
        String pesquisa = campoPesquisaPedidos.getText().trim();
        if (pesquisa.isEmpty()) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabelaPedidos.getModel());
            tabelaPedidos.setRowSorter(sorter);
            sorter.setRowFilter(null);
            return;
        }

        try {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabelaPedidos.getModel());
            tabelaPedidos.setRowSorter(sorter);
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + Pattern.quote(pesquisa));
            sorter.setRowFilter(filter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na pesquisa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesquisarItens() {
        String pesquisa = campoPesquisaItens.getText().trim();
        if (pesquisa.isEmpty()) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabelaItensPedido.getModel());
            tabelaItensPedido.setRowSorter(sorter);
            sorter.setRowFilter(null);
            return;
        }

        try {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabelaItensPedido.getModel());
            tabelaItensPedido.setRowSorter(sorter);
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + Pattern.quote(pesquisa));
            sorter.setRowFilter(filter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na pesquisa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

        private void pesquisarProdutos() {
        String pesquisa = campoPesquisaProdutos.getText().trim();
        if (pesquisa.isEmpty()) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabelaProdutos.getModel());
            tabelaProdutos.setRowSorter(sorter);
            sorter.setRowFilter(null);
            return;
        }

        try {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabelaProdutos.getModel());
            tabelaProdutos.setRowSorter(sorter);
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + Pattern.quote(pesquisa));
            sorter.setRowFilter(filter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na pesquisa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarAnalise() {
        JDialog dialogAnalise = new JDialog(this, "Análise de Dados - Livraria Entre Palavras", true);
        dialogAnalise.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogAnalise.setSize(1200, 800);
        dialogAnalise.setLocationRelativeTo(this);
        dialogAnalise.setLayout(new BorderLayout());

        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(new Color(249, 237, 202));
        painelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Cabeçalho
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(new Color(115, 103, 47));
        cabecalho.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("Análise de Dados");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitulo = new JLabel("Estatísticas e Métricas do Sistema");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(200, 200, 200));
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        cabecalho.add(titulo, BorderLayout.CENTER);
        cabecalho.add(subtitulo, BorderLayout.SOUTH);

       
        JTabbedPane abasAnalise = new JTabbedPane();
        abasAnalise.setBackground(new Color(249, 237, 202));
        abasAnalise.setFont(new Font("Segoe UI", Font.BOLD, 12));

       
        abasAnalise.addTab("Visão Geral", criarAbaVisaoGeral());
        abasAnalise.addTab("Pedidos", criarAbaPedidos());

        painelPrincipal.add(cabecalho, BorderLayout.NORTH);
        painelPrincipal.add(abasAnalise, BorderLayout.CENTER);

        
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelRodape.setBackground(new Color(249, 237, 202));
        
        JButton btnFechar = new JButton("Fechar Dashboard");
        btnFechar.setBackground(new Color(205, 92, 92));
        btnFechar.setForeground(Color.WHITE);
        btnFechar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFechar.addActionListener(e -> dialogAnalise.dispose());
        
        painelRodape.add(btnFechar);

        painelPrincipal.add(painelRodape, BorderLayout.SOUTH);

        dialogAnalise.add(painelPrincipal);
        dialogAnalise.setVisible(true);
    }

    private JPanel criarAbaVisaoGeral() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(249, 237, 202));
        painel.setBorder(new EmptyBorder(15, 15, 15, 15));

        
        Map<String, Object> dadosAnalise = BancoDeDados.obterDadosAnalise();

        
        JPanel painelMetricas = new JPanel(new GridLayout(2, 3, 15, 15));
        painelMetricas.setBackground(new Color(249, 237, 202));

       
        double totalVendas = dadosAnalise.containsKey("totalVendas") ? (Double) dadosAnalise.get("totalVendas") : 0.0;
        int totalPedidos = dadosAnalise.containsKey("totalPedidos") ? (Integer) dadosAnalise.get("totalPedidos") : 0;
        int totalUsuarios = dadosAnalise.containsKey("totalUsuarios") ? (Integer) dadosAnalise.get("totalUsuarios") : 0;
        double ticketMedio = dadosAnalise.containsKey("ticketMedio") ? (Double) dadosAnalise.get("ticketMedio") : 0.0;
        int pedidosEsteMes = dadosAnalise.containsKey("pedidosEsteMes") ? (Integer) dadosAnalise.get("pedidosEsteMes") : 0;
        int totalProdutosVendidos = dadosAnalise.containsKey("totalProdutosVendidos") ? (Integer) dadosAnalise.get("totalProdutosVendidos") : 0;

       
        painelMetricas.add(criarCardMetrica(
            "Total de Vendas", 
            "R$ " + formatarDecimal(totalVendas),
            new Color(34, 139, 34)
        ));

       
        painelMetricas.add(criarCardMetrica(
            "Total de Pedidos", 
            String.valueOf(totalPedidos),
            new Color(184, 134, 11)
        ));

        
        painelMetricas.add(criarCardMetrica(
            "Usuarios Cadastrados", 
            String.valueOf(totalUsuarios),
            new Color(75, 0, 130)
        ));

        
        painelMetricas.add(criarCardMetrica(
            "Ticket Medio", 
            "R$ " + formatarDecimal(ticketMedio),
            new Color(205, 92, 92)
        ));

        
        painelMetricas.add(criarCardMetrica(
            "Pedidos Este Mes", 
            String.valueOf(pedidosEsteMes),
            new Color(30, 144, 255)
        ));

      
        painelMetricas.add(criarCardMetrica(
            "Total de Produtos Vendidos", 
            String.valueOf(totalProdutosVendidos),
            new Color(160, 120, 80)
        ));

       
        JPanel painelGraficos = new JPanel(new GridLayout(1, 2, 15, 15));
        painelGraficos.setBackground(new Color(249, 237, 202));

        // Gráfico de Status dos Pedidos - APENAS DADOS REAIS
        try {
            DefaultPieDataset datasetStatus = new DefaultPieDataset();
            if (dadosAnalise.containsKey("statusPedidos")) {
                Map<String, Integer> statusPedidos = (Map<String, Integer>) dadosAnalise.get("statusPedidos");
                if (!statusPedidos.isEmpty()) {
                    for (Map.Entry<String, Integer> entry : statusPedidos.entrySet()) {
                        datasetStatus.setValue(entry.getKey(), entry.getValue());
                    }
                    
                    JFreeChart chartStatus = ChartFactory.createPieChart(
                        "Status dos Pedidos",
                        datasetStatus,
                        true, true, false
                    );
                    ChartPanel chartPanelStatus = new ChartPanel(chartStatus);
                    chartPanelStatus.setPreferredSize(new Dimension(400, 300));
                    painelGraficos.add(chartPanelStatus);
                } else {
                    painelGraficos.add(new JLabel("Sem dados de status de pedidos", SwingConstants.CENTER));
                }
            } else {
                painelGraficos.add(new JLabel("Sem dados de status de pedidos", SwingConstants.CENTER));
            }
        } catch (Exception e) {
            painelGraficos.add(new JLabel("Erro ao carregar grafico de status", SwingConstants.CENTER));
        }

       
        try {
            DefaultCategoryDataset datasetVendas = new DefaultCategoryDataset();
            if (dadosAnalise.containsKey("vendasPorMes")) {
                Map<String, Double> vendasPorMes = (Map<String, Double>) dadosAnalise.get("vendasPorMes");
                if (!vendasPorMes.isEmpty()) {
                    for (Map.Entry<String, Double> entry : vendasPorMes.entrySet()) {
                        datasetVendas.addValue(entry.getValue(), "Vendas", entry.getKey());
                    }
                    
                    JFreeChart chartVendas = ChartFactory.createBarChart(
                        "Vendas por Mes (R$)",
                        "Mes",
                        "Valor (R$)",
                        datasetVendas
                    );
                    ChartPanel chartPanelVendas = new ChartPanel(chartVendas);
                    chartPanelVendas.setPreferredSize(new Dimension(400, 300));
                    painelGraficos.add(chartPanelVendas);
                } else {
                    painelGraficos.add(new JLabel("Sem dados de vendas por mes", SwingConstants.CENTER));
                }
            } else {
                painelGraficos.add(new JLabel("Sem dados de vendas por mes", SwingConstants.CENTER));
            }
        } catch (Exception e) {
            painelGraficos.add(new JLabel("Erro ao carregar grafico de vendas", SwingConstants.CENTER));
        }

        painel.add(painelMetricas, BorderLayout.NORTH);
        painel.add(painelGraficos, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarAbaPedidos() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(249, 237, 202));
        painel.setBorder(new EmptyBorder(15, 15, 15, 15));

       
        Map<String, Object> statsPedidos = BancoDeDados.obterEstatisticasPedidos();

        JPanel painelStats = new JPanel(new GridLayout(2, 3, 15, 15));
        painelStats.setBackground(new Color(249, 237, 202));

        painelStats.add(criarCardMetrica(
            "Pedidos Entregues",
            String.valueOf(statsPedidos.get("pedidosEntregues")),
            new Color(34, 139, 34)
        ));

        painelStats.add(criarCardMetrica(
            "Pedidos Pendentes",
            String.valueOf(statsPedidos.get("pedidosPendentes")),
            new Color(255, 165, 0)
        ));

        painelStats.add(criarCardMetrica(
            "Pedidos Cancelados",
            String.valueOf(statsPedidos.get("pedidosCancelados")),
            new Color(205, 92, 92)
        ));

        painelStats.add(criarCardMetrica(
            "Maior Pedido",
            "R$ " + formatarDecimal((Double) statsPedidos.get("maiorPedido")),
            new Color(75, 0, 130)
        ));

        painelStats.add(criarCardMetrica(
            "Pedidos Hoje",
            String.valueOf(statsPedidos.get("pedidosHoje")),
            new Color(30, 144, 255)
        ));

        painelStats.add(criarCardMetrica(
            "Taxa de Conclusao",
            formatarDecimal((Double) statsPedidos.get("taxaConclusao")) + "%",
            new Color(160, 120, 80)
        ));

       
        try {
            DefaultCategoryDataset datasetEvolucao = new DefaultCategoryDataset();
            if (statsPedidos.containsKey("evolucaoPedidos")) {
                Map<String, Integer> evolucaoPedidos = (Map<String, Integer>) statsPedidos.get("evolucaoPedidos");
                if (!evolucaoPedidos.isEmpty()) {
                    for (Map.Entry<String, Integer> entry : evolucaoPedidos.entrySet()) {
                        datasetEvolucao.addValue(entry.getValue(), "Pedidos", entry.getKey());
                    }
                    
                    JFreeChart chartEvolucao = ChartFactory.createLineChart(
                        "Evolucao de Pedidos (Ultimos 6 Meses)",
                        "Mes",
                        "Quantidade de Pedidos",
                        datasetEvolucao
                    );
                    ChartPanel chartPanelEvolucao = new ChartPanel(chartEvolucao);
                    chartPanelEvolucao.setPreferredSize(new Dimension(600, 400));

                    painel.add(painelStats, BorderLayout.NORTH);
                    painel.add(chartPanelEvolucao, BorderLayout.CENTER);
                } else {
                    painel.add(painelStats, BorderLayout.NORTH);
                    painel.add(new JLabel("Sem dados de evolucao de pedidos", SwingConstants.CENTER), BorderLayout.CENTER);
                }
            } else {
                painel.add(painelStats, BorderLayout.NORTH);
                painel.add(new JLabel("Sem dados de evolucao de pedidos", SwingConstants.CENTER), BorderLayout.CENTER);
            }
        } catch (Exception e) {
            painel.add(painelStats, BorderLayout.NORTH);
            painel.add(new JLabel("Erro ao carregar grafico de evolucao", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        return painel;
    }

   
    private JPanel criarCardMetrica(String titulo, String valor, Color cor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(cor, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelTitulo.setForeground(new Color(89, 55, 30));

        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(new Font("Segoe UI", Font.BOLD, 20));
        labelValor.setForeground(cor);

        card.add(labelTitulo, BorderLayout.NORTH);
        card.add(labelValor, BorderLayout.CENTER);

        return card;
    }

    private String formatarDecimal(double valor) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(valor);
    }
}