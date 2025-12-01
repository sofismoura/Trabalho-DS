/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import novoPack.BancoDeDados;
import java.awt.geom.Ellipse2D;

public class TelaPerfil extends JFrame {
    private JTextField campoNome;
    private JTextField campoEmail;
    private JTextField campoEndereco;
    private JButton btnEditar;
    private JButton btnSalvar;
    private JButton btnAlterarSenha;
    private JButton btnSair;
    private JButton btnLogout;
    private JLabel labelImagemPerfil;
    private byte[] imagemPerfilBytes;

    // Cores da paleta
    private final Color COR_FUNDO = new Color(249, 237, 202);
    private final Color COR_PRIMARIA = new Color(115, 103, 47);
    private final Color COR_SECUNDARIA = new Color(89, 55, 30);
    private final Color COR_BOTAO_EDICAO = new Color(89, 55, 30);
    private final Color COR_BOTAO_EDICAO_HOVER = new Color(120, 75, 50); 
    private final Color COR_BORDA = new Color(210, 180, 140);
    private final Color COR_SUCESSO = new Color(50, 140, 90);
    private final Color COR_SUCESSO_HOVER = new Color(70, 160, 110);
    private final Color COR_ALERTA = new Color(70, 70, 120);
    private final Color COR_ALERTA_HOVER = new Color(90, 90, 150);
    private final Color COR_SAIR = new Color(150, 150, 150);
    private final Color COR_SAIR_HOVER = new Color(170, 170, 170);
    private final Color COR_LOGOUT = new Color(200, 0, 0);
    private final Color COR_LOGOUT_HOVER = new Color(220, 0, 0);

    public TelaPerfil() {
        if (!SessaoUsuario.isLogado()) {
            JOptionPane.showMessageDialog(this, 
                "Você precisa fazer login primeiro!\n\n" +
                "Será redirecionado para a tela de login.", 
                "Acesso Restrito", 
                JOptionPane.WARNING_MESSAGE);
            dispose();
            return;
        }

        inicializarComponentes();
        carregarImagemPerfil();
        configurarListeners();
        exibirTela();
    }

    private void inicializarComponentes() {
        setTitle("Livraria Entre Palavras - Meu Perfil");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(COR_FUNDO);
        painelPrincipal.setBorder(new EmptyBorder(0, 0, 0, 0));

        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);
        painelPrincipal.add(criarConteudoComScroll(), BorderLayout.CENTER);

        add(painelPrincipal);
    }

    private JPanel criarHeader() {
        JPanel logoTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoTopPanel.setBackground(COR_PRIMARIA);
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

        return logoTopPanel;
    }

    private JScrollPane criarConteudoComScroll() {
        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(COR_FUNDO);
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Título e Subtítulo
        JLabel titulo = new JLabel("Meu Perfil 👤");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Serif", Font.BOLD, 32));
        titulo.setForeground(COR_SECUNDARIA);

        JLabel subtitulo = new JLabel("Gerencie suas informações pessoais e credenciais");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Serif", Font.PLAIN, 15));
        subtitulo.setForeground(COR_SECUNDARIA.darker());
        subtitulo.setBorder(BorderFactory.createEmptyBorder(8, 0, 40, 0));

        painelConteudo.add(titulo);
        painelConteudo.add(subtitulo);

        
        painelConteudo.add(criarPainelPerfilCentralizado());
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 30)));
        
       
        painelConteudo.add(criarFormulario());
        painelConteudo.add(Box.createRigidArea(new Dimension(0, 40)));
        
       
        painelConteudo.add(criarPainelBotoes());

        JScrollPane scrollPane = new JScrollPane(painelConteudo);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(COR_FUNDO);

        return scrollPane;
    }

    private JPanel criarPainelPerfilCentralizado() {
        JPanel painelCard = new JPanel(new BorderLayout(10, 10));
        painelCard.setBackground(Color.WHITE);
        painelCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        painelCard.setMaximumSize(new Dimension(500, 220)); 
        painelCard.setAlignmentX(Component.CENTER_ALIGNMENT);

      
        JPanel painelImagem = new JPanel(new BorderLayout());
        painelImagem.setBackground(Color.WHITE);

        labelImagemPerfil = new JLabel();
        labelImagemPerfil.setPreferredSize(new Dimension(100, 100));
        labelImagemPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelImagemPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel tooltip = new JLabel("<html><u>Alterar Foto</u></html>"); 
        tooltip.setFont(new Font("Serif", Font.ITALIC, 11));
        tooltip.setForeground(COR_PRIMARIA);
        tooltip.setHorizontalAlignment(SwingConstants.CENTER);
        tooltip.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        painelImagem.add(labelImagemPerfil, BorderLayout.CENTER);
        painelImagem.add(tooltip, BorderLayout.SOUTH);

        JPanel wrapperImagem = new JPanel(new GridBagLayout());
        wrapperImagem.setBackground(Color.WHITE);
        wrapperImagem.add(painelImagem);
        painelCard.add(wrapperImagem, BorderLayout.WEST);

        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new BoxLayout(painelInfo, BoxLayout.Y_AXIS));
        painelInfo.setBackground(Color.WHITE);
        painelInfo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0)); 

        JLabel nomeUsuario = new JLabel(SessaoUsuario.getNomeUsuario());
        nomeUsuario.setFont(new Font("Serif", Font.BOLD, 26));
        nomeUsuario.setForeground(COR_SECUNDARIA.darker());
        nomeUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailUsuario = new JLabel(SessaoUsuario.getEmailUsuario());
        emailUsuario.setFont(new Font("Serif", Font.PLAIN, 16));
        emailUsuario.setForeground(COR_SECUNDARIA);
        emailUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailUsuario.setBorder(BorderFactory.createEmptyBorder(5, 0, 8, 0));

        JLabel tipoUsuario = new JLabel("Tipo: " + 
            (BancoDeDados.isAdmin(SessaoUsuario.getEmailUsuario()) ? "Administrador 👑" : "Cliente ⭐"));
        tipoUsuario.setFont(new Font("Serif", Font.BOLD, 14));
        tipoUsuario.setForeground(COR_PRIMARIA.darker());
        tipoUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        painelInfo.add(nomeUsuario);
        painelInfo.add(emailUsuario);
        painelInfo.add(tipoUsuario);

        painelCard.add(painelInfo, BorderLayout.CENTER);

        return painelCard;
    }

    private JPanel criarFormulario() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(COR_FUNDO);
        painelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA.darker(), 1), 
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        painelFormulario.setMaximumSize(new Dimension(500, 300));
        painelFormulario.setAlignmentX(Component.CENTER_ALIGNMENT); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; 

        
        JLabel tituloForm = new JLabel("Informações de Cadastro");
        tituloForm.setFont(new Font("Serif", Font.BOLD, 18));
        tituloForm.setForeground(COR_SECUNDARIA);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        painelFormulario.add(tituloForm, gbc);
        
        gbc.insets = new Insets(15, 5, 10, 5); 
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        
        adicionarCampoFormulario(painelFormulario, gbc, "Nome Completo:", campoNome = new JTextField(25), 1);
        campoNome.setText(SessaoUsuario.getNomeUsuario());

        
        adicionarCampoFormulario(painelFormulario, gbc, "E-mail:", campoEmail = new JTextField(25), 2);
        campoEmail.setText(SessaoUsuario.getEmailUsuario());
        campoEmail.setEditable(false);
        campoEmail.setBackground(new Color(240, 240, 240)); 

       
        adicionarCampoFormulario(painelFormulario, gbc, "Endereço:", campoEndereco = new JTextField(25), 3);
        String endereco = BancoDeDados.obterEnderecoUsuario(SessaoUsuario.getEmailUsuario());
        campoEndereco.setText(endereco != null ? endereco : "");

        
        for (JTextField campo : new JTextField[]{campoNome, campoEmail, campoEndereco}) {
            campo.setFont(new Font("Serif", Font.PLAIN, 15));
            campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            ));
            if (campo != campoEmail) {
                campo.setEditable(false);
                campo.setBackground(new Color(250, 250, 250));
            }
        }
        
        return painelFormulario;
    }

    private void adicionarCampoFormulario(JPanel painel, GridBagConstraints gbc, String label, JComponent campo, int linha) {
       
        gbc.gridx = 0; gbc.gridy = linha;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Serif", Font.BOLD, 15));
        jLabel.setForeground(COR_SECUNDARIA);
        painel.add(jLabel, gbc);

        
        gbc.gridx = 1;
        painel.add(campo, gbc);
    }

    private JPanel criarPainelBotoes() {
        JPanel painelBotoes = new JPanel(new GridBagLayout());
        painelBotoes.setBackground(COR_FUNDO);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        painelBotoes.setMaximumSize(new Dimension(500, 200));
        painelBotoes.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

      
        btnEditar = criarBotao("EDITAR PERFIL", COR_BOTAO_EDICAO, COR_BOTAO_EDICAO_HOVER, 200, 42);
        btnSalvar = criarBotao("SALVAR ALTERAÇÕES", COR_SUCESSO, COR_SUCESSO_HOVER, 200, 42);
        btnSalvar.setVisible(false);
        btnAlterarSenha = criarBotao("ALTERAR SENHA", COR_ALERTA, COR_ALERTA_HOVER, 200, 42);
        
       
        btnSair = criarBotao("FECHAR", COR_SAIR, COR_SAIR_HOVER, 200, 42);
        btnLogout = criarBotao("LOGOUT", COR_LOGOUT, COR_LOGOUT_HOVER, 200, 42);

        
        gbc.gridy = 0; gbc.gridx = 0; 
        painelBotoes.add(btnEditar, gbc);
        
        gbc.gridx = 1;
        painelBotoes.add(btnAlterarSenha, gbc);

        
        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 2;
        painelBotoes.add(btnSalvar, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        painelBotoes.add(btnSair, gbc);

       
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        painelBotoes.add(btnLogout, gbc);

        return painelBotoes;
    }

    private JButton criarBotao(String texto, Color corNormal, Color corHover, int largura, int altura) {
        JButton botao = new JButton(texto);
        botao.setBackground(corNormal);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Serif", Font.BOLD, 13));
        botao.setPreferredSize(new Dimension(largura, altura));
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(
                Math.max(corNormal.getRed() - 20, 0),
                Math.max(corNormal.getGreen() - 20, 0),
                Math.max(corNormal.getBlue() - 20, 0)
            ), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        
        botao.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(corHover);
            }
            public void mouseExited(MouseEvent e) {
                botao.setBackground(corNormal);
            }
        });

        return botao;
    }

    private void carregarImagemPerfil() {
        int idUsuario = BancoDeDados.obterIdUsuario(SessaoUsuario.getEmailUsuario());
        if (idUsuario != -1) {
            byte[] imagemBytes = BancoDeDados.carregarImagemPerfil(idUsuario);
            if (imagemBytes != null) {
                exibirImagem(imagemBytes);
                imagemPerfilBytes = imagemBytes;
            } else {
                carregarImagemPadrao();
            }
        } else {
            carregarImagemPadrao();
        }
    }

    private void carregarImagemPadrao() {
        try {
            ImageIcon iconOriginal = new ImageIcon("src/assets/images/imagem_conta.png");
            Image imagemRedimensionada = iconOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon iconPerfil = new ImageIcon(imagemRedimensionada);
            Image imagemCircular = criarImagemCircular(iconPerfil.getImage(), 100, 100);
            labelImagemPerfil.setIcon(new ImageIcon(imagemCircular));
        } catch (Exception e) {
            labelImagemPerfil.setText("👤");
            labelImagemPerfil.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            labelImagemPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private void exibirImagem(byte[] imagemBytes) {
        try {
            ImageIcon icon = new ImageIcon(imagemBytes);
            Image imagemCircular = criarImagemCircular(icon.getImage(), 100, 100);
            labelImagemPerfil.setIcon(new ImageIcon(imagemCircular));
        } catch (Exception e) {
            System.err.println("Erro ao exibir imagem: " + e.getMessage());
            carregarImagemPadrao();
        }
    }

    private Image criarImagemCircular(Image imagemOriginal, int largura, int altura) {
        BufferedImage bi = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
       
        g2.setClip(new Ellipse2D.Float(0, 0, largura, altura));
        g2.drawImage(imagemOriginal, 0, 0, largura, altura, null);
        
       
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.draw(new Ellipse2D.Float(1, 1, largura - 2, altura - 2));

        g2.dispose();
        
        return bi;
    }

    private void configurarListeners() {
        
        labelImagemPerfil.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selecionarImagemPerfil();
            }
        });

        btnEditar.addActionListener(e -> habilitarEdicao());
        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnAlterarSenha.addActionListener(e -> abrirAlteracaoSenha());
        
       
        btnSair.addActionListener(e -> dispose());
        
       
        btnLogout.addActionListener(e -> fazerLogout());
    }

   
    private void fazerLogout() {
        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja sair da sua conta?\n\n" +
            "Você será redirecionado para a tela inicial.",
            "Confirmar Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
           
            dispose();
            
           
            SessaoUsuario.logout();
            
         
            Menu_Produtos menuPrincipal = Menu_Produtos.getInstance();
            if (menuPrincipal != null) {
                menuPrincipal.atualizarIconePerfil();
            }
            
           
            new TelaInicial().setVisible(true);
            
            JOptionPane.showMessageDialog(null,
                "Logout realizado com sucesso!",
                "Logout",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionarImagemPerfil() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecionar Foto de Perfil");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Imagens (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            try {
                BufferedImage imagemOriginal = ImageIO.read(arquivo);
                if (imagemOriginal == null) {
                    throw new Exception("Formato de imagem não suportado");
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(imagemOriginal, "PNG", baos);
                imagemPerfilBytes = baos.toByteArray();

                exibirImagem(imagemPerfilBytes);
                salvarImagemPerfil();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao carregar a imagem: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvarImagemPerfil() {
        if (imagemPerfilBytes != null) {
            int idUsuario = BancoDeDados.obterIdUsuario(SessaoUsuario.getEmailUsuario());
            if (idUsuario != -1) {
                boolean sucesso = BancoDeDados.salvarImagemPerfil(idUsuario, imagemPerfilBytes);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                        "Foto de perfil atualizada com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    Menu_Produtos menuPrincipal = Menu_Produtos.getInstance();
                    if (menuPrincipal != null) {
                        menuPrincipal.atualizarIconePerfil();
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Erro ao salvar foto de perfil.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void habilitarEdicao() {
        campoNome.setEditable(true);
        campoEndereco.setEditable(true);
        campoNome.setBackground(Color.WHITE);
        campoEndereco.setBackground(Color.WHITE);
        btnEditar.setVisible(false);
        btnSalvar.setVisible(true);
    }

    private void salvarAlteracoes() {
        String novoNome = campoNome.getText().trim();
        String novoEndereco = campoEndereco.getText().trim();

        if (novoNome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome não pode estar vazio!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean sucesso = BancoDeDados.atualizarPerfilUsuario(SessaoUsuario.getEmailUsuario(), novoNome, novoEndereco);

        if (sucesso) {
            SessaoUsuario.setNomeUsuario(novoNome);
            JOptionPane.showMessageDialog(this, 
                "Perfil atualizado com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            campoNome.setEditable(false);
            campoEndereco.setEditable(false);
            campoNome.setBackground(new Color(250, 250, 250));
            campoEndereco.setBackground(new Color(250, 250, 250));
            btnEditar.setVisible(true);
            btnSalvar.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar perfil. Tente novamente.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirAlteracaoSenha() {
        JPasswordField campoSenha = new JPasswordField();
        JPasswordField campoConfirmar = new JPasswordField();
        
        Object[] mensagem = {
            "Nova Senha:", campoSenha,
            "Confirmar Senha:", campoConfirmar
        };

        int opcao = JOptionPane.showConfirmDialog(this, mensagem, "Alterar Senha", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcao == JOptionPane.OK_OPTION) {
            String senha = new String(campoSenha.getPassword());
            String confirmar = new String(campoConfirmar.getPassword());

            if (senha.length() < 6) {
                JOptionPane.showMessageDialog(this, 
                    "A senha deve ter pelo menos 6 caracteres.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!senha.equals(confirmar)) {
                JOptionPane.showMessageDialog(this, 
                    "As senhas não coincidem.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (BancoDeDados.atualizarSenha(SessaoUsuario.getEmailUsuario(), senha)) {
                JOptionPane.showMessageDialog(this, 
                    "Senha alterada com sucesso!",
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao alterar senha. Tente novamente.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exibirTela() {
        pack();
        setSize(600, 750);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}