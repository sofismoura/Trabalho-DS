/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import novoPack.BancoDeDados;

public class CadastrarProduto extends JFrame {
    private JTextField campoDescricao, campoEstoque, campoPreco;
    private JComboBox<String> comboCategoria, comboGenero;
    private JTextArea areaDescricaoDetalhada;
    private JLabel labelImagem;
    private JButton btnSelecionarImagem, btnSalvar;
    private File arquivoImagemSelecionado;
    private byte[] imagemBytes;
    
 
    private JPanel painelCamposLivro, painelCamposDvd, painelCamposCd;
   
    private JTextField campoAutor, campoEditora, campoEdicao;
    private JTextField campoDiretor, campoDuracao, campoCensura;
    private JTextField campoArtista, campoGravadora, campoPaisOrigem;

    // Gêneros pré-definidos
    private final String[] GENEROS_LIVRO = {
        "Finanças", "Fábula", "Religião", "Aventura", "Romance", 
        "Suspense", "Terror", "Clássico", "Drama", "Autoajuda", 
        "Infantil", "Ficção", "Mistério", "Arte", "Fantasia"
    };
    
    private final String[] GENEROS_CD = {
        "Pop", "Rap", "Samba", "Rock", "Hip-Hop", "K-pop", 
        "Alternativo", "MPB", "R&B", "Bachata", "Rock Progressivo"
    };
    
    private final String[] GENEROS_DVD = {
        "Comédia", "Ação", "Drama", "Animação", "Romance", 
        "Biografia", "Terror", "Ficção Científica"
    };

    public CadastrarProduto() {
        setTitle("Cadastro de Produto - Livraria Entre Palavras");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        ImageIcon icon = new ImageIcon("src/assets/images/Logo.png");
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            setIconImage(icon.getImage());
        }

        initComponents();
        carregarListeners();
    }

    private void initComponents() {
        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(new Color(249, 237, 202));
        painelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Cabeçalho
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(new Color(115, 103, 47));
        cabecalho.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("📦 Cadastro de Novo Produto");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        cabecalho.add(titulo, BorderLayout.CENTER);
        painelPrincipal.add(cabecalho, BorderLayout.NORTH);

        // Painel de conteúdo principal
        JPanel painelConteudo = new JPanel(new BorderLayout(10, 10));
        painelConteudo.setBackground(new Color(249, 237, 202));

        // Painel de formulário
        painelConteudo.add(criarPainelFormulario(), BorderLayout.CENTER);
        
        // Painel de imagem
        painelConteudo.add(criarPainelImagem(), BorderLayout.EAST);

        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.setBackground(new Color(249, 237, 202));

        btnSalvar = new JButton("💾 Salvar Produto");
        btnSalvar.setBackground(new Color(34, 139, 34));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setBorder(new CompoundBorder(
            new LineBorder(new Color(30, 120, 30), 2),
            new EmptyBorder(10, 20, 10, 20)
        ));

        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setBackground(new Color(205, 92, 92));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setBorder(new CompoundBorder(
            new LineBorder(new Color(180, 70, 70), 2),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnLimpar = new JButton("🧹 Limpar Campos");
        btnLimpar.setBackground(new Color(184, 134, 11));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLimpar.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 110, 0), 2),
            new EmptyBorder(10, 20, 10, 20)
        ));
        btnLimpar.addActionListener(e -> limparCampos());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnCancelar);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(249, 237, 202));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Painel de campos básicos
        JPanel painelBasico = new JPanel(new GridBagLayout());
        painelBasico.setBackground(new Color(249, 237, 202));
        painelBasico.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(160, 120, 80), 1),
            "Informações Básicas"
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        // Categoria
        gbc.gridx = 0; gbc.gridy = 0;
        painelBasico.add(criarLabel("Categoria:*"), gbc);
        gbc.gridx = 1;
        String[] categorias = {"LIVRO", "DVD", "CD"};
        comboCategoria = new JComboBox<>(categorias);
        comboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        painelBasico.add(comboCategoria, gbc);

        // Descrição
        gbc.gridx = 0; gbc.gridy = 1;
        painelBasico.add(criarLabel("Título/Descrição:*"), gbc);
        gbc.gridx = 1;
        campoDescricao = criarTextField(25);
        painelBasico.add(campoDescricao, gbc);

        // Gênero 
        gbc.gridx = 0; gbc.gridy = 2;
        painelBasico.add(criarLabel("Gênero:*"), gbc);
        gbc.gridx = 1;
        comboGenero = new JComboBox<>(GENEROS_LIVRO); 
        comboGenero.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        painelBasico.add(comboGenero, gbc);

        // Estoque
        gbc.gridx = 0; gbc.gridy = 3;
        painelBasico.add(criarLabel("Estoque:*"), gbc);
        gbc.gridx = 1;
        campoEstoque = criarTextField(25);
        painelBasico.add(campoEstoque, gbc);

        // Preço
        gbc.gridx = 0; gbc.gridy = 4;
        painelBasico.add(criarLabel("Preço (R$):*"), gbc);
        gbc.gridx = 1;
        campoPreco = criarTextField(25);
        painelBasico.add(campoPreco, gbc);

      
        JPanel painelCamposEspecificos = new JPanel(new CardLayout());
        painelCamposEspecificos.setBackground(new Color(249, 237, 202));
        painelCamposEspecificos.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(160, 120, 80), 1),
            "Campos Específicos"
        ));

        // Criar painéis para cada categoria
        painelCamposLivro = criarPainelLivro();
        painelCamposDvd = criarPainelDvd();
        painelCamposCd = criarPainelCd();

        painelCamposEspecificos.add(painelCamposLivro, "LIVRO");
        painelCamposEspecificos.add(painelCamposDvd, "DVD");
        painelCamposEspecificos.add(painelCamposCd, "CD");

        // Descrição detalhada
        JPanel painelDescricao = new JPanel(new BorderLayout(5, 5));
        painelDescricao.setBackground(new Color(249, 237, 202));
        painelDescricao.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(160, 120, 80), 1),
            "Descrição Detalhada"
        ));

        areaDescricaoDetalhada = new JTextArea(4, 30);
        areaDescricaoDetalhada.setLineWrap(true);
        areaDescricaoDetalhada.setWrapStyleWord(true);
        areaDescricaoDetalhada.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        areaDescricaoDetalhada.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 120, 80), 1),
            new EmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane scrollDescricao = new JScrollPane(areaDescricaoDetalhada);

        painelDescricao.add(scrollDescricao, BorderLayout.CENTER);

       
        painel.add(painelBasico, BorderLayout.NORTH);
        painel.add(painelCamposEspecificos, BorderLayout.CENTER);
        painel.add(painelDescricao, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelLivro() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(249, 237, 202));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.weightx = 1.0;

        // Autor
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(criarLabel("Autor:"), gbc);
        gbc.gridx = 1;
        campoAutor = criarTextField(20);
        painel.add(campoAutor, gbc);

        // Editora
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(criarLabel("Editora:"), gbc);
        gbc.gridx = 1;
        campoEditora = criarTextField(20);
        painel.add(campoEditora, gbc);

        // Edição
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(criarLabel("Edição:"), gbc);
        gbc.gridx = 1;
        campoEdicao = criarTextField(20);
        painel.add(campoEdicao, gbc);

        return painel;
    }

    private JPanel criarPainelDvd() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(249, 237, 202));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.weightx = 1.0;

        // Diretor
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(criarLabel("Diretor:"), gbc);
        gbc.gridx = 1;
        campoDiretor = criarTextField(20);
        painel.add(campoDiretor, gbc);

        // Duração
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(criarLabel("Duração:"), gbc);
        gbc.gridx = 1;
        campoDuracao = criarTextField(20);
        painel.add(campoDuracao, gbc);

        // Censura
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(criarLabel("Censura:"), gbc);
        gbc.gridx = 1;
        campoCensura = criarTextField(20);
        painel.add(campoCensura, gbc);

        return painel;
    }

    private JPanel criarPainelCd() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(249, 237, 202));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.weightx = 1.0;

        // Artista
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(criarLabel("Artista:"), gbc);
        gbc.gridx = 1;
        campoArtista = criarTextField(20);
        painel.add(campoArtista, gbc);

        // Gravadora
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(criarLabel("Gravadora:"), gbc);
        gbc.gridx = 1;
        campoGravadora = criarTextField(20);
        painel.add(campoGravadora, gbc);

        // País de Origem
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(criarLabel("País de Origem:"), gbc);
        gbc.gridx = 1;
        campoPaisOrigem = criarTextField(20);
        painel.add(campoPaisOrigem, gbc);

        return painel;
    }

    private JPanel criarPainelImagem() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(new Color(249, 237, 202));
        painel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(160, 120, 80), 1),
            "Imagem do Produto"
        ));
        painel.setPreferredSize(new Dimension(250, 400));

        labelImagem = new JLabel("<html><center>📷<br>Nenhuma imagem selecionada</center></html>", SwingConstants.CENTER);
        labelImagem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelImagem.setVerticalTextPosition(SwingConstants.BOTTOM);
        labelImagem.setHorizontalTextPosition(SwingConstants.CENTER);
        labelImagem.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 120, 80), 2),
            new EmptyBorder(20, 20, 20, 20)
        ));
        labelImagem.setPreferredSize(new Dimension(200, 200));
        labelImagem.setOpaque(true);
        labelImagem.setBackground(Color.WHITE);

        btnSelecionarImagem = new JButton("Selecionar Imagem");
        btnSelecionarImagem.setBackground(new Color(184, 134, 11));
        btnSelecionarImagem.setForeground(Color.WHITE);
        btnSelecionarImagem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSelecionarImagem.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 110, 0), 1),
            new EmptyBorder(8, 15, 8, 15)
        ));

        JButton btnRemoverImagem = new JButton("Remover");
        btnRemoverImagem.setBackground(new Color(205, 92, 92));
        btnRemoverImagem.setForeground(Color.WHITE);
        btnRemoverImagem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRemoverImagem.setBorder(new CompoundBorder(
            new LineBorder(new Color(180, 70, 70), 1),
            new EmptyBorder(8, 15, 8, 15)
        ));
        btnRemoverImagem.addActionListener(e -> removerImagem());

        JPanel painelBotoesImagem = new JPanel(new GridLayout(2, 1, 5, 5));
        painelBotoesImagem.setBackground(new Color(249, 237, 202));
        painelBotoesImagem.add(btnSelecionarImagem);
        painelBotoesImagem.add(btnRemoverImagem);

        painel.add(labelImagem, BorderLayout.CENTER);
        painel.add(painelBotoesImagem, BorderLayout.SOUTH);

        return painel;
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(89, 55, 30));
        return label;
    }

    private JTextField criarTextField(int colunas) {
        JTextField field = new JTextField(colunas);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(160, 120, 80), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private void carregarListeners() {
        // Listener para mudança de categoria - atualiza os gêneros
        comboCategoria.addActionListener(e -> {
            String categoria = (String) comboCategoria.getSelectedItem();
            CardLayout cl = (CardLayout) ((JPanel) painelCamposLivro.getParent()).getLayout();
            cl.show(painelCamposLivro.getParent(), categoria);
            
          
            atualizarGeneros(categoria);
        });

  
        btnSelecionarImagem.addActionListener(e -> selecionarImagem());

       
        btnSalvar.addActionListener(e -> salvarProduto());
    }

    private void atualizarGeneros(String categoria) {
        comboGenero.removeAllItems();
        String[] generos;
        
        switch (categoria) {
            case "LIVRO":
                generos = GENEROS_LIVRO;
                break;
            case "CD":
                generos = GENEROS_CD;
                break;
            case "DVD":
                generos = GENEROS_DVD;
                break;
            default:
                generos = GENEROS_LIVRO;
        }
        
        for (String genero : generos) {
            comboGenero.addItem(genero);
        }
    }

    private void selecionarImagem() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar Imagem do Produto");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imagens (JPG, PNG, GIF, BMP)", "jpg", "jpeg", "png", "gif", "bmp"));
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            arquivoImagemSelecionado = fileChooser.getSelectedFile();
            try {
                // Verificar tamanho da imagem (máximo 2MB)
                long tamanhoArquivo = arquivoImagemSelecionado.length();
                if (tamanhoArquivo > 2 * 1024 * 1024) {
                    JOptionPane.showMessageDialog(this, 
                        "A imagem é muito grande! Tamanho máximo: 2MB",
                        "Imagem Muito Grande", JOptionPane.WARNING_MESSAGE);
                    return;
                }

               
                imagemBytes = Files.readAllBytes(arquivoImagemSelecionado.toPath());
                
              
                ImageIcon icon = new ImageIcon(arquivoImagemSelecionado.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                labelImagem.setIcon(new ImageIcon(img));
                labelImagem.setText("");
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao carregar imagem: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerImagem() {
        arquivoImagemSelecionado = null;
        imagemBytes = null;
        labelImagem.setIcon(null);
        labelImagem.setText("<html><center>📷<br>Nenhuma imagem selecionada</center></html>");
    }

    private void limparCampos() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja limpar todos os campos?",
            "Limpar Campos",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            campoDescricao.setText("");
            campoEstoque.setText("");
            campoPreco.setText("");
            areaDescricaoDetalhada.setText("");
            campoAutor.setText("");
            campoEditora.setText("");
            campoEdicao.setText("");
            campoDiretor.setText("");
            campoDuracao.setText("");
            campoCensura.setText("");
            campoArtista.setText("");
            campoGravadora.setText("");
            campoPaisOrigem.setText("");
            removerImagem();
            comboGenero.setSelectedIndex(0);
        }
    }

 private void salvarProduto() {
    System.out.println("=== INICIANDO PROCESSO DE SALVAMENTO ===");
    
    if (!validarCampos()) {
        System.out.println("❌ Validação falhou");
        return;
    }

    try {
        Produto produto = criarProduto();
        
        if (produto == null) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao criar produto. Verifique os dados.",
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmar salvamento
        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja salvar o produto?\n\n" +
            "Descrição: " + produto.getDescricao() + "\n" +
            "Categoria: " + produto.getCategoria() + "\n" +
            "Gênero: " + produto.getGenero() + "\n" +
            "Preço: R$ " + String.format("%.2f", produto.getPreco()),
            "Confirmar Salvamento",
            JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            System.out.println("Usuário cancelou o salvamento");
            return;
        }

        System.out.println("Chamando BancoDeDados.salvarProduto...");
        
        // Salvar no banco
        boolean sucesso = BancoDeDados.salvarProduto(produto, imagemBytes);
        
        if (sucesso) {
            System.out.println("Produto salvo com sucesso no banco!");
            
          
            if (Menu_Produtos.getInstance() != null) {
                Menu_Produtos.getInstance().atualizarProdutos();
            }
            
            JOptionPane.showMessageDialog(this, 
                "✅ Produto cadastrado com sucesso!\nO produto já está disponível no menu.",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            System.out.println("❌ Falha ao salvar produto no banco");
            JOptionPane.showMessageDialog(this, 
                "❌ Erro ao salvar produto no banco. Verifique os logs.",
                "Erro", JOptionPane.ERROR_MESSAGE);
        }

    } catch (Exception ex) {
        System.err.println("ERRO CRÍTICO ao salvar produto: " + ex.getMessage());
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "❌ Erro ao salvar produto:\n" + ex.getMessage(),
            "Erro", JOptionPane.ERROR_MESSAGE);
    }
}

    private boolean validarCampos() {
       
        if (campoDescricao.getText().trim().isEmpty()) {
            mostrarErroValidacao("Título/Descrição é obrigatória.", campoDescricao);
            return false;
        }
        if (comboGenero.getSelectedItem() == null) {
            mostrarErroValidacao("Gênero é obrigatório.", null);
            return false;
        }
        if (campoEstoque.getText().trim().isEmpty()) {
            mostrarErroValidacao("Estoque é obrigatório.", campoEstoque);
            return false;
        }
        if (campoPreco.getText().trim().isEmpty()) {
            mostrarErroValidacao("Preço é obrigatório.", campoPreco);
            return false;
        }

       
        try {
            int estoque = Integer.parseInt(campoEstoque.getText().trim());
            if (estoque < 0) {
                mostrarErroValidacao("Estoque não pode ser negativo.", campoEstoque);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarErroValidacao("Estoque deve ser um número inteiro válido.", campoEstoque);
            return false;
        }

        try {
            double preco = Double.parseDouble(campoPreco.getText().trim());
            if (preco <= 0) {
                mostrarErroValidacao("Preço deve ser maior que zero.", campoPreco);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarErroValidacao("Preço deve ser um número válido.", campoPreco);
            return false;
        }

       
        String categoria = (String) comboCategoria.getSelectedItem();
        if ("LIVRO".equals(categoria)) {
            if (campoAutor.getText().trim().isEmpty()) {
                mostrarErroValidacao("Autor é obrigatório para livros.", campoAutor);
                return false;
            }
            if (campoEditora.getText().trim().isEmpty()) {
                mostrarErroValidacao("Editora é obrigatória para livros.", campoEditora);
                return false;
            }
        } else if ("DVD".equals(categoria)) {
            if (campoDiretor.getText().trim().isEmpty()) {
                mostrarErroValidacao("Diretor é obrigatório para DVDs.", campoDiretor);
                return false;
            }
            if (campoDuracao.getText().trim().isEmpty()) {
                mostrarErroValidacao("Duração é obrigatória para DVDs.", campoDuracao);
                return false;
            }
        } else if ("CD".equals(categoria)) {
            if (campoArtista.getText().trim().isEmpty()) {
                mostrarErroValidacao("Artista é obrigatório para CDs.", campoArtista);
                return false;
            }
            if (campoGravadora.getText().trim().isEmpty()) {
                mostrarErroValidacao("Gravadora é obrigatória para CDs.", campoGravadora);
                return false;
            }
        }

        return true;
    }
    
 

    private void mostrarErroValidacao(String mensagem, JTextField campo) {
        JOptionPane.showMessageDialog(this, mensagem, "Validação", JOptionPane.WARNING_MESSAGE);
        if (campo != null) {
            campo.requestFocus();
            campo.selectAll();
        }
    }

  private Produto criarProduto() {
    try {
        String categoria = (String) comboCategoria.getSelectedItem();
        String descricao = campoDescricao.getText().trim();
        String genero = (String) comboGenero.getSelectedItem();
        int estoque = Integer.parseInt(campoEstoque.getText().trim());
        double preco = Double.parseDouble(campoPreco.getText().trim());
        String descricaoDetalhada = areaDescricaoDetalhada.getText().trim();
        
        String caminhoImagem = arquivoImagemSelecionado != null ? 
            arquivoImagemSelecionado.getAbsolutePath() : "";

        System.out.println("Criando objeto Produto...");
        System.out.println("   Categoria: " + categoria);
        System.out.println("   Descrição: " + descricao);
        System.out.println("   Gênero: " + genero);
        System.out.println("   Estoque: " + estoque);
        System.out.println("   Preço: " + preco);

      
        switch (categoria) {
            case "LIVRO":
                String autor = campoAutor.getText().trim();
                String editora = campoEditora.getText().trim();
                String edicao = campoEdicao.getText().trim();
                System.out.println("   Autor: " + autor);
                System.out.println("   Editora: " + editora);
                System.out.println("   Edição: " + edicao);
                return new Livro(descricao, genero, estoque, preco,
                              autor, editora, edicao,
                              caminhoImagem, descricaoDetalhada);
            case "DVD":
                String diretor = campoDiretor.getText().trim();
                String duracao = campoDuracao.getText().trim();
                String censura = campoCensura.getText().trim();
                System.out.println("   Diretor: " + diretor);
                System.out.println("   Duração: " + duracao);
                System.out.println("   Censura: " + censura);
                return new Dvd(descricao, genero, estoque, preco,
                             diretor, duracao, censura,
                             caminhoImagem, descricaoDetalhada);
            case "CD":
                String artista = campoArtista.getText().trim();
                String gravadora = campoGravadora.getText().trim();
                String paisOrigem = campoPaisOrigem.getText().trim();
                System.out.println("   Artista: " + artista);
                System.out.println("   Gravadora: " + gravadora);
                System.out.println("   País Origem: " + paisOrigem);
                return new Cd(descricao, genero, estoque, preco,
                            artista, gravadora, paisOrigem,
                            caminhoImagem, descricaoDetalhada);
            default:
                System.out.println("⚠️  Categoria desconhecida, criando produto genérico");
                return new Produto(descricao, genero, estoque, preco, categoria, 
                                 caminhoImagem, descricaoDetalhada);
        }
    } catch (Exception e) {
        System.err.println("❌ Erro ao criar produto: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}
}