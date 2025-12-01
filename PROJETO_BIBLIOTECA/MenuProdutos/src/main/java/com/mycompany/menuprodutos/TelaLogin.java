/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.util.Random;
import java.util.HashMap;
import novoPack.BancoDeDados;

public class TelaLogin extends JFrame {
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private static HashMap<String, String> codigosRecuperacao = new HashMap<>();
    private String emailRecuperacao;

    public TelaLogin() {
        setTitle("Livraria Entre Palavras - Login");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Cores da paleta da biblioteca
        Color corFundo = new Color(249, 237, 202);
        Color corPrimaria = new Color(115, 103, 47);
        Color corSecundaria = new Color(89, 55, 30);
        Color corBotao = new Color(89, 35, 30);
        Color corTexto = new Color(89, 55, 30);
        Color corBorda = new Color(210, 180, 140);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(corFundo);
        painelPrincipal.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Header
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

        // Painel 
        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(corFundo);
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("Login");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Serif", Font.BOLD, 24));
        titulo.setForeground(corSecundaria);

        JLabel subtitulo = new JLabel("Entre em sua conta");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Serif", Font.PLAIN, 14));
        subtitulo.setForeground(corSecundaria);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 25, 0));

        // Formulário de login
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(corFundo);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Campo E-mail
        gbc.gridx = 0; gbc.gridy = 0;
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

        // Campo Senha
        gbc.gridx = 0; gbc.gridy = 1;
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

       
        JPanel formularioContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formularioContainer.setBackground(corFundo);
        formularioContainer.add(painelFormulario);

        
        JButton btnLogin = new JButton("ENTRAR");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setBackground(corBotao);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Serif", Font.BOLD, 16));
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 45, 40), 2),
            BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(120, 45, 40));
            }
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(corBotao);
            }
        });

        JPanel linkPanel = new JPanel();
        linkPanel.setBackground(corFundo);
        linkPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        
        JButton btnEsqueciSenha = new JButton("Esqueci minha senha");
        btnEsqueciSenha.setFont(new Font("Serif", Font.PLAIN, 14));
        btnEsqueciSenha.setForeground(corBotao);
        btnEsqueciSenha.setBackground(corFundo);
        btnEsqueciSenha.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnEsqueciSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEsqueciSenha.setFocusPainted(false);
        btnEsqueciSenha.setContentAreaFilled(false);
        btnEsqueciSenha.setBorderPainted(false);
        
      
        btnEsqueciSenha.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnEsqueciSenha.setForeground(new Color(120, 45, 40));
                btnEsqueciSenha.setBackground(new Color(240, 230, 210));
            }
            public void mouseExited(MouseEvent e) {
                btnEsqueciSenha.setForeground(corBotao);
                btnEsqueciSenha.setBackground(corFundo);
            }
        });
        
        btnEsqueciSenha.addActionListener(e -> abrirRecuperacaoSenha());
        linkPanel.add(btnEsqueciSenha);

       
        JPanel cadastroPanel = new JPanel();
        cadastroPanel.setBackground(corFundo);
        cadastroPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel lblCadastro = new JLabel("<html>Não tem uma conta? <u><b style='color: #59231E;'>Cadastre-se aqui</b></u></html>");
        lblCadastro.setFont(new Font("Serif", Font.PLAIN, 14));
        lblCadastro.setForeground(corTexto);
        lblCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCadastro.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new TelaCadastro();
                dispose();
            }
            public void mouseEntered(MouseEvent e) {
                lblCadastro.setForeground(new Color(120, 45, 40));
            }
            public void mouseExited(MouseEvent e) {
                lblCadastro.setForeground(corTexto);
            }
        });

        cadastroPanel.add(lblCadastro);

       
        painelConteudo.add(titulo);
        painelConteudo.add(subtitulo);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 10)));
        painelConteudo.add(formularioContainer);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 20)));
        painelConteudo.add(btnLogin);
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 10)));
        painelConteudo.add(linkPanel);
        painelConteudo.add(cadastroPanel);

        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);

       
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(corFundo);
        southPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JButton btnSair = new JButton("Sair");
        btnSair.setFocusPainted(false);
        btnSair.setBackground(new Color(200, 0, 0));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Serif", Font.BOLD, 12));
        btnSair.setPreferredSize(new Dimension(80, 30));
        btnSair.addActionListener(e -> dispose());
        southPanel.add(btnSair);
        
        painelPrincipal.add(southPanel, BorderLayout.SOUTH);

        add(painelPrincipal);

      
        btnLogin.addActionListener(e -> realizarLogin());
        
     
        campoSenha.addActionListener(e -> realizarLogin());
        
      
        pack();
        setSize(450, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void realizarLogin() {
        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarErro("Por favor, preencha todos os campos.");
            return;
        }

        if (!email.contains("@") || !email.endsWith("gmail.com")) {
            mostrarErro("Por favor, insira um e-mail válido do G-mail.");
            return;
        }

        
        if (BancoDeDados.verificarCredenciais(email, senha)) {
            // Login bem-sucedido
            String nomeUsuario = BancoDeDados.obterNomeUsuario(email);
            int idUsuario = BancoDeDados.obterIdUsuario(email);
            
          
            SessaoUsuario.login(email, nomeUsuario, idUsuario);
            
            mostrarSucesso("Login realizado com sucesso!\nBem-vindo(a), " + nomeUsuario + "!");
            dispose();
            
        } else {
            mostrarErro("E-mail ou senha incorretos.\nVerifique suas credenciais.");
        }
    }

    private void abrirRecuperacaoSenha() {
        JDialog dialog = new JDialog(this, "Recuperação de Senha", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        Color corFundo = new Color(249, 237, 202);
        Color corPrimaria = new Color(115, 103, 47);
        Color corSecundaria = new Color(89, 55, 30);
        Color corBotao = new Color(89, 35, 30);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(corFundo);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(corFundo);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tituloLabel = new JLabel("Recuperar Senha");
        tituloLabel.setFont(new Font("Serif", Font.BOLD, 20));
        tituloLabel.setForeground(corSecundaria);
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><div style='text-align: center;'>Insira seu e-mail para receber um código de verificação</div></html>");
        descLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        descLabel.setForeground(corSecundaria);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setBorder(new EmptyBorder(10, 0, 20, 0));

        headerPanel.add(tituloLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(descLabel);

        // Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(corFundo);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel labelEmail = new JLabel("E-mail:");
        labelEmail.setFont(new Font("Serif", Font.BOLD, 12));
        labelEmail.setForeground(corSecundaria);
        formPanel.add(labelEmail, gbc);

        gbc.gridx = 1;
        JTextField campoEmailRec = new JTextField(15);
        campoEmailRec.setFont(new Font("Serif", Font.PLAIN, 12));
        campoEmailRec.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 180, 140), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        formPanel.add(campoEmailRec, gbc);

        JButton btnEnviarCodigo = new JButton("ENVIAR CÓDIGO");
        btnEnviarCodigo.setFont(new Font("Serif", Font.BOLD, 12));
        btnEnviarCodigo.setBackground(corBotao);
        btnEnviarCodigo.setForeground(Color.WHITE);
        btnEnviarCodigo.setFocusPainted(false);
        btnEnviarCodigo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 45, 40), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btnEnviarCodigo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(corFundo);
        buttonPanel.add(btnEnviarCodigo);

        painel.add(headerPanel, BorderLayout.NORTH);
        painel.add(formPanel, BorderLayout.CENTER);
        painel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(painel);

        btnEnviarCodigo.addActionListener(e -> {
            emailRecuperacao = campoEmailRec.getText().trim();
            
            if (emailRecuperacao.isEmpty() || !emailRecuperacao.contains("@")) {
                JOptionPane.showMessageDialog(dialog, "Por favor, insira um e-mail válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!BancoDeDados.emailExiste(emailRecuperacao)) {
                JOptionPane.showMessageDialog(dialog, "Este e-mail não está cadastrado em nosso sistema.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Gerar código de verificação
            String codigo = gerarCodigoVerificacao();
            codigosRecuperacao.put(emailRecuperacao, codigo);
            
            // Enviar código por email
            try {
                enviarCodigoPorEmail(emailRecuperacao, codigo);
                JOptionPane.showMessageDialog(dialog, 
                    "Código de verificação enviado para:\n" + emailRecuperacao + 
                    "\n\nVerifique sua caixa de entrada e spam.",
                    "Código Enviado", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                abrirVerificacaoCodigo(dialog, emailRecuperacao);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Erro ao enviar email: " + ex.getMessage() + 
                    "\n\nCódigo de teste: " + codigo, 
                    "Erro no Envio", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void enviarCodigoPorEmail(String destinatario, String codigo) {
        try {
            String assunto = "Código de Recuperação de Senha - Livraria Entre Palavras";
            String mensagem = criarMensagemCodigoRecuperacao(codigo);
            
            EmailSender.enviarEmailRecuperacaoSenha(destinatario, assunto, mensagem);
            System.out.println("✅ Código de recuperação enviado para: " + destinatario);
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar código por email: " + e.getMessage());
            throw new RuntimeException("Falha no envio do email: " + e.getMessage());
        }
    }

    private String criarMensagemCodigoRecuperacao(String codigo) {
        return "<!DOCTYPE html>" +
               "<html lang='pt-BR'>" +
               "<head>" +
               "    <meta charset='UTF-8'>" +
               "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
               "    <title>Recuperação de Senha</title>" +
               "    <style>" +
               "        body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 20px; background-color: #f9f5f0; }" +
               "        .container { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }" +
               "        .header { text-align: center; background: linear-gradient(135deg, #8B4513, #A0522D); color: white; padding: 20px; border-radius: 10px 10px 0 0; margin: -30px -30px 20px -30px; }" +
               "        .header h1 { margin: 0; font-size: 24px; }" +
               "        .logo { font-size: 28px; font-weight: bold; margin-bottom: 10px; }" +
               "        .content { padding: 20px 0; }" +
               "        .codigo { background-color: #f0f8ff; padding: 25px; border-radius: 8px; margin: 20px 0; text-align: center; border: 2px dashed #8B4513; }" +
               "        .codigo-numero { font-size: 32px; font-weight: bold; color: #8B4513; letter-spacing: 8px; }" +
               "        .aviso { background-color: #fff8e1; padding: 15px; border-radius: 8px; margin: 15px 0; border-left: 4px solid #ffa000; }" +
               "        .footer { text-align: center; margin-top: 30px; padding-top: 20px; border-top: 2px solid #eee; color: #666; font-size: 12px; }" +
               "        .destaque { color: #8B4513; font-weight: bold; }" +
               "    </style>" +
               "</head>" +
               "<body>" +
               "    <div class='container'>" +
               "        <div class='header'>" +
               "            <div class='logo'>📚 Livraria Entre Palavras</div>" +
               "            <h1>Recuperação de Senha</h1>" +
               "        </div>" +
               "        " +
               "        <div class='content'>" +
               "            <h2>Olá!</h2>" +
               "            <p>Recebemos uma solicitação para redefinir a senha da sua conta.</p>" +
               "            " +
               "            <div class='codigo'>" +
               "                <h3 style='color: #8B4513; margin-top: 0;'>Seu Código de Verificação</h3>" +
               "                <div class='codigo-numero'>" + codigo + "</div>" +
               "                <p style='margin-top: 15px; color: #555;'>Use este código para redefinir sua senha</p>" +
               "            </div>" +
               "            " +
               "            <div class='aviso'>" +
               "                <h4 style='color: #8B4513; margin-top: 0;'>⚠️ Importante</h4>" +
               "                <ul style='text-align: left;'>" +
               "                    <li>Este código é válido por <strong>15 minutos</strong></li>" +
               "                    <li>Não compartilhe este código com ninguém</li>" +
               "                    <li>Se você não solicitou esta redefinição, ignore este email</li>" +
               "                </ul>" +
               "            </div>" +
               "            " +
               "            <p>Insira este código na tela de recuperação de senha para continuar o processo.</p>" +
               "        </div>" +
               "        " +
               "        <div class='footer'>" +
               "            <p><strong>Livraria Entre Palavras</strong></p>" +
               "            <p>📞 (11) 99999-9999 | 📧 contato@livrariaentrepalavras.com.br</p>" +
               "            <p>🕒 Horário de atendimento: Segunda a Sábado, 9h às 18h</p>" +
               "            <p style='font-size: 11px; color: #999; margin-top: 15px;'>" +
               "                Este é um email automático, por favor não responda.<br>" +
               "                Se tiver dúvidas, entre em contato conosco pelos canais acima." +
               "            </p>" +
               "        </div>" +
               "    </div>" +
               "</body>" +
               "</html>";
    }

    private void abrirVerificacaoCodigo(JDialog parentDialog, String email) {
        parentDialog.dispose();
        
        JDialog dialog = new JDialog(this, "Verificação de Código", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        Color corFundo = new Color(249, 237, 202);
        Color corSecundaria = new Color(89, 55, 30);
        Color corBotao = new Color(89, 35, 30);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(corFundo);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(corFundo);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tituloLabel = new JLabel("Verifique seu E-mail");
        tituloLabel.setFont(new Font("Serif", Font.BOLD, 18));
        tituloLabel.setForeground(corSecundaria);
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><div style='text-align: center;'>Insira o código de 6 dígitos enviado para:<br><strong>" + email + "</strong></div></html>");
        descLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        descLabel.setForeground(corSecundaria);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setBorder(new EmptyBorder(10, 0, 15, 0));

        headerPanel.add(tituloLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(descLabel);

        // Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(corFundo);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel labelCodigo = new JLabel("Código:");
        labelCodigo.setFont(new Font("Serif", Font.BOLD, 12));
        labelCodigo.setForeground(corSecundaria);
        formPanel.add(labelCodigo, gbc);

        gbc.gridx = 1;
        JTextField campoCodigo = new JTextField(10);
        campoCodigo.setFont(new Font("Serif", Font.PLAIN, 12));
        campoCodigo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 180, 140), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        formPanel.add(campoCodigo, gbc);

        JButton btnVerificar = new JButton("VERIFICAR");
        btnVerificar.setFont(new Font("Serif", Font.BOLD, 12));
        btnVerificar.setBackground(corBotao);
        btnVerificar.setForeground(Color.WHITE);
        btnVerificar.setFocusPainted(false);
        btnVerificar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 45, 40), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(corFundo);
        buttonPanel.add(btnVerificar);

        painel.add(headerPanel, BorderLayout.NORTH);
        painel.add(formPanel, BorderLayout.CENTER);
        painel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(painel);

        btnVerificar.addActionListener(e -> {
            String codigoDigitado = campoCodigo.getText().trim();
            String codigoCorreto = codigosRecuperacao.get(email);

            if (codigoDigitado.equals(codigoCorreto)) {
                dialog.dispose();
                abrirRedefinicaoSenha(email);
            } else {
                JOptionPane.showMessageDialog(dialog, "Código inválido. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void abrirRedefinicaoSenha(String email) {
        JDialog dialog = new JDialog(this, "Nova Senha", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        Color corFundo = new Color(249, 237, 202);
        Color corSecundaria = new Color(89, 55, 30);
        Color corBotao = new Color(89, 35, 30);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(corFundo);
        painel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(corFundo);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tituloLabel = new JLabel("Nova Senha");
        tituloLabel.setFont(new Font("Serif", Font.BOLD, 18));
        tituloLabel.setForeground(corSecundaria);
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><div style='text-align: center;'>Crie uma nova senha para sua conta</div></html>");
        descLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        descLabel.setForeground(corSecundaria);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setBorder(new EmptyBorder(10, 0, 15, 0));

        headerPanel.add(tituloLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(descLabel);

        // Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(corFundo);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nova Senha
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel labelSenha = new JLabel("Nova Senha:");
        labelSenha.setFont(new Font("Serif", Font.BOLD, 12));
        labelSenha.setForeground(corSecundaria);
        formPanel.add(labelSenha, gbc);

        gbc.gridx = 1;
        JPasswordField campoSenha = new JPasswordField(15);
        campoSenha.setFont(new Font("Serif", Font.PLAIN, 12));
        campoSenha.setEchoChar('•');
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 180, 140), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        formPanel.add(campoSenha, gbc);

        // Confirmar Senha
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel labelConfirmar = new JLabel("Confirmar Senha:");
        labelConfirmar.setFont(new Font("Serif", Font.BOLD, 12));
        labelConfirmar.setForeground(corSecundaria);
        formPanel.add(labelConfirmar, gbc);

        gbc.gridx = 1;
        JPasswordField campoConfirmar = new JPasswordField(15);
        campoConfirmar.setFont(new Font("Serif", Font.PLAIN, 12));
        campoConfirmar.setEchoChar('•');
        campoConfirmar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 180, 140), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        formPanel.add(campoConfirmar, gbc);

        JButton btnRedefinir = new JButton("REDEFINIR SENHA");
        btnRedefinir.setFont(new Font("Serif", Font.BOLD, 12));
        btnRedefinir.setBackground(corBotao);
        btnRedefinir.setForeground(Color.WHITE);
        btnRedefinir.setFocusPainted(false);
        btnRedefinir.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 45, 40), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(corFundo);
        buttonPanel.add(btnRedefinir);

        painel.add(headerPanel, BorderLayout.NORTH);
        painel.add(formPanel, BorderLayout.CENTER);
        painel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(painel);

        btnRedefinir.addActionListener(e -> {
            String senha = new String(campoSenha.getPassword());
            String confirmar = new String(campoConfirmar.getPassword());

            if (senha.length() < 6) {
                JOptionPane.showMessageDialog(dialog, "A senha deve ter pelo menos 6 caracteres.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!senha.equals(confirmar)) {
                JOptionPane.showMessageDialog(dialog, "As senhas não coincidem.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Atualizar senha no banco de dados
            if (BancoDeDados.atualizarSenha(email, senha)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Senha redefinida com sucesso!\nVocê já pode fazer login com sua nova senha.",
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                codigosRecuperacao.remove(email); // Limpar código usado
                
                // Fechar esta tela e abrir o login novamente
                dispose();
                new TelaLogin();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Erro ao redefinir senha. Tente novamente.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }


    private String gerarCodigoVerificacao() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro no Login", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Login Bem-sucedido", JOptionPane.INFORMATION_MESSAGE);
    }
}