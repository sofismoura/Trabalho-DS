/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import novoPack.BancoDeDados;

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
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel painelTítulo = new JPanel();
        painelTítulo.setBackground(new Color(249, 237, 202));
        painelTítulo.setLayout(new BoxLayout(painelTítulo, BoxLayout.Y_AXIS));
        painelTítulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel linha1 = new JPanel();
        linha1.setBackground(new Color(249, 237, 202));
        linha1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JLabel bemVindoAdmin = new JLabel("Login de Administrador");
        bemVindoAdmin.setFont(new Font("Serif", Font.BOLD, 22));
        bemVindoAdmin.setForeground(new Color(89, 55, 30));
        
        linha1.add(bemVindoAdmin);
        
        painelTítulo.add(linha1);
        
        JLabel subtitulo = new JLabel("Apenas administradores podem acessar esta página:");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Serif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(89, 55, 30));
        subtitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(new Color(249, 237, 202));
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel labelEmail = new JLabel("E-mail: ");
        labelEmail.setFont(new Font("Serif", Font.PLAIN, 14));
        labelEmail.setForeground(new Color(89, 55, 30));
        
        JTextField campoEmail = new JTextField(20);

        JLabel labelSenha = new JLabel("Senha: ");
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
        
        JButton bntConcluir = new JButton("Entrar");
        bntConcluir.setAlignmentX(Component.CENTER_ALIGNMENT);
        bntConcluir.setBackground(new Color(89, 35, 30));
        bntConcluir.setForeground(Color.WHITE);
        bntConcluir.setFocusPainted(false);
        bntConcluir.setFont(new Font("Serif", Font.BOLD, 14));
        bntConcluir.setPreferredSize(new Dimension(100, 70)); 
        bntConcluir.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bntConcluir.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel concluirButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        concluirButtonPanel.setBackground(new Color(249, 237, 202));
        concluirButtonPanel.add(bntConcluir);

        painelConteudo.add(painelTítulo);
        painelConteudo.add(subtitulo);
        painelConteudo.add(painelFormulario);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 20)));
        painelConteudo.add(concluirButtonPanel);
        
        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(new Color(249, 237, 202)); 
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnSairTelaLoginAdmin = new JButton("Sair");
        btnSairTelaLoginAdmin.setFocusPainted(false);
        btnSairTelaLoginAdmin.setBackground(new Color(200, 0, 0)); 
        btnSairTelaLoginAdmin.setForeground(Color.WHITE);
        btnSairTelaLoginAdmin.setFont(new Font("Serif", Font.BOLD, 14));
        btnSairTelaLoginAdmin.setPreferredSize(new Dimension(100, 40)); 
        btnSairTelaLoginAdmin.addActionListener(e -> {
            dispose(); 
        });
        southPanel.add(btnSairTelaLoginAdmin);
        
        painelPrincipal.add(southPanel, BorderLayout.SOUTH);

        add(painelPrincipal); 
        pack();
        setLocationRelativeTo(null); 
        setVisible(true); 
        
        bntConcluir.addActionListener(e -> {
            String email = campoEmail.getText().trim();
            String senha = new String(campoSenha.getPassword());

           
            boolean loginValido = validarLoginAdmin(email, senha);

            if (loginValido) {
                JOptionPane.showMessageDialog(this,
                    "Login de Administrador bem-sucedido!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
                
                
                new TelaAdministracao().setVisible(true);
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this,
                    "E-mail ou senha inválidos. Tente novamente.",
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    
    private boolean validarLoginAdmin(String email, String senha) {
        
        boolean loginValido = BancoDeDados.validarLogin(email, senha);
        
        if (loginValido) {
           
            boolean isAdmin = BancoDeDados.isAdmin(email);
            return isAdmin;
        }
        
        return false;
    }
}