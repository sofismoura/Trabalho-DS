/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

// Aqui é onde importo tudo que preciso pra fazer a interface bonitona funcionar
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author dti
 */
public class TelaLogin extends JFrame{
    
    public TelaLogin(){
        
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(249, 237, 202));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 30,20,30));
        
        JPanel painelTítulo = new JPanel();
        painelTítulo.setBackground(new Color(249, 237, 202));
        painelTítulo.setLayout(new BoxLayout(painelTítulo, BoxLayout.Y_AXIS));
        painelTítulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel linha1 = new JPanel();
        linha1.setBackground(new Color(249, 237, 202));
        linha1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JLabel bemVindoDeVolta = new JLabel("Bem-Vindo de Volta");
        bemVindoDeVolta.setFont(new Font("Serif", Font.BOLD, 22));
        bemVindoDeVolta.setForeground(new Color(89, 55, 30));
        
        linha1.add(bemVindoDeVolta);
        
        painelTítulo.add(linha1);
        
        JLabel subtitulo = new JLabel("Coloque seu email e sua senha para continuar:");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Serif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(89, 55, 30));
        subtitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JPanel painelFormulario = new JPanel (new GridBagLayout());
        painelFormulario.setBackground(new Color(249, 237, 202));
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel labelEmail = new JLabel("E-mail (gamil): ");
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
        gbc.gridx = 0;
        painelFormulario.add(labelEmail, gbc);
        
        gbc.gridy = 1;
        painelFormulario.add(campoEmail, gbc);
        
        gbc.gridy = 2;
        painelFormulario.add(labelSenha, gbc);
        
        gbc.gridy = 3;
        painelFormulario.add(campoSenha, gbc);
        
        JButton bntConcluir = new JButton("Concluir");
        bntConcluir.setAlignmentX(Component.CENTER_ALIGNMENT);
        bntConcluir.setBackground(new Color(89, 35, 30));
        bntConcluir.setForeground(Color.WHITE);
        bntConcluir.setFocusPainted(false);
        bntConcluir.setFont(new Font("Serif", Font.BOLD, 14));
        bntConcluir.setMaximumSize(new Dimension(200, 30));
        bntConcluir.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bntConcluir.setHorizontalAlignment(SwingConstants.CENTER);
        
        painel.add(painelTítulo);
        painel.add(subtitulo);
        painel.add(painelFormulario);
        painel.add(Box.createRigidArea(new Dimension(0, 20)));
        painel.add(bntConcluir);
        
        add(painel);
        setSize(380, 340);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
                bntConcluir.addActionListener(e -> {
            String email = campoEmail.getText();
            String senha = new String(campoSenha.getPassword());

            JOptionPane.showMessageDialog(this,
                    "E-mail (gmail): " + email + "\nSenha: " + senha,
                    "Dados inseridos",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}