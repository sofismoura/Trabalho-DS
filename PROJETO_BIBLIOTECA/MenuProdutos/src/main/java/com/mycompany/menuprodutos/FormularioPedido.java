/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;
import java.sql.ResultSet;
import java.sql.SQLException;
import novoPack.BancoDeDados;

public class FormularioPedido extends JFrame {

    private ArrayList<ItemCarrinho> carrinho;
    private JTextArea listaPedidosTextArea;
    private JLabel codigoPedidoLabel;
    private JTextField nomeCompletoField;
    private JTextField enderecoField;
    private JFormattedTextField telefoneField;
    private JFormattedTextField cpfField;
    private JTextField emailField;
    private JButton finalizarCompraButton;
    private JLabel precoFinalLabel;
    private DecimalFormat df = new DecimalFormat("R$ #,##0.00");
    private boolean usuarioLogado = false;
    private String emailUsuarioLogado = null;

    
    private Color primaryColor = new Color(139, 69, 19);   // Marrom café
    private Color secondaryColor = new Color(160, 120, 80); // Marrom claro
    private Color backgroundColor = new Color(253, 245, 230); // Bege muito 
    private Color cardColor = new Color(255, 250, 240);     // Bege branco
    private Color buttonColor = new Color(184, 134, 11);    // Marrom dourado
    private Color buttonHoverColor = new Color(160, 110, 0); // Marrom dourado
    private Color successColor = new Color(34, 139, 34);    // Verde sucesso
    private Color warningColor = new Color(205, 92, 92);    // Vermelho suave
    private Color textColor = new Color(60, 30, 10);        // Marrom escuro 

    public FormularioPedido(ArrayList<ItemCarrinho> carrinho) {
        this.carrinho = carrinho;
        verificarLoginUsuario();
        ImageIcon icon = new ImageIcon("src/assets/images/Logo.png");
        setIconImage(icon.getImage());
        inicializarUI();
    }

    public FormularioPedido(ArrayList<ItemCarrinho> carrinho, String emailUsuario) {
        this.carrinho = carrinho;
        if (validarEmailNoBanco(emailUsuario)) {
            this.usuarioLogado = true;
            this.emailUsuarioLogado = emailUsuario;
        } else {
            this.usuarioLogado = false;
            this.emailUsuarioLogado = null;
            JOptionPane.showMessageDialog(null, 
                "Email não cadastrado no sistema!\nPor favor, faça o cadastro primeiro.", 
                "Usuário Não Encontrado", 
                JOptionPane.WARNING_MESSAGE);
        }
        ImageIcon icon = new ImageIcon("src/assets/images/Logo.png");
        setIconImage(icon.getImage());
        inicializarUI();
    }

    private boolean validarEmailNoBanco(String email) {
        return BancoDeDados.emailExiste(email);
    }

    private void verificarLoginUsuario() {
        if (SessaoUsuario.isLogado()) {
            String emailSessao = SessaoUsuario.getEmailUsuario();
            if (validarEmailNoBanco(emailSessao)) {
                this.usuarioLogado = true;
                this.emailUsuarioLogado = emailSessao;
                System.out.println("Usuário logado via sessão: " + emailUsuarioLogado);
            } else {
                this.usuarioLogado = false;
                this.emailUsuarioLogado = null;
                SessaoUsuario.logout();
                System.out.println("Sessão inválida - email não existe no banco");
            }
        } else {
            this.usuarioLogado = false;
            System.out.println("Nenhum usuário logado detectado na sessão");
        }
    }

    private void inicializarUI() {
        setTitle("Finalizar Pedido - Livraria Entre Palavras");
        setSize(550, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(backgroundColor);
        
      
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        add(mainPanel);

     
        mainPanel.add(criarHeaderPanel(), BorderLayout.NORTH);

        
        JScrollPane scrollPane = new JScrollPane(criarCentralPanel());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(backgroundColor);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

       
        mainPanel.add(criarBottomPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel criarHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(backgroundColor);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

      
        JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        tituloPanel.setBackground(backgroundColor);
        
      
        ImageIcon logoIcon = new ImageIcon("src/assets/images/Logo.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        tituloPanel.add(logoLabel);

       
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(backgroundColor);
        
        JLabel tituloLabel = new JLabel("Seu Pedido");
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        tituloLabel.setForeground(primaryColor);
        
        JLabel subtituloLabel = new JLabel("Revise e finalize sua compra");
        subtituloLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtituloLabel.setForeground(secondaryColor);
        
        textPanel.add(tituloLabel);
        textPanel.add(subtituloLabel);
        tituloPanel.add(textPanel);

        headerPanel.add(tituloPanel, BorderLayout.CENTER);
        
        
        JPanel statusPanel = criarStatusPanel();
        headerPanel.add(statusPanel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel criarStatusPanel() {
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statusPanel.setBackground(cardColor);
        statusPanel.setBorder(new CompoundBorder(
            new LineBorder(secondaryColor, 1, true),
            new EmptyBorder(10, 20, 10, 20)
        ));

        Icon statusIcon;
        String statusText;
        Color statusColor;

        if (usuarioLogado) {
            statusIcon = criarIcon("✓", successColor, 18);
            statusText = "Conectado como: " + emailUsuarioLogado;
            statusColor = successColor;
        } else {
            statusIcon = criarIcon("⚠", warningColor, 18);
            statusText = "Faça login para continuar";
            statusColor = warningColor;
        }

        JLabel iconLabel = new JLabel(statusIcon);
        statusPanel.add(iconLabel);

        JLabel statusLabel = new JLabel(statusText);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(statusColor);
        statusPanel.add(statusLabel);

        return statusPanel;
    }

    private JPanel criarCentralPanel() {
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.setBackground(backgroundColor);

       
        centralPanel.add(criarCardItensPedido());
        centralPanel.add(Box.createVerticalStrut(15));

        
        centralPanel.add(criarCardCodigoPedido());
        centralPanel.add(Box.createVerticalStrut(15));

        
        centralPanel.add(criarCardLogin());
        centralPanel.add(Box.createVerticalStrut(15));

        
        centralPanel.add(criarCardFormulario());

        return centralPanel;
    }

    private JPanel criarCardItensPedido() {
        return criarCard("Itens do Seu Pedido", criarListaPedidosPanel(), new Color(245, 245, 220));
    }

    private JPanel criarCardCodigoPedido() {
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contentPanel.setBackground(cardColor);
        
        String codigoPedido = gerarCodigoPedido();
        codigoPedidoLabel = new JLabel("Código: " + codigoPedido);
        codigoPedidoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        codigoPedidoLabel.setForeground(primaryColor);
        codigoPedidoLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        contentPanel.add(codigoPedidoLabel);

        return criarCard("Código do Pedido", contentPanel, new Color(240, 248, 255));
    }

    private JPanel criarCardLogin() {
        JPanel contentPanel = criarLoginLogoutPanel();
        contentPanel.setBackground(cardColor);
        return criarCard("Sua Conta", contentPanel, new Color(240, 255, 240));
    }

    private JPanel criarCardFormulario() {
        return criarCard("Dados de Entrega", criarFormularioPanel(), new Color(255, 250, 240));
    }

    private JPanel criarCard(String titulo, Component conteudo, Color corDestaque) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(backgroundColor);
        card.setBorder(new EmptyBorder(0, 0, 10, 0));

       
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloLabel.setForeground(primaryColor);
        tituloLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        card.add(tituloLabel, BorderLayout.NORTH);

        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(corDestaque);
        contentPanel.setBorder(new CompoundBorder(
            new LineBorder(secondaryColor, 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        contentPanel.add(conteudo, BorderLayout.CENTER);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JScrollPane criarListaPedidosPanel() {
        listaPedidosTextArea = new JTextArea(8, 35);
        listaPedidosTextArea.setEditable(false);
        listaPedidosTextArea.setBackground(cardColor);
        listaPedidosTextArea.setForeground(textColor);
        listaPedidosTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaPedidosTextArea.setLineWrap(true);
        listaPedidosTextArea.setWrapStyleWord(true);
        
        StringBuilder listaPedidos = new StringBuilder();
        if (carrinho.isEmpty()) {
            listaPedidos.append("Seu carrinho está vazio...\n\n");
            listaPedidos.append("Adicione itens deliciosos ao carrinho antes de finalizar seu pedido!");
        } else {
            listaPedidos.append("Itens selecionados:\n\n");
            for (int i = 0; i < carrinho.size(); i++) {
                ItemCarrinho item = carrinho.get(i);
                listaPedidos.append("• ").append(item.getProduto().getDescricao())
                           .append(" (x").append(item.getQuantidade())
                           .append(") - ").append(df.format(item.calcularPrecoTotal())).append("\n");
            }
            listaPedidos.append("\n");
        }
        listaPedidosTextArea.setText(listaPedidos.toString());
        
        JScrollPane scrollPane = new JScrollPane(listaPedidosTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(cardColor);
        return scrollPane;
    }

    private JPanel criarLoginLogoutPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        if (!usuarioLogado) {
            JButton fazerLoginButton = criarBotaoEstilizado("Fazer Login / Cadastro", buttonColor, 14);
            fazerLoginButton.addActionListener(e -> abrirTelaLogin());
            fazerLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(fazerLoginButton);
            
            if (!carrinho.isEmpty()) {
                panel.add(Box.createVerticalStrut(12));
                JLabel avisoLabel = criarLabelAviso("Login obrigatório para finalizar sua compra");
                avisoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(avisoLabel);
            }
        } else {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            buttonPanel.setOpaque(false);
            
            JButton logoutButton = criarBotaoEstilizado("Sair", warningColor, 13);
            logoutButton.addActionListener(e -> {
                SessaoUsuario.logout();
                JOptionPane.showMessageDialog(this, "Logout realizado com sucesso!");
                dispose();
                new FormularioPedido(carrinho);
            });
            buttonPanel.add(logoutButton);
            
            JLabel bemVindoLabel = criarLabelSucesso("✅ Pronto para finalizar!");
            bemVindoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            buttonPanel.add(bemVindoLabel);
            
            panel.add(buttonPanel);
        }
        
        return panel;
    }

    private JPanel criarFormularioPanel() {
        JPanel formularioPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        formularioPanel.setOpaque(false);

       
        adicionarCampoFormulario(formularioPanel, " Nome Completo:", nomeCompletoField = criarTextField());
        adicionarCampoFormulario(formularioPanel, "Endereço:", enderecoField = criarTextField());
        adicionarCampoFormulario(formularioPanel, "Telefone:", telefoneField = criarTelefoneField());
        adicionarCampoFormulario(formularioPanel, "CPF:", cpfField = criarCpfField());
        adicionarCampoFormulario(formularioPanel, "📧 Email:", emailField = criarTextField());

      
        if (usuarioLogado) {
            preencherDadosUsuarioAutomaticamente();
        }

        
        if (!usuarioLogado) {
            bloquearCamposFormulario();
        }

        return formularioPanel;
    }

    private void adicionarCampoFormulario(JPanel panel, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(textColor);
        panel.add(label);
        panel.add(field);
    }

    private JTextField criarTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(
            new LineBorder(secondaryColor, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }

    private JFormattedTextField criarTelefoneField() {
        try {
            MaskFormatter telefoneMask = new MaskFormatter("(##) #####-####");
            telefoneMask.setPlaceholderCharacter('_');
            JFormattedTextField field = new JFormattedTextField(telefoneMask);
            estilizarCampoFormatado(field);
            return field;
        } catch (ParseException e) {
            JFormattedTextField field = new JFormattedTextField();
            estilizarCampoFormatado(field);
            return field;
        }
    }

    private JFormattedTextField criarCpfField() {
        try {
            MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
            cpfMask.setPlaceholderCharacter('_');
            JFormattedTextField field = new JFormattedTextField(cpfMask);
            estilizarCampoFormatado(field);
            return field;
        } catch (ParseException e) {
            JFormattedTextField field = new JFormattedTextField();
            estilizarCampoFormatado(field);
            return field;
        }
    }

    private void estilizarCampoFormatado(JFormattedTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(
            new LineBorder(secondaryColor, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(Color.WHITE);
    }

    private JPanel criarBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

       
        JPanel cardFinalizar = new JPanel(new BorderLayout(20, 20));
        cardFinalizar.setBackground(new Color(250, 240, 230));
        cardFinalizar.setBorder(new CompoundBorder(
            new LineBorder(primaryColor, 3, true),
            new EmptyBorder(25, 30, 25, 30)
        ));

        // Preço Final
        double precoFinal = calcularPrecoFinal();
        precoFinalLabel = new JLabel("Total: " + df.format(precoFinal));
        precoFinalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        precoFinalLabel.setForeground(primaryColor);
        precoFinalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardFinalizar.add(precoFinalLabel, BorderLayout.NORTH);

        
        finalizarCompraButton = criarBotaoFinalizar();
        cardFinalizar.add(finalizarCompraButton, BorderLayout.CENTER);

      
        if (carrinho.isEmpty()) {
            JLabel avisoCarrinhoLabel = criarLabelAviso("Adicione itens ao carrinho antes de finalizar");
            avisoCarrinhoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            avisoCarrinhoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            cardFinalizar.add(avisoCarrinhoLabel, BorderLayout.SOUTH);
        }

        bottomPanel.add(cardFinalizar);

        
        if (usuarioLogado) {
            adicionarListenersValidacao();
        }

        return bottomPanel;
    }

    private JButton criarBotaoFinalizar() {
        JButton button = new JButton("🎉 Finalizar Compra");
        button.setEnabled(usuarioLogado && !carrinho.isEmpty());
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(usuarioLogado && !carrinho.isEmpty() ? buttonColor : Color.GRAY);
        button.setBorder(new CompoundBorder(
            new LineBorder(usuarioLogado && !carrinho.isEmpty() ? buttonHoverColor : Color.DARK_GRAY, 3, true),
            new EmptyBorder(15, 40, 15, 40)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> finalizarPedido());

       
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(buttonHoverColor);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(buttonColor);
                }
            }
        });

        return button;
    }

    private JButton criarBotaoEstilizado(String texto, Color corFundo, int tamanhoFonte) {
        JButton button = new JButton(texto);
        button.setFont(new Font("Segoe UI", Font.BOLD, tamanhoFonte));
        button.setForeground(Color.WHITE);
        button.setBackground(corFundo);
        button.setBorder(new CompoundBorder(
            new LineBorder(corFundo.darker(), 2, true),
            new EmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(corFundo.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(corFundo);
            }
        });

        return button;
    }

    private JLabel criarLabelAviso(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(warningColor);
        return label;
    }

    private JLabel criarLabelSucesso(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(successColor);
        return label;
    }

    private Icon criarIcon(String texto, Color cor, int tamanho) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, tamanho));
        label.setForeground(cor);
        label.setOpaque(true);
        label.setBackground(cardColor);
        
        return new Icon() {
            public int getIconWidth() { return tamanho + 6; }
            public int getIconHeight() { return tamanho + 6; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                label.setBounds(x, y, getIconWidth(), getIconHeight());
                label.paint(g);
            }
        };
    }

   
    private void preencherDadosUsuarioAutomaticamente() {
        if (usuarioLogado && emailUsuarioLogado != null) {
            try {
                System.out.println("Preenchendo dados para usuário: " + emailUsuarioLogado);
                
                ResultSet usuario = BancoDeDados.getUsuarioCompletoPorEmail(emailUsuarioLogado);
                if (usuario != null && usuario.next()) {
                    nomeCompletoField.setText(usuario.getString("nome_completo"));
                    emailField.setText(usuario.getString("email"));
                    
                    String telefone = usuario.getString("telefone");
                    String endereco = usuario.getString("endereco");
                    
                    if (telefone != null && !telefone.trim().isEmpty()) {
                        try {
                            telefoneField.setValue(telefone);
                        } catch (Exception e) {
                            telefoneField.setText(telefone);
                        }
                    }
                    
                    if (endereco != null && !endereco.trim().isEmpty()) {
                        enderecoField.setText(endereco);
                    }
                    
                    System.out.println("Dados do usuário preenchidos automaticamente");
                    
                    SwingUtilities.invokeLater(() -> {
                        adicionarListenersValidacao();
                        verificarEstadoBotao();
                    });
                } else {
                    System.out.println("Usuário não encontrado no banco de dados");
                    JOptionPane.showMessageDialog(this, 
                        "Erro: Usuário não encontrado no sistema!\nPor favor, faça login novamente.", 
                        "Erro de Usuário", 
                        JOptionPane.ERROR_MESSAGE);
                    SessaoUsuario.logout();
                    dispose();
                    new FormularioPedido(carrinho);
                }
            } catch (SQLException e) {
                System.err.println("Erro ao carregar dados do usuário: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void bloquearCamposFormulario() {
        Component[] components = {nomeCompletoField, enderecoField, telefoneField, cpfField, emailField};
        for (Component comp : components) {
            comp.setEnabled(false);
            comp.setBackground(new Color(240, 240, 240));
        }
    }

    private void adicionarListenersValidacao() {
        ActionListener verificarCamposListener = e -> verificarEstadoBotao();

        Component[] fields = {nomeCompletoField, enderecoField, telefoneField, cpfField, emailField};
        for (Component field : fields) {
            if (field instanceof JTextField) {
                ((JTextField) field).addActionListener(verificarCamposListener);
            }
        }
        
        javax.swing.event.DocumentListener documentListener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { verificarEstadoBotao(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { verificarEstadoBotao(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { verificarEstadoBotao(); }
        };
        
        ((JTextField) nomeCompletoField).getDocument().addDocumentListener(documentListener);
        ((JTextField) enderecoField).getDocument().addDocumentListener(documentListener);
        ((JTextField) emailField).getDocument().addDocumentListener(documentListener);
        
        telefoneField.addPropertyChangeListener("value", e -> verificarEstadoBotao());
        cpfField.addPropertyChangeListener("value", e -> verificarEstadoBotao());
    }

    private void verificarEstadoBotao() {
        boolean camposPreenchidos = validarCampos();
        boolean carrinhoPreenchido = !carrinho.isEmpty();
        finalizarCompraButton.setEnabled(usuarioLogado && camposPreenchidos && carrinhoPreenchido);
        finalizarCompraButton.setBackground(
            usuarioLogado && camposPreenchidos && carrinhoPreenchido ? buttonColor : Color.GRAY
        );
    }

    private void abrirTelaLogin() {
        int option = JOptionPane.showConfirmDialog(this, 
            "Para finalizar a compra, você precisa fazer login ou cadastro.\nDeseja ir para a tela de login agora?", 
            "Login Necessário", 
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
            new TelaLogin();
        }
    }

    private void finalizarPedido() {
        if (!validarCampos()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos corretamente antes de finalizar o pedido.", 
                "Campos Incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Seu carrinho está vazio!\nAdicione itens antes de finalizar o pedido.", 
                "Carrinho Vazio", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Exception emailException = null;
        
        try {
            
            salvarPedidoNoBanco();
            
            String destinatario = emailField.getText().trim();
            String codigoPedido = codigoPedidoLabel.getText().replace("Código: ", "");
            String nomeCliente = nomeCompletoField.getText().trim();
            double precoTotal = calcularPrecoFinal();
            
            // Tentar enviar email
            try {
                enviarEmailConfirmacao(destinatario, nomeCliente, codigoPedido, precoTotal);
            } catch (Exception e) {
                emailException = e;
                System.err.println("Erro no envio de email, mas pedido foi salvo: " + e.getMessage());
            }

            String mensagemEmail = (emailException == null) ? 
                "Email de confirmação enviado para: " + destinatario : 
                "Erro no envio do email, mas pedido foi salvo com sucesso!";
                
            JOptionPane.showMessageDialog(this, 
                "✅ Pedido realizado com sucesso!\n\n" +
                "Código do pedido: " + codigoPedido + "\n" +
                "Valor total: " + df.format(precoTotal) + "\n" +
                "📧 " + mensagemEmail + "\n\n" +
                "Agradecemos pela sua compra!", 
                "Pedido Finalizado", 
                JOptionPane.INFORMATION_MESSAGE);
            
            limparCarrinho();
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao finalizar pedido:\n" + e.getMessage(), 
                "Erro no Pedido", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enviarEmailConfirmacao(String destinatario, String nomeCliente, String codigoPedido, double precoTotal) {
        try {
            StringBuilder detalhesPedido = new StringBuilder();
            detalhesPedido.append("<h3>Detalhes do seu Pedido:</h3>");
            detalhesPedido.append("<p><strong>Código do Pedido:</strong> ").append(codigoPedido).append("</p>");
            detalhesPedido.append("<p><strong>Cliente:</strong> ").append(nomeCliente).append("</p>");
            detalhesPedido.append("<p><strong>Valor Total:</strong> ").append(df.format(precoTotal)).append("</p>");
            
            detalhesPedido.append("<h4>Itens do Pedido:</h4>");
            detalhesPedido.append("<ul>");
            for (ItemCarrinho item : carrinho) {
                detalhesPedido.append("<li>")
                             .append(item.getProduto().getDescricao())
                             .append(" - Quantidade: ").append(item.getQuantidade())
                             .append(" - Preço: ").append(df.format(item.calcularPrecoTotal()))
                             .append("</li>");
            }
            detalhesPedido.append("</ul>");
            
           
            EmailSender.enviarEmailComDetalhes(destinatario, nomeCliente, detalhesPedido.toString(), precoTotal);
            
            System.out.println("✅ Email enviado com sucesso para: " + destinatario);
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar email: " + e.getMessage());
            throw new RuntimeException("Falha no envio do email: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        return !nomeCompletoField.getText().trim().isEmpty() &&
               !enderecoField.getText().trim().isEmpty() &&
               !telefoneField.getText().trim().contains("_") &&
               !cpfField.getText().trim().contains("_") &&
               emailField.getText().trim().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private void salvarPedidoNoBanco() {
        try {
            int idUsuario = BancoDeDados.obterIdUsuarioPorEmail(emailUsuarioLogado);
            String codigoPedido = codigoPedidoLabel.getText().replace("Código: ", "");
            double precoTotal = calcularPrecoFinal();
            
            boolean pedidoSalvo = BancoDeDados.salvarPedido(
                codigoPedido, 
                idUsuario, 
                nomeCompletoField.getText().trim(),
                enderecoField.getText().trim(),
                telefoneField.getText().trim(),
                cpfField.getText().trim(),
                emailField.getText().trim(),
                precoTotal
            );
            
            if (pedidoSalvo) {
                BancoDeDados.salvarItensPedido(codigoPedido, carrinho);
                System.out.println("✅ Pedido salvo no banco: " + codigoPedido);
            } else {
                throw new RuntimeException("Falha ao salvar pedido no banco");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar pedido: " + e.getMessage());
        }
    }

    private void limparCarrinho() {
        carrinho.clear();
        if (usuarioLogado) {
            int idUsuario = BancoDeDados.obterIdUsuarioPorEmail(emailUsuarioLogado);
            if (idUsuario != -1) {
                BancoDeDados.limparCarrinhoUsuario(idUsuario);
            }
        }
    }

    private double calcularPrecoFinal() {
        double total = 0;
        for (ItemCarrinho item : carrinho) {
            total += item.calcularPrecoTotal();
        }
        return total;
    }

    private String gerarCodigoPedido() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return codigo.toString();
    }

   
    private void testarEnvioEmail() {
        try {
            String testeDestinatario = "seuemail@gmail.com"; 
            String testeNome = "Cliente Teste";
            String testeDetalhes = "<p><strong>Pedido Teste:</strong> #TEST123</p>" +
                                  "<ul><li>Livro Teste - Quantidade: 1 - Preço: R$ 29,90</li></ul>";
            double testeTotal = 29.90;
            
            EmailSender.enviarEmailComDetalhes(testeDestinatario, testeNome, testeDetalhes, testeTotal);
            JOptionPane.showMessageDialog(this, "Email de teste enviado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro no teste de email: " + e.getMessage());
        }
    }
}