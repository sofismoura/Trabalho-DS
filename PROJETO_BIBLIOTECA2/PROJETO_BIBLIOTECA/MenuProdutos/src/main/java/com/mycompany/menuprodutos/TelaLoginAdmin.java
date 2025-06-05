/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaLoginAdmin extends JFrame { 

    public TelaLoginAdmin() { 
        setTitle("Livraria Entre Palavras - Login de Administrador"); 
        setResizable(false); 

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ImageIcon icon = new ImageIcon("src/assets/images/Logo.png");
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            setIconImage(icon.getImage());
        } else {
            System.err.println("Erro ao carregar a imagem do ícone da TelaLoginAdmin: src/assets/images/Logo.png");
        }

        JPanel painelPrincipal = new JPanel(); 
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBackground(new Color(249, 237, 202)); 
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

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
            System.err.println("Erro ao carregar a imagem da logo na TelaLoginAdmin: " + logoPath);
        }
        painelPrincipal.add(logoTopPanel, BorderLayout.NORTH);

        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(new Color(249, 237, 202));
        
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(249, 237, 202));
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));
        painelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel linha1 = new JPanel();
        linha1.setBackground(new Color(249, 237, 202));
        linha1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JLabel bemVindoDeVolta = new JLabel("Bem-Vindo de Volta");
        bemVindoDeVolta.setFont(new Font("Serif", Font.BOLD, 22));
        bemVindoDeVolta.setForeground(new Color(89, 55, 30));

        linha1.add(bemVindoDeVolta);

        JLabel administrador = new JLabel("Administrador!");
        administrador.setFont(new Font("Serif", Font.BOLD, 22));
        administrador.setForeground(new Color(89, 55, 30));
        administrador.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelTitulo.add(linha1);
        painelTitulo.add(administrador);

        JLabel subtitulo = new JLabel("Coloque seu email e sua senha para continuar:");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Serif", Font.PLAIN, 14)); 
        subtitulo.setForeground(new Color(89, 55, 30));
        subtitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(new Color(249, 237, 202));
        GridBagConstraints gbc = new GridBagConstraints(); 

        JLabel labelEmail = new JLabel("E-mail (gmail):");
        labelEmail.setFont(new Font("Serif", Font.PLAIN, 14));
        labelEmail.setForeground(new Color(89, 55, 30));

        JTextField campoEmail = new JTextField(20);

        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(new Font("Serif", Font.PLAIN, 14));
        labelSenha.setForeground(new Color(89, 55, 30));

        JPasswordField campoSenha = new JPasswordField(20);
        campoSenha.setEchoChar('•'); 

        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 0, 0); 
        gbc.weightx = 1.0; 

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelFormulario.add(labelEmail, gbc);

        gbc.gridy = 1;
        painelFormulario.add(campoEmail, gbc);

        gbc.gridy = 2;
        painelFormulario.add(labelSenha, gbc);

        gbc.gridy = 3;
        painelFormulario.add(campoSenha, gbc);

        JButton btnEntrar = new JButton("Concluir");
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.setBackground(new Color(89, 35, 30)); 
        btnEntrar.setForeground(Color.WHITE); 
        btnEntrar.setFocusPainted(false); 
        btnEntrar.setFont(new Font("Serif", Font.BOLD, 14)); 
        // AJUSTADO: Definindo o tamanho preferencial para 100x40 para coincidir com o botão 'Sair'
        btnEntrar.setPreferredSize(new Dimension(150, 50)); 
        btnEntrar.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); 
        btnEntrar.setHorizontalAlignment(SwingConstants.CENTER); 

        painelConteudo.add(painelTitulo);
        painelConteudo.add(subtitulo);
        painelConteudo.add(painelFormulario);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 20))); 
        painelConteudo.add(btnEntrar);

        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(new Color(249, 237, 202)); 
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnSairTelaAdmin = new JButton("Sair");
        btnSairTelaAdmin.setFocusPainted(false);
        btnSairTelaAdmin.setBackground(new Color(200, 0, 0)); 
        btnSairTelaAdmin.setForeground(Color.WHITE);
        btnSairTelaAdmin.setFont(new Font("Serif", Font.BOLD, 14));
        // Mantém o tamanho 100x40
        btnSairTelaAdmin.setPreferredSize(new Dimension(100, 40)); 
        btnSairTelaAdmin.addActionListener(e -> {
            dispose(); 
        });
        southPanel.add(btnSairTelaAdmin);
        
        painelPrincipal.add(southPanel, BorderLayout.SOUTH);

        add(painelPrincipal); 
        pack(); 
        setLocationRelativeTo(null); 
        setVisible(true); 

        btnEntrar.addActionListener(e -> {
            String email = campoEmail.getText(); 
            String senha = new String(campoSenha.getPassword()); 

            JOptionPane.showMessageDialog(this,
                    "E-mail (gmail): " + email + "\nSenha: " + senha,
                    "Dados inseridos",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
