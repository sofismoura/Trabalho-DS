/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaInicial extends JFrame {

    public TelaInicial() {

        setTitle("Livraria Entre Palavras - Login");
        setResizable(false); 

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        ImageIcon icon = new ImageIcon("src/assets/images/Logo.png");
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            setIconImage(icon.getImage());
        } else {
            System.err.println("Erro ao carregar a imagem do ícone da TelaInicial: src/assets/images/Logo.png");
        }

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout()); 
        painelPrincipal.setBackground(new Color(249, 237, 202));

        JPanel logoTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoTopPanel.setBackground(new Color(115, 103, 47));
        logoTopPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String logoPath = "src/assets/images/Logo.png";
        ImageIcon logoIcon = new ImageIcon(logoPath);
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image logoImage = logoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(logoImage);
            JLabel logoLabel = new JLabel(logoIcon);
            logoTopPanel.add(logoLabel);
        } else {
            JLabel logoText = new JLabel("LIVRARIA");
            logoText.setFont(new Font("Serif", Font.BOLD, 20));
            logoText.setForeground(Color.WHITE);
            logoTopPanel.add(logoText);
            System.err.println("Erro ao carregar a imagem da logo na TelaInicial: " + logoPath);
        }
        painelPrincipal.add(logoTopPanel, BorderLayout.NORTH);


        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(new Color(249, 237, 202));
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Bem-Vindo!");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Serif", Font.BOLD, 22));
        titulo.setForeground(new Color(89, 55, 30));

        JLabel subtitulo = new JLabel("Escolha a opção que mais se adequa a você:");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Serif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(89, 55, 30));
        subtitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton btnCadastro = criarBotao("CADASTRE-SE");
        JButton btnLogin = criarBotao("FAZER LOGIN"); // NOVO BOTÃO
        JButton btnAdmin = criarBotao("ADMINISTRADORES");
        
JButton btnPerfil = criarBotao("MEU PERFIL");


painelConteudo.add(btnCadastro);
painelConteudo.add(Box.createRigidArea(new Dimension(0, 5)));
painelConteudo.add(btnLogin);
painelConteudo.add(Box.createRigidArea(new Dimension(0, 5)));
painelConteudo.add(btnPerfil); // NOVO BOTÃO PERFIL
painelConteudo.add(Box.createRigidArea(new Dimension(0, 5)));
painelConteudo.add(btnAdmin);


btnPerfil.addActionListener(e -> {
    new TelaPerfil();
    this.dispose();
});

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

        painelConteudo.add(titulo);
        painelConteudo.add(subtitulo);
        painelConteudo.add(btnCadastro);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 5)));
        painelConteudo.add(btnLogin); 
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 5)));
        painelConteudo.add(btnAdmin);
        painelConteudo.add(aviso);

        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(new Color(249, 237, 202));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnSairTelaInicial = new JButton("Sair");
        btnSairTelaInicial.setFocusPainted(false);
        btnSairTelaInicial.setBackground(new Color(200, 0, 0));
        btnSairTelaInicial.setForeground(Color.WHITE);
        btnSairTelaInicial.setFont(new Font("Serif", Font.BOLD, 14));
        btnSairTelaInicial.addActionListener(e -> {
            dispose();
        });
        southPanel.add(btnSairTelaInicial);
        
        painelPrincipal.add(southPanel, BorderLayout.SOUTH);

        add(painelPrincipal);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        
        btnCadastro.addActionListener(e -> {
            TelaCadastro telaCadastro = new TelaCadastro();
            telaCadastro.setVisible(true);
            this.dispose(); 
        });
        
       
        btnLogin.addActionListener(e -> {
            new TelaLogin().setVisible(true);
            this.dispose();
        });

        btnAdmin.addActionListener(e -> {
            TelaLoginAdmin telaLoginAdmin = new TelaLoginAdmin();
            telaLoginAdmin.setVisible(true);
            this.dispose();
        });
    }
    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setBackground(new Color(89, 35, 30));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Serif", Font.BOLD, 14));
        botao.setMaximumSize(new Dimension(200, 30));
        return botao;
    }
}