/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import javax.swing.border.EmptyBorder;
import novoPack.BancoDeDados;

public class TelaCadastro extends JFrame {

    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JFormattedTextField campoTelefone;
    private JTextField campoEndereco;

    public TelaCadastro() {
        setTitle("Livraria Entre Palavras - Cadastro de Usuário");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 750);
        setLocationRelativeTo(null);

        
        Color corFundo = new Color(249, 237, 202);
        Color corPrimaria = new Color(115, 103, 47);
        Color corSecundaria = new Color(89, 55, 30);
        Color corBotao = new Color(89, 35, 30);
        Color corTexto = new Color(89, 55, 30);
        Color corBorda = new Color(210, 180, 140);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(corFundo);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        
        JPanel logoTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoTopPanel.setBackground(corPrimaria);
        logoTopPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        String logoPath = "src/assets/images/Logo.png";
        ImageIcon logoIcon = new ImageIcon(logoPath);
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image logoImage = logoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(logoImage);
            JLabel logoLabel = new JLabel(logoIcon);
            logoTopPanel.add(logoLabel);
            
            JLabel textoLabel = new JLabel("<html><div style='text-align: center; color: white; margin-left: 15px;'>" +
                                         "<b style='font-size:16px;'>Livraria</b><br>" +
                                         "<b style='font-size:18px;'>Entre Palavras</b></div></html>");
            textoLabel.setFont(new Font("Serif", Font.BOLD, 16));
            logoTopPanel.add(textoLabel);
        } else {
            JLabel logoText = new JLabel("LIVRARIA ENTRE PALAVRAS");
            logoText.setFont(new Font("Serif", Font.BOLD, 20));
            logoText.setForeground(Color.WHITE);
            logoTopPanel.add(logoText);
        }
        painelPrincipal.add(logoTopPanel, BorderLayout.NORTH);

       
        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(corFundo);
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        
        JLabel titulo = new JLabel("Cadastro de Novo Usuário");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Serif", Font.BOLD, 22));
        titulo.setForeground(corSecundaria);

        JLabel subtitulo = new JLabel("Preencha os dados abaixo para se cadastrar:");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Serif", Font.PLAIN, 14));
        subtitulo.setForeground(corSecundaria);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 25, 0));

       
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(corFundo);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

       
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel labelNome = new JLabel("Nome Completo:");
        labelNome.setFont(new Font("Serif", Font.BOLD, 14));
        labelNome.setForeground(corSecundaria);
        painelFormulario.add(labelNome, gbc);

        gbc.gridx = 1;
        campoNome = new JTextField(20);
        campoNome.setFont(new Font("Serif", Font.PLAIN, 14));
        campoNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corBorda, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        painelFormulario.add(campoNome, gbc);

       
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel labelEmail = new JLabel("E-mail:");
        labelEmail.setFont(new Font("Serif", Font.BOLD, 14));
        labelEmail.setForeground(corSecundaria);
        painelFormulario.add(labelEmail, gbc);

        gbc.gridx = 1;
        campoEmail = new JTextField(20);
        campoEmail.setFont(new Font("Serif", Font.PLAIN, 14));
        campoEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corBorda, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        painelFormulario.add(campoEmail, gbc);

       
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(new Font("Serif", Font.BOLD, 14));
        labelSenha.setForeground(corSecundaria);
        painelFormulario.add(labelSenha, gbc);

        gbc.gridx = 1;
        campoSenha = new JPasswordField(20);
        campoSenha.setFont(new Font("Serif", Font.PLAIN, 14));
        campoSenha.setEchoChar('•');
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corBorda, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        painelFormulario.add(campoSenha, gbc);

      
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel labelTelefone = new JLabel("Telefone:");
        labelTelefone.setFont(new Font("Serif", Font.BOLD, 14));
        labelTelefone.setForeground(corSecundaria);
        painelFormulario.add(labelTelefone, gbc);

        gbc.gridx = 1;
        try {
            MaskFormatter mascara = new MaskFormatter("(##) #####-####");
            campoTelefone = new JFormattedTextField(mascara);
        } catch (ParseException e) {
            campoTelefone = new JFormattedTextField();
        }
        campoTelefone.setFont(new Font("Serif", Font.PLAIN, 14));
        campoTelefone.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corBorda, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        painelFormulario.add(campoTelefone, gbc);

      
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel labelEndereco = new JLabel("Endereço:");
        labelEndereco.setFont(new Font("Serif", Font.BOLD, 14));
        labelEndereco.setForeground(corSecundaria);
        painelFormulario.add(labelEndereco, gbc);

        gbc.gridx = 1;
        campoEndereco = new JTextField(20);
        campoEndereco.setFont(new Font("Serif", Font.PLAIN, 14));
        campoEndereco.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corBorda, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        painelFormulario.add(campoEndereco, gbc);

      
        JPanel formularioContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formularioContainer.setBackground(corFundo);
        formularioContainer.add(painelFormulario);

      
        JButton btnCadastrar = new JButton("CADASTRAR");
        btnCadastrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCadastrar.setBackground(corBotao);
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setFont(new Font("Serif", Font.BOLD, 16));
        btnCadastrar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 45, 40), 2),
            BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        btnCadastrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnCadastrar.setBackground(new Color(120, 45, 40));
            }
            public void mouseExited(MouseEvent e) {
                btnCadastrar.setBackground(corBotao);
            }
        });

   
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(corFundo);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel lblLogin = new JLabel("<html>Já tem uma conta? <u><b style='color: #59231E;'>Faça login aqui</b></u></html>");
        lblLogin.setFont(new Font("Serif", Font.PLAIN, 14));
        lblLogin.setForeground(corTexto);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
       
        lblLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println("Clicou em Faça login aqui"); // Debug
                abrirTelaLogin();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblLogin.setForeground(new Color(120, 45, 40));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblLogin.setForeground(corTexto);
            }
        });

        loginPanel.add(lblLogin);

      
        painelConteudo.add(titulo);
        painelConteudo.add(subtitulo);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 10)));
        painelConteudo.add(formularioContainer);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 25)));
        painelConteudo.add(btnCadastrar);
        painelConteudo.add(loginPanel);

        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);

     
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(corFundo);
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnSair = new JButton("Sair");
        btnSair.setFocusPainted(false);
        btnSair.setBackground(new Color(200, 0, 0));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Serif", Font.BOLD, 14));
        btnSair.setPreferredSize(new Dimension(100, 35));
        btnSair.addActionListener(e -> dispose());
        southPanel.add(btnSair);
        
        painelPrincipal.add(southPanel, BorderLayout.SOUTH);

        add(painelPrincipal);

    
        btnCadastrar.addActionListener(e -> {
            realizarCadastro();
        });
    }

   
    private void abrirTelaLogin() {
        System.out.println("Abrindo TelaLogin..."); 
        TelaLogin telaLogin = new TelaLogin();
        telaLogin.setVisible(true);
        this.dispose(); 
    }

    private void realizarCadastro() {
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword());
        String telefone = campoTelefone.getText().replaceAll("[^0-9]", "");
        String endereco = campoEndereco.getText().trim();
        String tipoUsuario = "Cliente";

     
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty() || endereco.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos.", 
                "Campos Incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.endsWith("gmail.com")) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, insira um e-mail válido do G-mail.", 
                "E-mail Inválido", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (telefone.length() != 11) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, insira um número de telefone completo.", 
                "Telefone Inválido", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (senha.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "A senha deve ter pelo menos 6 caracteres.", 
                "Senha Fraca", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (BancoDeDados.emailExiste(email)) {
            JOptionPane.showMessageDialog(this, 
                "Este e-mail já está cadastrado.", 
                "E-mail em Uso", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Salvar usuário
        boolean sucesso = BancoDeDados.salvarUsuario(nome, email, senha, telefone, endereco, tipoUsuario);
        
        if (sucesso) {
            mostrarConfirmacaoCadastro(nome, email);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erro ao cadastrar usuário. Tente novamente.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarConfirmacaoCadastro(String nome, String email) {
        JDialog dialog = new JDialog(this, "Cadastro Realizado!", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

    
        Color corFundo = new Color(249, 237, 202);
        Color corPrimaria = new Color(115, 103, 47);
        Color corSecundaria = new Color(89, 55, 30);
        Color corBotao = new Color(89, 35, 30);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(corFundo);
        painel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

  
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(corFundo);
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ícone 
        JLabel iconLabel = new JLabel("✓");
        iconLabel.setFont(new Font("Serif", Font.BOLD, 48));
        iconLabel.setForeground(new Color(60, 120, 60));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título
        JLabel tituloLabel = new JLabel("Cadastro Concluído!");
        tituloLabel.setFont(new Font("Serif", Font.BOLD, 24));
        tituloLabel.setForeground(corSecundaria);
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Mensagem
        JLabel mensagemLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<b>Parabéns, " + nome + "!</b><br><br>"
                + "Sua conta foi criada com sucesso.<br>"
                + "Agora você pode explorar toda nossa livraria."
                + "</div></html>");
        mensagemLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        mensagemLabel.setForeground(corSecundaria);
        mensagemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botão
        JButton btnContinuar = new JButton("FAZER LOGIN");
        btnContinuar.setFont(new Font("Serif", Font.BOLD, 14));
        btnContinuar.setBackground(corBotao);
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setFocusPainted(false);
        btnContinuar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 45, 40), 2),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        btnContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnContinuar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        contentPanel.add(iconLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(tituloLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(mensagemLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        contentPanel.add(btnContinuar);

        painel.add(contentPanel, BorderLayout.CENTER);
        dialog.add(painel);

        btnContinuar.addActionListener(e -> {
            dialog.dispose();
            dispose();
            abrirTelaLogin(); 
        });

        dialog.setVisible(true);
    }
}