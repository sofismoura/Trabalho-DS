/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class Menu extends JFrame {

    private Produto produto;
    private JLabel quantidadeLabel;
    private JLabel precoTotalLabel;
    private int quantidade = 1;
    private DecimalFormat df = new DecimalFormat("R$ #,##0.00");
    private int borderRadius = 20; 
    private Color backgroundColor = new Color(245, 245, 220); // Bege claro (Fundo do menu)
    private Color borderColor = new Color(205, 133, 63); // Marrom claro (Cor da borda dos cards)
    private Color primaryColor = new Color(139, 69, 19); // Marrom sela (Cor primária/Logo)
    private Color secondaryColor = new Color(210, 180, 140); // Marrom claro (Botões)
    private Font primaryFont = new Font("Serif", Font.PLAIN, 16);
    private Font boldFont = new Font("Serif", Font.BOLD, 18);
    private Menu_Produtos menuProdutosReferencia; 
    private Menu_Produtos menuProdutos; 

     protected Menu(Produto produto, Menu_Produtos menuProdutos) {
        this.produto = produto;
        this.menuProdutosReferencia = menuProdutos; 
        setTitle("Detalhes da Compra");
        setUndecorated(true); 
        setBackground(new Color(0, 0, 0, 0)); 
        setSize(550, 600); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout()); 

       
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

       
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 25, 5, 25)); // Margens do cabeçalho

       
        JLabel categoriaLabel = new JLabel();
        categoriaLabel.setFont(new Font("Serif", Font.BOLD, 26));
        categoriaLabel.setForeground(primaryColor);
        categoriaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (produto instanceof Livro) {
            categoriaLabel.setText("Livro");
        } else if (produto instanceof Cd) {
            categoriaLabel.setText("CD");
        } else if (produto instanceof Dvd) {
            categoriaLabel.setText("DVD");
        }
        headerPanel.add(categoriaLabel);

      
        JButton sairButton = new JButton("X");
        sairButton.setFont(new Font("Arial", Font.BOLD, 18));
        sairButton.setForeground(Color.BLACK);
        sairButton.setBackground(secondaryColor);
        sairButton.setFocusPainted(false);
        sairButton.setBorder(new EmptyBorder(5, 10, 5, 10));
        sairButton.setPreferredSize(new Dimension(40, 30));
        sairButton.setMinimumSize(new Dimension(40, 30));
        sairButton.setMaximumSize(new Dimension(40, 30));
        sairButton.addActionListener(e -> {
            dispose(); 
        });
        headerPanel.add(sairButton);

        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(10)); 

      
        JLabel imagemLabel = new JLabel();
        imagemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon(produto.getCaminhoImagem());
            Image scaledImage = icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH); // Reduzi o tamanho da imagem
            imagemLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            imagemLabel.setText("Sem imagem");
            imagemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        mainPanel.add(imagemLabel);
        mainPanel.add(Box.createVerticalStrut(15)); 

       
        JLabel tituloLabel = new JLabel(produto.getDescricao());
        tituloLabel.setFont(new Font("Serif", Font.BOLD, 18)); 
        tituloLabel.setForeground(primaryColor);
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER); 
        mainPanel.add(tituloLabel);
        mainPanel.add(Box.createVerticalStrut(8)); 

     
        JPanel detalhesPanel = new JPanel();
        detalhesPanel.setLayout(new BoxLayout(detalhesPanel, BoxLayout.Y_AXIS));
        detalhesPanel.setOpaque(false);
        detalhesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (produto instanceof Livro) {
            Livro livro = (Livro) produto;
            detalhesPanel.add(createDetailLabel("Autor: " + livro.getAutor()));
            detalhesPanel.add(createDetailLabel("Editora: " + livro.getEditora()));
            detalhesPanel.add(createDetailLabel("Edição: " + livro.getEdicao()));
        } else if (produto instanceof Cd) {
            Cd cd = (Cd) produto;
            detalhesPanel.add(createDetailLabel("Artista: " + cd.getArtista()));
            detalhesPanel.add(createDetailLabel("Gravadora: " + cd.getGravadora()));
            detalhesPanel.add(createDetailLabel("País: " + cd.getPaisDeOrigem()));
        } else if (produto instanceof Dvd) {
            Dvd dvd = (Dvd) produto;
            detalhesPanel.add(createDetailLabel("Diretor: " + dvd.getDiretor()));
            detalhesPanel.add(createDetailLabel("Duração: " + dvd.getDuracao()));
            detalhesPanel.add(createDetailLabel("Censura: " + dvd.getCensura()));
        }
        mainPanel.add(detalhesPanel);
        mainPanel.add(Box.createVerticalStrut(10)); // Reduzi o espaçamento

        // --- Descrição do Produto ---
        JTextPane descricaoTextPane = new JTextPane();
        descricaoTextPane.setText(produto.getDescricaoDetalhada());
        descricaoTextPane.setEditable(false);
        descricaoTextPane.setBackground(new Color(255, 255, 255));
        descricaoTextPane.setFont(primaryFont);
        descricaoTextPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1), // Borda mais fina
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Espaçamento interno
        ));

        StyledDocument doc = descricaoTextPane.getStyledDocument();
        SimpleAttributeSet centerAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAlign, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), centerAlign, false);

        JScrollPane scrollPaneDescricao = new JScrollPane(descricaoTextPane);
        scrollPaneDescricao.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneDescricao.setPreferredSize(new Dimension(350, 100)); // Altura limitada para a descrição
        scrollPaneDescricao.setMaximumSize(new Dimension(350, 150));

        mainPanel.add(scrollPaneDescricao);
        mainPanel.add(Box.createVerticalStrut(12)); 

       
        JLabel precoUnitarioLabel = new JLabel("Preço Unitário: " + df.format(produto.getPreco()));
        precoUnitarioLabel.setFont(boldFont);
        precoUnitarioLabel.setForeground(primaryColor);
        precoUnitarioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(precoUnitarioLabel);
        mainPanel.add(Box.createVerticalStrut(10)); 

     
        JPanel quantidadePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // FlowLayout para centralizar
        quantidadePanel.setOpaque(false);

        JButton menosButton = createQuantityButton("-");
        quantidadePanel.add(menosButton);

        quantidadeLabel = new JLabel(String.valueOf(quantidade));
        quantidadeLabel.setFont(boldFont);
        quantidadePanel.add(quantidadeLabel);

        JButton maisButton = createQuantityButton("+");
        quantidadePanel.add(maisButton);

        menosButton.addActionListener(e -> {
            if (quantidade > 1) {
                quantidade--;
                quantidadeLabel.setText(String.valueOf(quantidade));
                atualizarPrecoTotal();
            }
        });
        maisButton.addActionListener(e -> {
            quantidade++;
            quantidadeLabel.setText(String.valueOf(quantidade));
            atualizarPrecoTotal();
        });

        mainPanel.add(quantidadePanel);
        mainPanel.add(Box.createVerticalStrut(15)); 

      
        precoTotalLabel = new JLabel("Preço Total: " + df.format(produto.getPreco()));
        precoTotalLabel.setFont(new Font("Serif", Font.BOLD, 20));
        precoTotalLabel.setForeground(primaryColor);
        precoTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(precoTotalLabel);
        mainPanel.add(Box.createVerticalStrut(20)); // Reduzi o espaçamento


   
        JButton adicionarCarrinhoButton = new JButton("Adicionar ao carrinho");
        adicionarCarrinhoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        adicionarCarrinhoButton.setBackground(secondaryColor);
        adicionarCarrinhoButton.setForeground(Color.BLACK);
        adicionarCarrinhoButton.setFont(boldFont);
        adicionarCarrinhoButton.setFocusPainted(false);
        adicionarCarrinhoButton.setBorder(new EmptyBorder(10, 30, 10, 30));
        adicionarCarrinhoButton.addActionListener(e -> {
            
            boolean produtoJaNoCarrinho = false;
            for (ItemCarrinho item : menuProdutosReferencia.carrinho) {
                if (item.getProduto().equals(produto)) {
                    item.incrementarQuantidade();
                    produtoJaNoCarrinho = true;
                    break;
                }
            }
            // Se o produto não estiver no carrinho, adiciona um novo ItemCarrinho
            if (!produtoJaNoCarrinho) {
                menuProdutosReferencia.carrinho.add(new ItemCarrinho(produto, quantidade));
            }

            JOptionPane.showMessageDialog(Menu.this, produto.getDescricao() + " (x" + quantidade + ") adicionado ao carrinho!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        mainPanel.add(adicionarCarrinhoButton);

       
        RoundedPanel2 roundedMenuPanel2 = new RoundedPanel2(borderRadius, backgroundColor, borderColor);
        roundedMenuPanel2.setLayout(new BorderLayout());
        roundedMenuPanel2.add(mainPanel, BorderLayout.CENTER);
        roundedMenuPanel2.setBorder(new EmptyBorder(15, 15, 15, 15)); // Adiciona margens ao redor do conteúdo

        // Adiciona o RoundedPanel ao JScrol
        JScrollPane scrollPaneMenu = new JScrollPane(roundedMenuPanel2);
        scrollPaneMenu.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneMenu.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneMenu.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPaneMenu, BorderLayout.CENTER);
    }

    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(primaryFont);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

     protected  JButton createQuantityButton(String text) {
        JButton button = new JButton(text);
        button.setFont(boldFont);
        button.setPreferredSize(new Dimension(60, 60)); // Botões de quantidade menores
        button.setFocusPainted(false);
        return button;
    }

     protected void atualizarPrecoTotal() {
        double precoUnitario = produto.getPreco();
        double precoTotal = precoUnitario * quantidade;
        precoTotalLabel.setText("Preço Total: " + df.format(precoTotal));
    }
}