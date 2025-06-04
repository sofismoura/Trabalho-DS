/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaInicial extends JFrame {
    // criei a classe TelaInicial que é uma janela (herda JFrame, que é janela pronta)

    public TelaInicial() {
        // construtor, que roda assim que a tela aparece — aqui a gente monta tudo

        setTitle("Livraria Entre Palavras - Login"); // Adicionado título da janela
        setResizable(false); // Torna a janela não redimensionável

        // Configura o comportamento de fechamento da janela
        // Agora, ao clicar no 'X', apenas esta janela será fechada, não o aplicativo inteiro.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        // Adiciona o ícone da aplicação
        ImageIcon icon = new ImageIcon("src/assets/images/Logo.png");
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            setIconImage(icon.getImage());
        } else {
            System.err.println("Erro ao carregar a imagem do ícone da TelaInicial: src/assets/images/Logo.png");
        }

        // Painel principal
        JPanel painelPrincipal = new JPanel(); // Renomeado para evitar conflito com 'painel' interno
        painelPrincipal.setLayout(new BorderLayout()); // Usando BorderLayout para organizar a logo e o conteúdo
        painelPrincipal.setBackground(new Color(249, 237, 202)); // bege claro

        // --- Adicionar a logo na parte superior (NORTH) ---
        JPanel logoTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoTopPanel.setBackground(new Color(115, 103, 47)); // Cor da navbar do Menu_Produtos
        logoTopPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String logoPath = "src/assets/images/Logo.png";
        ImageIcon logoIcon = new ImageIcon(logoPath);
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image logoImage = logoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH); // Tamanho menor para a logo
            logoIcon = new ImageIcon(logoImage);
            JLabel logoLabel = new JLabel(logoIcon);
            logoTopPanel.add(logoLabel);
        } else {
            JLabel logoText = new JLabel("LIVRARIA"); // Texto alternativo se a imagem não carregar
            logoText.setFont(new Font("Serif", Font.BOLD, 20));
            logoText.setForeground(Color.WHITE);
            logoTopPanel.add(logoText);
            System.err.println("Erro ao carregar a imagem da logo na TelaInicial: " + logoPath);
        }
        painelPrincipal.add(logoTopPanel, BorderLayout.NORTH);


        // Painel para o conteúdo central (Boas-vindas, Botões, Aviso)
        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(new Color(249, 237, 202)); // bege claro
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Label de boas-vindas
        JLabel titulo = new JLabel("Bem-Vindo!");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Serif", Font.BOLD, 22));
        titulo.setForeground(new Color(89, 55, 30)); // marrom escuro

        JLabel subtitulo = new JLabel("Escolha a opção que mais se adequa a você:");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Serif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(89, 55, 30));
        subtitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Botões
        JButton btnCadastro = criarBotao("CADASTRE-SE");
        JButton btnLogin = criarBotao("LOGIN");
        JButton btnAdmin = criarBotao("ADMINISTRADORES");

        // Texto explicativo
        JLabel aviso = new JLabel(
            "<html><div style='text-align: justify; width: 250px; margin: 0 auto; font-size: 14pt;'>"
            + "O acesso à área do administrador é restrito exclusivamente àqueles que gerenciam o" 
            + "<br>"
            + "código da página."
            + "</div></html>"
        );
        aviso.setAlignmentX(Component.CENTER_ALIGNMENT);
        aviso.setFont(new Font("Serif", Font.ITALIC, 12));
        aviso.setForeground(new Color(130, 100, 80));
        aviso.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Adiciona componentes ao painel de conteúdo
        painelConteudo.add(titulo);
        painelConteudo.add(subtitulo);
        painelConteudo.add(btnCadastro);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 5)));
        painelConteudo.add(btnLogin);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 5)));
        painelConteudo.add(btnAdmin);
        painelConteudo.add(aviso);

        // Adiciona o painel de conteúdo ao centro do painel principal
        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);

        // --- Adicionar o botão "Sair" na parte inferior (SOUTH) ---
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(new Color(249, 237, 202)); // Mesma cor de fundo
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnSairTelaInicial = new JButton("Sair");
        btnSairTelaInicial.setFocusPainted(false);
        btnSairTelaInicial.setBackground(new Color(200, 0, 0)); // Vermelho
        btnSairTelaInicial.setForeground(Color.WHITE);
        btnSairTelaInicial.setFont(new Font("Serif", Font.BOLD, 14));
        btnSairTelaInicial.addActionListener(e -> {
            dispose(); // Fecha apenas esta janela (TelaInicial)
        });
        southPanel.add(btnSairTelaInicial);
        
        painelPrincipal.add(southPanel, BorderLayout.SOUTH);


        // Janela
        add(painelPrincipal); // Adiciona o painel principal na janela (JFrame)
        pack(); // Ajusta o tamanho da janela com base nos componentes
        setLocationRelativeTo(null); // Centraliza a janela na tela do computador
        setVisible(true); // Deixa a janela visível pra aparecer pra gente

        // Ações dos botões (você pode adaptar pra abrir outras telas)
        btnCadastro.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir tela de cadastro"));
        btnLogin.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir tela de login"));
        btnAdmin.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir área de administrador"));
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setBackground(new Color(89, 35, 30)); // marrom escuro
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Serif", Font.BOLD, 14));
        botao.setMaximumSize(new Dimension(200, 30));
        return botao;
    }

    // Método main para testar a TelaInicial separadamente, se desejar
    /*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaInicial();
        });
    }
    */
}

