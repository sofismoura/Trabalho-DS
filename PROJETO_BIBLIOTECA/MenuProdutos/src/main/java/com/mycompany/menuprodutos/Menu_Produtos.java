/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.text.DecimalFormat;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.net.URL;
import javax.imageio.ImageIO;
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
import novoPack.BancoDeDados;
import com.mycompany.menuprodutos.SessaoUsuario;
import static com.mysql.cj.protocol.x.XProtocolDecoder.instance;
import java.awt.image.BufferedImage;
import java.awt.geom.Ellipse2D;
import java.awt.BasicStroke;

public class Menu_Produtos extends JFrame {

    private JPanel produtosPanelContainer;
    private JPanel produtosGridPanel;
    private JScrollPane scrollPane;
    private ArrayList<Produto> produtos;
    private RoundedTextField campoPesquisa;
    private String categoriaAtual = "HOME";
    private String generoAtual = null; 
    private JPanel generoPanel; 
    private JLabel contaLabel;
    private JButton btnAtualizar;
    private Timer timerSincronizacao;
    private static Menu_Produtos instance;
    ArrayList<ItemCarrinho> carrinho = new ArrayList<>();
    
public Menu_Produtos() {
    setTitle("Livraria Entre Palavras");
    setResizable(false);
    instance = this; 

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    Rectangle bounds = gd.getDefaultConfiguration().getBounds();
    Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());

    int width = bounds.width - insets.left - insets.right;
    int height = bounds.height - insets.top - insets.bottom;

    setSize(width, height);
    setLocation(insets.left, insets.top);
   
    ImageIcon icon = new ImageIcon("src/assets/images/Logo.png");
    setIconImage(icon.getImage());

    setLayout(new BorderLayout());

    produtos = new ArrayList<>();
    carrinho = new ArrayList<>();
    
    produtosPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
    produtosPanelContainer.setBackground(new Color(244, 240, 230));

    produtosGridPanel = new JPanel(new GridLayout(0, 5, 20, 20));
    produtosGridPanel.setBackground(new Color(244, 240, 230));
    produtosGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    produtosPanelContainer.add(produtosGridPanel);
    
    System.out.println("=== INICIANDO VERIFICAÇÃO DE SINCRONIZAÇÃO ===");
    BancoDeDados.verificarSincronizacaoProdutos();
    
    carregarProdutos();

    JPanel carrossel = criarCarrossel();
    
    JPanel mainContentPanel = new JPanel(new BorderLayout());
    mainContentPanel.setBackground(new Color(244, 240, 230));

    
    mainContentPanel.add(carrossel, BorderLayout.NORTH);
    mainContentPanel.add(produtosPanelContainer, BorderLayout.CENTER);

  
    scrollPane = new JScrollPane(mainContentPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.getVerticalScrollBar().setBlockIncrement(160);

   
    JPanel header = criarHeader();
    add(header, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);

    carregarProdutos();

    if (SessaoUsuario.isLogado()) {
        ArrayList<ItemCarrinho> carrinhoSalvo = BancoDeDados.carregarCarrinho(SessaoUsuario.getIdUsuario());
        if (carrinhoSalvo != null && !carrinhoSalvo.isEmpty()) {
            carrinho.clear();
            carrinho.addAll(carrinhoSalvo);
            System.out.println("Carrinho carregado do banco para " + SessaoUsuario.getNomeUsuario() + 
                             " com " + carrinho.size() + " itens");
            
            JOptionPane.showMessageDialog(this, 
                "Seu carrinho foi carregado com " + carrinho.size() + " itens!",
                "Carrinho Recuperado",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
     inicializarSincronizacao();
    atualizarExibicaoProdutos();
}

   private boolean verificarMudancas(ArrayList<Produto> novosProdutos) {
        System.out.println("🔍 Verificando mudanças no banco...");
        
        boolean mudou = false;
        
        if (produtos.size() != novosProdutos.size()) {
            System.out.println("📊 Mudança detectada: número de produtos alterado de " + 
                             produtos.size() + " para " + novosProdutos.size());
            mudou = true;
        } else {
           
            for (Produto novoProduto : novosProdutos) {
                boolean encontrado = false;
                for (Produto produtoAntigo : produtos) {
                    if (produtoAntigo.getId() == novoProduto.getId()) {
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    System.out.println("📊 Mudança detectada: produto ID " + novoProduto.getId() + " adicionado/removido");
                    mudou = true;
                    break;
                }
            }
            
          
            for (Produto produtoAntigo : produtos) {
                boolean aindaExiste = false;
                for (Produto novoProduto : novosProdutos) {
                    if (produtoAntigo.getId() == novoProduto.getId()) {
                        aindaExiste = true;
                        break;
                    }
                }
                if (!aindaExiste) {
                    System.out.println("📊 Mudança detectada: produto ID " + produtoAntigo.getId() + " foi removido");
                    mudou = true;
                    break;
                }
            }
        }
        
        return mudou;
 }

 public void atualizarManual() {
     System.out.println("🔍 Atualização manual solicitada...");
     
     try {
     
         ArrayList<Produto> produtosAtuais = BancoDeDados.carregarProdutosDoBanco();
         
         System.out.println("📦 Produtos carregados do banco: " + produtosAtuais.size());
         
        
         produtos.clear();
         produtos.addAll(produtosAtuais);
         
       
         SwingUtilities.invokeLater(() -> {
             atualizarExibicaoProdutos();
             System.out.println("✅ Menu atualizado manualmente com sucesso");
             
           
             JOptionPane.showMessageDialog(this, 
                 "Menu atualizado com sucesso!\n" + produtos.size() + " produtos sincronizados com o banco de dados.",
                 "Atualização Concluída", 
                 JOptionPane.INFORMATION_MESSAGE);
         });
     } catch (Exception e) {
         System.err.println("❌ Erro na atualização manual: " + e.getMessage());
         JOptionPane.showMessageDialog(this, 
             "Erro ao atualizar produtos: " + e.getMessage(),
             "Erro na Atualização", 
             JOptionPane.ERROR_MESSAGE);
     }
 }

 public void sincronizarComBanco() {
     System.out.println("🔄 Iniciando sincronização com banco...");
     
     try {
        
         ArrayList<Produto> produtosAtuais = BancoDeDados.carregarProdutosDoBanco();
         
         
         boolean mudou = verificarMudancas(produtosAtuais);
         
         if (mudou) {
             System.out.println("📦 Detectadas mudanças no banco, atualizando menu...");
             
          
             produtos.clear();
             produtos.addAll(produtosAtuais);
             
            
             SwingUtilities.invokeLater(() -> {
                 atualizarExibicaoProdutos();
                 System.out.println("✅ Menu atualizado com sucesso");
             });
         } else {
             System.out.println("✅ Banco sincronizado - sem mudanças");
         }
         
     } catch (Exception e) {
         System.err.println("❌ Erro na sincronização: " + e.getMessage());
     }
 }

private void inicializarSincronizacao() {
      
        timerSincronizacao = new Timer(30000, e -> {
            sincronizarComBanco();
        });
        timerSincronizacao.start();
        
        System.out.println("✅ Sistema de sincronização automática iniciado");
    }
    
    public static Menu_Produtos getInstance() {
        return instance;
    }

private JPanel criarHeader() {
    JPanel panel = new JPanel();
    Color navbarColor = new Color(115, 103, 47);
    panel.setBackground(navbarColor);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JPanel topRowPanel = new JPanel(new BorderLayout());
    topRowPanel.setBackground(navbarColor);
    topRowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, topRowPanel.getPreferredSize().height));

    JPanel logoPanel = new JPanel();
    logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    logoPanel.setBackground(navbarColor);
    
    String imagePath = "src/assets/images/Logo.png";
    ImageIcon logoIcon = new ImageIcon(imagePath);
    Image image = logoIcon.getImage();
    Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    logoIcon = new ImageIcon(resizedImage);
    
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(navbarColor);

        
        btnAtualizar = new JButton("🔄 Atualizar");
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBackground(new Color(65, 105, 225)); // Azul
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFont(new Font("Serif", Font.BOLD, 14));
        btnAtualizar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 70, 180), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        btnAtualizar.setToolTipText("Sincronizar com banco de dados");
        btnAtualizar.addActionListener(e -> atualizarManual());
        
        btnAtualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAtualizar.setBackground(new Color(30, 70, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAtualizar.setBackground(new Color(65, 105, 225));
            }
        });

        rightPanel.add(btnAtualizar);
        rightPanel.add(Box.createHorizontalStrut(10));

        JButton btnSair = new JButton("Sair");
        btnSair.setFocusPainted(false);
        btnSair.setBackground(new Color(200, 0, 0));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Serif", Font.BOLD, 16));
        btnSair.addActionListener(e -> System.exit(0));
        rightPanel.add(btnSair);

    JLabel imageLabel = new JLabel(logoIcon);
    JLabel textoLabel = new JLabel("<html><b>Livraria<br>Entre Palavras</b></html>");
    textoLabel.setFont(new Font("Serif", Font.BOLD, 18));
    textoLabel.setForeground(Color.WHITE);

    logoPanel.add(imageLabel);
    logoPanel.add(Box.createHorizontalStrut(10));
    logoPanel.add(textoLabel);

    topRowPanel.add(logoPanel, BorderLayout.WEST);

    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    searchPanel.setBackground(navbarColor);
    searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    campoPesquisa = new RoundedTextField(40);
    campoPesquisa.setFont(new Font("Serif", Font.PLAIN, 18));
    campoPesquisa.setPreferredSize(new Dimension(350, 35));
    campoPesquisa.setMaximumSize(new Dimension(400, 40));
    campoPesquisa.setBorderColor(Color.BLACK);
    campoPesquisa.setCornerRadius(20);

    searchPanel.add(campoPesquisa);
    JLabel lupaIcon = new JLabel("🔍");
    lupaIcon.setFont(new Font("Serif", Font.PLAIN, 20));
    searchPanel.add(lupaIcon);
    searchPanel.add(Box.createHorizontalStrut(105));

    campoPesquisa.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            atualizarExibicaoProdutos();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            atualizarExibicaoProdutos();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            atualizarExibicaoProdutos();
        }
    });
    topRowPanel.add(searchPanel, BorderLayout.CENTER);
    
    System.out.println("DEBUG: Header criado, campoPesquisa é nulo? " + (campoPesquisa == null));

    JPanel carrossel = criarCarrossel();
    
   
    JPanel mainContentPanel = new JPanel(new BorderLayout());
    mainContentPanel.setBackground(new Color(244, 240, 230));

    mainContentPanel.add(carrossel, BorderLayout.NORTH);

   
    produtosPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
    produtosPanelContainer.setBackground(new Color(244, 240, 230));

    produtosGridPanel = new JPanel(new GridLayout(0, 5, 20, 20));
    produtosGridPanel.setBackground(new Color(244, 240, 230));
    produtosGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    produtosPanelContainer.add(produtosGridPanel);

 
    mainContentPanel.add(produtosPanelContainer, BorderLayout.CENTER);

    scrollPane = new JScrollPane(mainContentPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.getVerticalScrollBar().setBlockIncrement(160);

    add(scrollPane, BorderLayout.CENTER);

    if (SessaoUsuario.isLogado()) {
        ArrayList<ItemCarrinho> carrinhoSalvo = BancoDeDados.carregarCarrinho(SessaoUsuario.getIdUsuario());
        if (carrinhoSalvo != null && !carrinhoSalvo.isEmpty()) {
            carrinho.clear(); // Limpa o carrinho atual
            carrinho.addAll(carrinhoSalvo); // Adiciona os itens salvos
            System.out.println("Carrinho carregado do banco para " + SessaoUsuario.getNomeUsuario() + 
                             " com " + carrinho.size() + " itens");
        }
    }

    carregarProdutos();
    atualizarExibicaoProdutos();
    
    System.out.println("DEBUG: Construtor finalizado, campoPesquisa é nulo? " + (campoPesquisa == null));

   
    contaLabel = new JLabel();
atualizarIconePerfil(); 

contaLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
contaLabel.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
     
        if (SessaoUsuario.isLogado()) {
          
            TelaPerfil telaPerfil = new TelaPerfil();
            telaPerfil.setVisible(true);
        } else {
           
            TelaInicial telaInicial = new TelaInicial();
            telaInicial.setVisible(true);
        }
    }
});
rightPanel.add(Box.createHorizontalStrut(10));
rightPanel.add(contaLabel);

    String carrinhoImagePath = "src/assets/images/carrinho_branco.png";
    ImageIcon carrinhoIcon = new ImageIcon(carrinhoImagePath);
    if (carrinhoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
        Image carrinhoImage = carrinhoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        carrinhoIcon = new ImageIcon(carrinhoImage);
        JLabel carrinhoLabel = new JLabel(carrinhoIcon);
        carrinhoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        carrinhoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuDoCarrinho carrinhoJanela = new MenuDoCarrinho(carrinho);
                carrinhoJanela.setVisible(true);
            }
        });
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(carrinhoLabel);
    } else {
        JLabel carrinhoLabel = new JLabel("🛒");
        carrinhoLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        carrinhoLabel.setForeground(Color.WHITE);
        carrinhoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        carrinhoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuDoCarrinho carrinhoJanela = new MenuDoCarrinho(carrinho);
                carrinhoJanela.setVisible(true);
            }
        });
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(carrinhoLabel);
    }
    
    topRowPanel.add(rightPanel, BorderLayout.EAST);

  
    JPanel bottomRowPanel = new JPanel();
    bottomRowPanel.setLayout(new BoxLayout(bottomRowPanel, BoxLayout.Y_AXIS));
    bottomRowPanel.setBackground(navbarColor);

  
    JPanel menu = new JPanel(new FlowLayout(FlowLayout.CENTER));
    menu.setBackground(navbarColor);

    generoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    generoPanel.setBackground(new Color(89, 30, 25, 1));
    generoPanel.setVisible(false);

    String[] categorias = {"HOME", "LIVROS", "CD", "DVD"};

    for (String categoria : categorias) {
        JButton botao = new JButton(categoria);
        botao.setFocusPainted(false);
        botao.setBackground(new Color(210, 180, 140));
        botao.setForeground(Color.BLACK);
        botao.setFont(new Font("Serif", Font.BOLD, 16));
        botao.addActionListener(e -> {
            categoriaAtual = botao.getText();
            generoAtual = null;
            campoPesquisa.setText("");
            atualizarExibicaoProdutos();
            generoPanel.setVisible(categoriaAtual.equals("LIVROS") || categoriaAtual.equals("CD") || categoriaAtual.equals("DVD"));
            atualizarBotoesGenero();
            bottomRowPanel.revalidate();
            bottomRowPanel.repaint();
        });
        menu.add(botao);
    }

    bottomRowPanel.add(menu);
    bottomRowPanel.add(generoPanel);

    panel.add(topRowPanel);
    panel.add(bottomRowPanel);

    return panel;
}

    @Override
    public void dispose() {
        if (timerSincronizacao != null) {
            timerSincronizacao.stop();
            System.out.println("⏹️ Sincronização automática parada");
        }
        super.dispose();
    }


public void atualizarIconePerfil() {
    SwingUtilities.invokeLater(() -> {
        try {
            ImageIcon contaIcon = carregarImagemPerfilUsuario();
            if (contaIcon != null) {
                contaLabel.setIcon(contaIcon);
                contaLabel.setText(""); 
                contaLabel.setToolTipText("Perfil de " + SessaoUsuario.getNomeUsuario());
                System.out.println("Ícone do perfil atualizado com sucesso");
            } else {
                contaLabel.setIcon(null);
                contaLabel.setText("👤");
                contaLabel.setFont(new Font("Serif", Font.PLAIN, 24)); 
                contaLabel.setForeground(Color.WHITE);
                contaLabel.setToolTipText("Perfil de " + SessaoUsuario.getNomeUsuario());
                System.out.println("Usando fallback de emoji para ícone do perfil");
            }
            contaLabel.revalidate();
            contaLabel.repaint();
        } catch (Exception e) {
            System.err.println("Erro ao atualizar ícone do perfil: " + e.getMessage());
        }
    });
}

private ImageIcon carregarImagemPerfilUsuario() {
    try {
       
        if (!SessaoUsuario.isLogado()) {
            System.out.println("Usuário não logado - carregando imagem padrão");
            return carregarImagemPadrao();
        }
        
     
        int idUsuario = SessaoUsuario.getIdUsuario();
        if (idUsuario == -1) {
            System.out.println("ID do usuário não encontrado - carregando imagem padrão");
            return carregarImagemPadrao();
        }
        
        System.out.println("Carregando imagem do perfil para usuário ID: " + idUsuario);
        
      
        byte[] imagemBytes = BancoDeDados.carregarImagemPerfil(idUsuario);
        
        if (imagemBytes != null && imagemBytes.length > 0) {
            System.out.println("Imagem personalizada encontrada, tamanho: " + imagemBytes.length + " bytes");
           
            ImageIcon imagemOriginal = new ImageIcon(imagemBytes);
            Image imagemRedimensionada = imagemOriginal.getImage()
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            
            return new ImageIcon(imagemRedimensionada);
        } else {
            System.out.println("Nenhuma imagem personalizada encontrada - carregando imagem padrão");
          
            return carregarImagemPadrao();
        }
        
    } catch (Exception e) {
        System.err.println("Erro ao carregar imagem do perfil: " + e.getMessage());
        e.printStackTrace();
        return carregarImagemPadrao();
    }
}

private ImageIcon carregarImagemPadrao() {
    try {
        String contaImagePath = "src/assets/images/imagem_conta.png";
        ImageIcon contaIcon = new ImageIcon(contaImagePath);
        if (contaIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            System.out.println("Carregando imagem padrão do perfil");
           
            Image contaImage = contaIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            return new ImageIcon(contaImage);
        } else {
            System.err.println("Imagem padrão não encontrada: " + contaImagePath);
            return criarIconePadrao();
        }
    } catch (Exception e) {
        System.err.println("Erro ao carregar imagem padrão: " + e.getMessage());
        return criarIconePadrao();
    }
}

private ImageIcon criarIconePadrao() {
    System.out.println("Criando ícone padrão com emoji");
    // Cria uma imagem com o emoji
    BufferedImage bi = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = bi.createGraphics();
    
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(new Color(115, 103, 47)); // Cor de fundo
    g2.fillOval(0, 0, 40, 40);
    
    g2.setColor(Color.WHITE);
    g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
    g2.drawString("👤", 10, 28);
    
    g2.dispose();
    return new ImageIcon(bi);
}
private Image criarImagemCircular(Image imagemOriginal, int largura, int altura) {
    try {
        BufferedImage bi = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        
        Shape clip = new Ellipse2D.Float(0, 0, largura, altura);
        g2.setClip(clip);
        g2.drawImage(imagemOriginal, 0, 0, largura, altura, null);
        
       
        g2.setClip(null);
        
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(1, 1, largura - 2, altura - 2);

        g2.dispose();
        return bi;
    } catch (Exception e) {
        System.err.println("Erro ao criar imagem circular: " + e.getMessage());
        return imagemOriginal; 
    }
}
 private JPanel criarCarrossel() {
    JPanel carrosselPanel = new JPanel();
    carrosselPanel.setBackground(new Color(244, 240, 230));
    carrosselPanel.setLayout(new BorderLayout());
    
    carrosselPanel.setPreferredSize(new Dimension(1100, 350)); 
    
    JLabel tituloLabel = new JLabel("🎉 Destaques da Semana 🎉");
    tituloLabel.setFont(new Font("Serif", Font.BOLD, 24));
    tituloLabel.setForeground(new Color(115, 103, 47));
    tituloLabel.setHorizontalAlignment(JLabel.CENTER);
    tituloLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
    
   
    JPanel mainCarouselPanel = new JPanel(new BorderLayout());
    mainCarouselPanel.setBackground(new Color(244, 240, 230));
    
   
    JPanel centerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
    centerContainer.setBackground(new Color(244, 240, 230));
    
   
    JPanel contentPanel = new JPanel(new BorderLayout(20, 0)); 
    contentPanel.setBackground(new Color(255, 255, 255));
    contentPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(115, 103, 47), 2),
        BorderFactory.createEmptyBorder(25, 25, 25, 25)
    ));
    contentPanel.setPreferredSize(new Dimension(800, 200)); 
    
    // PAINEL DA IMAGEM - CENTRALIZADO
    JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    imagePanel.setBackground(Color.WHITE);
    imagePanel.setPreferredSize(new Dimension(250, 150)); 
    
    JLabel imagemLabel = new JLabel();
    imagemLabel.setHorizontalAlignment(JLabel.CENTER);
    imagemLabel.setVerticalAlignment(JLabel.CENTER);
    imagemLabel.setBorder(BorderFactory.createLineBorder(new Color(210, 180, 140), 1));
    
  
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setBackground(Color.WHITE);
    infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    infoPanel.setPreferredSize(new Dimension(450, 150)); 
    
    JLabel tituloProdutoLabel = new JLabel();
    tituloProdutoLabel.setFont(new Font("Serif", Font.BOLD, 20));
    tituloProdutoLabel.setForeground(new Color(115, 103, 47));
    tituloProdutoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    tituloProdutoLabel.setHorizontalAlignment(JLabel.CENTER);
    
    JLabel tipoProdutoLabel = new JLabel();
    tipoProdutoLabel.setFont(new Font("Serif", Font.ITALIC, 14));
    tipoProdutoLabel.setForeground(Color.GRAY);
    tipoProdutoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    tipoProdutoLabel.setHorizontalAlignment(JLabel.CENTER);
    
    JLabel precoProdutoLabel = new JLabel();
    precoProdutoLabel.setFont(new Font("Serif", Font.BOLD, 18));
    precoProdutoLabel.setForeground(new Color(60, 100, 60));
    precoProdutoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
    precoProdutoLabel.setHorizontalAlignment(JLabel.CENTER);
    
    infoPanel.add(Box.createVerticalGlue()); 
    infoPanel.add(tituloProdutoLabel);
    infoPanel.add(Box.createVerticalStrut(10));
    infoPanel.add(tipoProdutoLabel);
    infoPanel.add(Box.createVerticalStrut(20));
    infoPanel.add(precoProdutoLabel);
    infoPanel.add(Box.createVerticalGlue()); 
    
   
    imagePanel.add(imagemLabel);
    
   
    contentPanel.add(imagePanel, BorderLayout.WEST);
    contentPanel.add(infoPanel, BorderLayout.CENTER);
    
    
    centerContainer.add(contentPanel);
    
  
    JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
    navigationPanel.setBackground(new Color(244, 240, 230));
    
    JButton btnAnterior = criarBotaoNavegacao("◀ Anterior");
    JButton btnProximo = criarBotaoNavegacao("Próximo ▶");
    
   
    JPanel dotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
    dotsPanel.setBackground(new Color(244, 240, 230));
    
    navigationPanel.add(btnAnterior);
    navigationPanel.add(dotsPanel);
    navigationPanel.add(btnProximo);
    
   
    mainCarouselPanel.add(centerContainer, BorderLayout.CENTER);
    mainCarouselPanel.add(navigationPanel, BorderLayout.SOUTH);
    
    carrosselPanel.add(tituloLabel, BorderLayout.NORTH);
    carrosselPanel.add(mainCarouselPanel, BorderLayout.CENTER);
   
    ArrayList<Produto> produtosCarrossel = new ArrayList<>();
    Random random = new Random();
    
    while (produtosCarrossel.size() < 6 && produtosCarrossel.size() < produtos.size()) {
        Produto produtoAleatorio = produtos.get(random.nextInt(produtos.size()));
        if (!produtosCarrossel.contains(produtoAleatorio)) {
            produtosCarrossel.add(produtoAleatorio);
        }
    }
    
    final int[] indiceAtual = {0};
    final int totalProdutos = produtosCarrossel.size();
    
   
    for (int i = 0; i < totalProdutos; i++) {
        JLabel dot = new JLabel("○");
        dot.setFont(new Font("Serif", Font.PLAIN, 20));
        dot.setForeground(new Color(115, 103, 47));
        dot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        final int index = i;
        dot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                indiceAtual[0] = index;
                
            }
        });
        dotsPanel.add(dot);
    }
    Runnable atualizarCarrossel = () -> {
        if (!produtosCarrossel.isEmpty() && indiceAtual[0] < produtosCarrossel.size()) {
            Produto produtoAtual = produtosCarrossel.get(indiceAtual[0]);
            
           
            tituloProdutoLabel.setText(produtoAtual.getDescricao());
            
            String tipo = produtoAtual.getClass().getSimpleName();
            tipoProdutoLabel.setText(tipo + " • " + produtoAtual.getGenero());
            
            DecimalFormat df = new DecimalFormat("R$ #,##0.00");
            precoProdutoLabel.setText(df.format(produtoAtual.getPreco()));
            
            try {
                String caminho = produtoAtual.getCaminhoImagem();
                ImageIcon icon = new ImageIcon(caminho);
                Image image = icon.getImage();
                
               
                int maxWidth = 250;
                int maxHeight = 140;
                int width = image.getWidth(null);
                int height = image.getHeight(null);
                
                if (width > maxWidth || height > maxHeight) {
                    double scale = Math.min((double) maxWidth / width, (double) maxHeight / height);
                    width = (int) (width * scale);
                    height = (int) (height * scale);
                }
                
                Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imagemLabel.setIcon(new ImageIcon(resizedImage));
                
            } catch (Exception ex) {
                System.err.println("Erro ao carregar imagem: " + produtoAtual.getCaminhoImagem());
                imagemLabel.setText("<html><center>📷<br>Imagem não disponível</center></html>");
                imagemLabel.setFont(new Font("Serif", Font.ITALIC, 14));
                imagemLabel.setForeground(Color.GRAY);
            }
            
            // Atualizar dots indicadores
            Component[] dots = dotsPanel.getComponents();
            for (int i = 0; i < dots.length; i++) {
                JLabel dot = (JLabel) dots[i];
                if (i == indiceAtual[0]) {
                    dot.setText("●");
                    dot.setForeground(new Color(210, 180, 140));
                } else {
                    dot.setText("○");
                    dot.setForeground(new Color(115, 103, 47));
                }
            }
        }
    };
    
   
    btnAnterior.addActionListener(e -> {
        indiceAtual[0] = (indiceAtual[0] - 1 + totalProdutos) % totalProdutos;
        atualizarCarrossel.run();
    });
    
    btnProximo.addActionListener(e -> {
        indiceAtual[0] = (indiceAtual[0] + 1) % totalProdutos;
        atualizarCarrossel.run();
    });
    
   
    Timer timer = new Timer(6000, e -> {
        indiceAtual[0] = (indiceAtual[0] + 1) % totalProdutos;
        atualizarCarrossel.run();
    });
    timer.start();
    
  
    if (!produtosCarrossel.isEmpty()) {
        atualizarCarrossel.run();
    }
    
    return carrosselPanel;
}

private JButton criarBotaoNavegacao(String texto) {
    JButton botao = new JButton(texto);
    botao.setFont(new Font("Serif", Font.BOLD, 14));
    botao.setBackground(new Color(115, 103, 47));
    botao.setForeground(Color.WHITE);
    botao.setFocusPainted(false);
    botao.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(89, 79, 35), 1),
        BorderFactory.createEmptyBorder(8, 15, 8, 15)
    ));
    botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
  
    botao.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            botao.setBackground(new Color(89, 79, 35));
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            botao.setBackground(new Color(115, 103, 47));
        }
    });
    
    return botao;
}

    private void atualizarBotoesGenero() {
        generoPanel.removeAll();
        if (categoriaAtual.equals("LIVROS")) {
            HashSet<String> generosLivros = new HashSet<>();
            for (Produto produto : produtos) {
                if (produto instanceof Livro) {
                    generosLivros.add(((Livro) produto).getGenero());
                }
            }
            for (String genero : generosLivros) {
                JButton botaoGenero = new JButton(genero);
                botaoGenero.setFocusPainted(false);
                botaoGenero.setBackground(new Color(230, 220, 190));
                botaoGenero.setForeground(Color.BLACK);
                botaoGenero.setFont(new Font("Serif", Font.PLAIN, 14));
                botaoGenero.addActionListener(e -> {
                    generoAtual = botaoGenero.getText();
                    campoPesquisa.setText("");
                    atualizarExibicaoProdutos();
                });
                generoPanel.add(botaoGenero);
            }
        } else if (categoriaAtual.equals("CD")) {
            HashSet<String> generosCds = new HashSet<>();
            for (Produto produto : produtos) {
                if (produto instanceof Cd) {
                    generosCds.add(((Cd) produto).getGenero());
                }
            }
            for (String genero : generosCds) {
                JButton botaoGenero = new JButton(genero);
                botaoGenero.setFocusPainted(false);
                botaoGenero.setBackground(new Color(230, 220, 190));
                botaoGenero.setForeground(Color.BLACK);
                botaoGenero.setFont(new Font("Serif", Font.PLAIN, 14));
                botaoGenero.addActionListener(e -> {
                    generoAtual = botaoGenero.getText();
                    campoPesquisa.setText("");
                    atualizarExibicaoProdutos();
                });
                generoPanel.add(botaoGenero);
            }
        } else if (categoriaAtual.equals("DVD")) {
            HashSet<String> generosDvds = new HashSet<>();
            for (Produto produto : produtos) {
                if (produto instanceof Dvd) {
                    generosDvds.add(((Dvd) produto).getGenero());
                }
            }
            for (String genero : generosDvds) {
                JButton botaoGenero = new JButton(genero);
                botaoGenero.setFocusPainted(false);
                botaoGenero.setBackground(new Color(230, 220, 190));
                botaoGenero.setForeground(Color.BLACK);
                botaoGenero.setFont(new Font("Serif", Font.PLAIN, 14));
                botaoGenero.addActionListener(e -> {
                    generoAtual = botaoGenero.getText();
                    campoPesquisa.setText("");
                    atualizarExibicaoProdutos();
                });
                generoPanel.add(botaoGenero);
            }
        }
        generoPanel.revalidate();
        generoPanel.repaint();
    }


private JPanel criarCard(Produto produto) {
    return new RoundedCardPanel(produto, Menu_Produtos.this); 
}

 public void adicionarAoCarrinho(Produto produto) {
    boolean encontrado = false;
    for (ItemCarrinho item : carrinho) {
        if (item.getProduto().equals(produto)) { 
            item.incrementarQuantidade();
            encontrado = true;
            break;
        }
    }
    if (!encontrado) {
        carrinho.add(new ItemCarrinho(produto, 1));
    }
    System.out.println("DEBUG: Carrinho atual: " + carrinho); 
}


public void carregarProdutos() {
    System.out.println("DEBUG: Carregando produtos...");

    // Limpar lista atual
    produtos.clear();

    ArrayList<Produto> produtosDoBanco = BancoDeDados.carregarProdutosDoBanco();
    produtos.addAll(produtosDoBanco);

    System.out.println("DEBUG: " + produtosDoBanco.size() + " produtos carregados do banco");

  
    if (produtos.isEmpty()) {
        System.out.println("DEBUG: Nenhum produto no banco, carregando produtos padrão...");
        carregarProdutosPadrao();
    }

    System.out.println("DEBUG: Total de produtos carregados: " + produtos.size());
    
    
    atualizarExibicaoProdutos();
}

public void atualizarProdutos() {
    carregarProdutos();
    atualizarExibicaoProdutos();
}

private void carregarProdutosPadrao() {
    System.out.println("DEBUG: Carregando produtos padrão do código...");
    
    // Livros (Categoria: "LIVROS")
    produtos.add(new Livro("A Biblioteca da Meia-Noite", "Ficção", 10, 69.90, "Matt Haig", "Editora XYZ", "1ª Edição", "src/assets/images/livro1.png", "Nora Seed encontra uma biblioteca entre a vida e a morte, com chances de viver outras vidas."));
    produtos.add(new Livro("A Empregada", "Suspense", 8, 40.20, "Freida McFadden", "Editora XYZ", "1ª Edição", "src/assets/images/livro2.png", "Um suspense psicológico com reviravoltas inesperadas em uma casa cheia de segredos."));
    produtos.add(new Livro("A Garota do Lago", "Mistério", 12, 25.90, "Charlie Donlea", "Editora ABC", "Edição de Luxo", "src/assets/images/livro3.png", "Um mistério envolvente na atmosfera sombria de um lago, investigado por uma jornalista."));
    produtos.add(new Livro("A Hipótese do Amor", "Romance", 14, 39.99, "Ali Hazelwood", "Editora LER", "2ª Edição", "src/assets/images/livro4.png", "Quando um falso namoro científico se torna uma atração real, as teorias sobre o amor são testadas."));
    produtos.add(new Livro("A Noiva", "Romance", 16, 49.90, "Ali Hazelwood", "Editora LER", "Edição Especial", "src/assets/images/livro5.png", "Uma perigosa aliança entre uma vampira e um lobisomem se transforma em um tórrido romance paranormal."));
    produtos.add(new Livro("A Revolução dos Bichos", "Fábula", 20, 24.20, "George Orwell", "Editora XYZ", "Edição de Bolso", "src/assets/images/livro6.png", "Uma alegoria satírica sobre o totalitarismo através da revolução dos animais em uma fazenda."));
    produtos.add(new Livro("A Sutil Arte de Ligar o F*da-se", "Autoajuda", 30, 35.00, "Mark Manson", "Editora Agora", "Edição Especial", "src/assets/images/livro7.png", "Um guia de autoajuda que confronta o pensamento positivo e ensina a focar no que importa."));
    produtos.add(new Livro("Assim que Acaba Assim que Começa", "Romance", 7, 150.00, "Colleen Hoover", "Editora Nova", "Primeira Edição", "src/assets/images/livro8.png", "Uma história de amor intensa e carregada de emoções conflitantes e difíceis decisões."));
    produtos.add(new Livro("Bíblia Sagrada", "Religião", 50, 50.00, "Diversos Autores", "Editora Record", "Edição Comemorativa", "src/assets/images/livro9.png", "Uma coletânea de livros sagrados que formam a base do cristianismo, com histórias e ensinamentos."));
    produtos.add(new Livro("Bobbie Goods - Para Pintar", "Arte", 15, 34.50, "Bobbie Goods", "Editora Art", "Edição de Arte", "src/assets/images/livro10.png", "Livros de colorir com ilustrações detalhadas, perfeitos para relaxar e estimular a criatividade artística."));
    produtos.add(new Livro("Café com Deus Pai", "Religião", 25, 53.15, "Junior Rostirola","Editora V&R", "Edição de Bolso", "src/assets/images/livro11.png", "Um devocional diário que oferece reflexões e mensagens para uma jornada espiritual transformadora ao longo do ano."));
    produtos.add(new Livro("Capitães da Areia", "Clássico", 22, 53.15, "Jorge Amado", "Editora Companhia", "Edição Popular", "src/assets/images/livro12.png", "Acompanhe a vida de Pedro Bala e outros garotos de rua que lutam para sobreviver em Salvador."));
    produtos.add(new Livro("Coleção Box do Harry Potter", "Fantasia", 10, 349.90, "J.K Rowling", "Editora Rocco", "Edição Limitada", "src/assets/images/livro13.png", "O box completo com os sete livros da inesquecível saga do bruxo Harry Potter."));
    produtos.add(new Livro("Coleção Percy Jackson", "Aventura", 18, 200.00, "Rick Riordan", "Editora Intrínseca", "Edição Completa", "src/assets/images/livro14.png", "Uma série de aventuras emocionantes que mistura a mitologia grega com o mundo moderno para jovens leitores."));
    produtos.add(new Livro("Dona Flor e seus 2 Maridos", "Romance", 12, 15.00, "Jorge Amado", "Editora Record", "Edição Popular", "src/assets/images/livro15.png", "Uma deliciosa comédia romântica ambientada na Bahia, sobre uma viúva dividida entre dois amores."));
    produtos.add(new Livro("Espreitador - Ordem Paranormal", "Terror", 14, 85.10, "Diversos Autores", "Editora Criativa", "Edição Especial", "src/assets/images/livro16.png", "Uma coleção de contos aterrorizantes que expandem o universo do fenômeno Ordem Paranormal."));
    produtos.add(new Livro("Extraordinário", "Drama", 11, 41.90, "R.J. Palacio", "Editora Intrínseca", "Primeira Edição", "src/assets/images/livro17.png", "A inspiradora jornada de Auggie Pullman ao ingressar na escola regular após anos de estudo em casa."));
    produtos.add(new Livro("Homem de Giz", "Mistério", 16, 39.90, "C.J. Tudor", "Editora Novo Conceito", "Edição Especial", "src/assets/images/livro18.png", "Um suspense psicológico onde um crime do passado volta a assombrar um grupo de amigos adultos."));
    produtos.add(new Livro("IT: A Coisa", "Terror", 5, 80.00, "Stephen King", "Editora Suma", "Edição Especial", "src/assets/images/livro19.png", "Um clássico do terror que acompanha um grupo de amigos confrontando uma entidade maligna em sua cidade natal."));
    produtos.add(new Livro("Jantar Secreto", "Suspense", 7, 50.90, "Raphael Montes", "Editora Companhia das Letras", "Primeira Edição", "src/assets/images/livro20.png", "Um suspense psicológico intenso ambientado no Rio de Janeiro, envolvendo segredos obscuros e jantares misteriosos."));
    produtos.add(new Livro("O Homem Mais Rico da Babilônia", "Finanças", 20, 19.20, "George S. Clason", "Editora Gente", "Edição Completa", "src/assets/images/livro21.png", "Através de parábolas na antiga Babilônia, o livro ensina princípios fundamentais sobre finanças e prosperidade."));
    produtos.add(new Livro("O Massacre da Serra Elétrica", "Terror", 13, 39.30, "Stefan Jaworzyn", "Editora Mad", "Edição Especial", "src/assets/images/livro22.png", "Uma análise aterrorizante do filme cult de terror, explorando os bastidores e a mente do assassino Leatherface."));
    produtos.add(new Livro("O Pequeno Príncipe", "Infantil", 10, 15.00, "Antoine de Saint-Exupéry", "Editora Agir", "Edição de Luxo", "src/assets/images/livro23.png", "Uma fábula poética e filosófica que aborda temas como a amizade, o amor e a perda sob a perspectiva de uma criança."));
    produtos.add(new Livro("Trilogia Príncipe Cruel", "Fantasia", 12, 120.00, "Holly Black", "Editora Galera", "Edição Completa", "src/assets/images/livro24.png", "Uma trilogia de fantasia sombria e envolvente, repleta de intrigas e jogos de poder na corte feérica."));

    // CDs
    produtos.add(new Cd("1989", "Pop", 25, 70.90, "TAYLOR SWIFT", "Republic Records", "EUA", "src/assets/images/cd1.jpg", "Quinto álbum pop de Taylor Swift."));
    produtos.add(new Cd("After Hours", "R&B", 25, 40.20, "THE WEEKND", "XO/Republic", "Canadá", "src/assets/images/cd2.jpg", "O quarto álbum R&B de The Weeknd."));
    produtos.add(new Cd("A Gente Tem Que Ser Feliz", "MPB", 25, 40.00, "NANDO REIS", "Relicário Music", "Brasil", "src/assets/images/cd3.jpg", "MPB poético e envolvente de Nando Reis."));
    produtos.add(new Cd("A Night at the Opera", "Rock", 25, 79.90, "QUEEN", "EMI", "Reino Unido", "src/assets/images/cd4.jpg", "Álbum de rock icônico do Queen."));
    produtos.add(new Cd("Aventura", "Bachata", 25, 25.90, "AVENTURA", "Premium Latin", "EUA", "src/assets/images/cd5.jpg", "Bachata que consagrou o grupo Aventura."));
    produtos.add(new Cd("Born This Way", "Pop", 25, 39.99, "LADY GAGA", "Interscope", "EUA", "src/assets/images/Cd6.png", "Segundo álbum pop de Lady Gaga."));
    produtos.add(new Cd("Emails I Can't Send", "Pop", 25, 24.20, "SABRINA CARPENTER", "Island Records", "EUA", "src/assets/images/cd7.jpg", "Pop moderno com letras introspectivas."));
    produtos.add(new Cd("K-12", "Alternativo", 25, 50.00, "MELANIE MARTINEZ", "Atlantic Records", "EUA", "src/assets/images/cd9.jpg", "Álbum conceitual alternativo de Melanie Martinez."));
    produtos.add(new Cd("Legends Never Die", "Hip-Hop", 25, 34.50, "JUICE WRLD", "Grade A/Interscope", "EUA", "src/assets/images/cd10.jpg", "Álbum póstumo de hip-hop de Juice WRLD."));
    produtos.add(new Cd("Midnights", "Pop", 25, 53.15, "TAYLOR SWIFT", "Republic Records", "EUA", "src/assets/images/cd11.jpg", "Décimo álbum pop de Taylor Swift."));
    produtos.add(new Cd("No.1", "K-pop", 25, 35.00, "BLACKPINK", "YG Entertainment", "Coreia do Sul", "src/assets/images/cd13.jpg", "Álbum vibrante de K-pop do BLACKPINK."));
    produtos.add(new Cd("Planet Her", "R&B", 25, 35.00, "DOJA CAT", "RCA Records", "EUA", "src/assets/images/cd14.jpg", "R&B e pop futurista de Doja Cat."));
    produtos.add(new Cd("Rumours", "Rock", 25, 39.99, "FLEETWOOD MAC", "Warner Bros. Records", "EUA", "src/assets/images/cd17.jpg", "Álbum de rock clássico do Fleetwood Mac."));
    produtos.add(new Cd("Samba de Raiz", "Samba", 25, 29.90, "ZECA PAGODINHO", "Universal Music", "Brasil", "src/assets/images/cd18.jpg", "Autêntico samba de raiz com Zeca Pagodinho."));
    produtos.add(new Cd("S.O.S", "R&B", 25, 49.90, "SZA", "Top Dawg Entertainment", "EUA", "src/assets/images/cd19.jpg", "Segundo álbum R&B de estúdio de SZA."));
    produtos.add(new Cd("The Dark Side of the Moon", "Rock Progressivo", 25, 35.00, "PINK FLOYD", "Harvest Records", "Reino Unido", "src/assets/images/cd20.jpg", "Álbum conceitual de rock progressivo."));
    produtos.add(new Cd("The Life of Pablo", "Rap", 25, 35.00, "KANYE WEST", "GOOD Music", "EUA", "src/assets/images/cd21.png", "Rap experimental de Kanye West."));
    produtos.add(new Cd("Unorthodox Junkebox", "Pop", 25, 120.00, "BRUNO MARS", "Atlantic Records", "EUA", "src/assets/images/cd24.jpg", "Segundo álbum pop de Bruno Mars."));
    produtos.add(new Cd("Viva la Vida", "Alternativo", 25, 50.00, "COLDPLAY", "Parlophone", "Reino Unido", "src/assets/images/cd23.jpg", "Quarto álbum alternativo do Coldplay."));

    // DVDs
    produtos.add(new Dvd("A Culpa é das Estrelas", "Romance", 15, 29.90, "Josh Boone", "125 min", "12 anos", "src/assets/images/Dvd1.jpg", "Romance emocionante sobre jovens com câncer."));
    produtos.add(new Dvd("A Espera de um Milagre", "Drama", 12, 39.90, "Frank Darabont", "189 min", "14 anos", "src/assets/images/Dvd2.jpg", "Drama comovente sobre um guarda prisional."));
    produtos.add(new Dvd("A Freira", "Terror", 20, 34.90, "Corin Hardy", "96 min", "16 anos", "src/assets/images/Dvd3.jpg", "Terror sobrenatural sobre a origem de uma freira demoníaca."));
    produtos.add(new Dvd("A Mentira", "Comédia", 18, 39.90, "Will Gluck", "92 min", "14 anos", "src/assets/images/Dvd4.jpg", "Comédia adolescente sobre uma mentira popular."));
    produtos.add(new Dvd("Ainda Estou Aqui", "Drama", 20, 34.90, "José Eduardo Belmonte", "120 min", "12 anos", "src/assets/images/Dvd5.jpg", "Drama brasileiro sobre luto e memória."));
    produtos.add(new Dvd("A Teoria de Tudo", "Biografia", 17, 39.90, "James Marsh", "123 min", "12 anos", "src/assets/images/Dvd6.jpg", "Biografia inspiradora de Stephen Hawking."));
    produtos.add(new Dvd("Avengers: Endgame", "Ação", 25, 79.90, "Anthony Russo, Joe Russo", "181 min", "12 anos", "src/assets/images/Dvd7.jpg", "O épico final da saga dos Vingadores."));
    produtos.add(new Dvd("Batman: O Cavaleiro das Trevas", "Ação", 20, 39.90, "Christopher Nolan", "152 min", "14 anos", "src/assets/images/Dvd8.jpg", "Aclamado filme de super-heróis Batman."));
    produtos.add(new Dvd("Blade Runner 2049", "Ficção Científica", 15, 59.90, "Denis Villeneuve", "164 min", "14 anos", "src/assets/images/Dvd9.jpg", "Ficção científica visualmente deslumbrante."));
    produtos.add(new Dvd("Cisne Negro", "Drama", 10, 49.90, "Darren Aronofsky", "108 min", "16 anos", "src/assets/images/Dvd10.jpg", "Suspense psicológico intenso sobre uma bailarina."));
    produtos.add(new Dvd("Coco", "Animação", 22, 49.90, "Lee Unkrich, Adrian Molina", "105 min", "Livre", "src/assets/images/Dvd11.jpg", "Animação emocionante sobre a cultura mexicana."));
    produtos.add(new Dvd("Divertida Mente", "Animação", 30, 39.90, "Pete Docter", "95 min", "Livre", "src/assets/images/Dvd12.jpg", "Animação criativa sobre as emoções de uma garota."));
    produtos.add(new Dvd("E Se Fosse Verdade?", "Romance", 14, 29.90, "Mark Waters", "95 min", "10 anos", "src/assets/images/Dvd13.jpg", "Comédia romântica adorável sobre um homem solitário."));
    produtos.add(new Dvd("Hereditary: O Herdeiro", "Terror", 12, 39.90, "Ari Aster", "127 min", "16 anos", "src/assets/images/Dvd19.jpg", "Terror psicológico perturbador sobre uma família."));
    produtos.add(new Dvd("Interestelar", "Ficção Científica", 18, 59.90, "Christopher Nolan", "169 min", "10 anos", "src/assets/images/Dvd14.jpg", "Ficção científica épica sobre exploração espacial."));
    produtos.add(new Dvd("It: A Coisa", "Terror", 15, 49.90, "Andy Muschietti", "135 min", "16 anos", "src/assets/images/Dvd15.jpg", "Terror baseado no livro de Stephen King."));
    produtos.add(new Dvd("John Wick", "Ação", 20, 39.90, "Chad Stahelski", "101 min", "16 anos", "src/assets/images/Dvd16.jpg", "Ação eletrizante sobre um assassino aposentado."));
    produtos.add(new Dvd("Mad Max: Fury Road", "Ação", 17, 49.90, "George Miller", "120 min", "16 anos", "src/assets/images/Dvd17.jpg", "Ação pós-apocalíptica visualmente impressionante."));
    
    System.out.println("DEBUG: " + produtos.size() + " produtos padrão carregados do código");
}
    
    
 protected void atualizarExibicaoProdutos() {
    // VERIFICAÇÃO DE SEGURANÇA - garantir que todos os componentes existem
    if (produtosGridPanel == null) {
        System.out.println("DEBUG: produtosGridPanel é nulo, inicializando...");
        produtosGridPanel = new JPanel(new GridLayout(0, 5, 20, 20));
        produtosGridPanel.setBackground(new Color(244, 240, 230));
        produtosGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    if (produtosPanelContainer == null) {
        System.out.println("DEBUG: produtosPanelContainer é nulo, inicializando...");
        produtosPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        produtosPanelContainer.setBackground(new Color(244, 240, 230));
        produtosPanelContainer.add(produtosGridPanel);
    }
    
    produtosGridPanel.removeAll();
    
   
    String textoPesquisa = "";
    if (campoPesquisa != null) {
        textoPesquisa = campoPesquisa.getText().toLowerCase();
    } else {
        System.out.println("DEBUG: AVISO - campoPesquisa é nulo, usando string vazia");
    }
    
    ArrayList<Produto> produtosParaExibir = new ArrayList<>();

    System.out.println("DEBUG: --- Início Atualização Exibição ---");
    System.out.println("DEBUG: Texto na pesquisa: '" + textoPesquisa + "'");
    System.out.println("DEBUG: Categoria atual: '" + categoriaAtual + "'");
    System.out.println("DEBUG: Gênero atual: '" + generoAtual + "'");

    if (!textoPesquisa.isEmpty()) {
      
        System.out.println("DEBUG: Modo: Pesquisa");
        String lowerCaseQuery = textoPesquisa.toLowerCase();
        for (Produto p : produtos) {
            boolean pesquisaCorresponde = (p.getDescricao() != null && p.getDescricao().toLowerCase().contains(lowerCaseQuery)) ||
                                         (p.getGenero() != null && p.getGenero().toLowerCase().contains(lowerCaseQuery));
            
       
            boolean categoriaCorresponde = categoriaAtual.equals("HOME") || 
                (categoriaAtual.equals("LIVROS") && "Livro".equals(p.getCategoria())) ||
                (categoriaAtual.equals("CD") && "CD".equals(p.getCategoria())) ||
                (categoriaAtual.equals("DVD") && "DVD".equals(p.getCategoria()));
            
            boolean generoCorresponde = true;

            if (categoriaAtual.equals("LIVROS") && generoAtual != null && p instanceof Livro) {
                generoCorresponde = ((Livro) p).getGenero().equalsIgnoreCase(generoAtual);
            } else if (categoriaAtual.equals("CD") && generoAtual != null && p instanceof Cd) {
                generoCorresponde = ((Cd) p).getGenero().equalsIgnoreCase(generoAtual);
            } else if (categoriaAtual.equals("DVD") && generoAtual != null && p instanceof Dvd) {
                generoCorresponde = ((Dvd) p).getGenero().equalsIgnoreCase(generoAtual);
            }

            if (pesquisaCorresponde && categoriaCorresponde && generoCorresponde) {
                System.out.println("DEBUG:   -> Produto '" + p.getDescricao() + "' corresponde à pesquisa, categoria e gênero. Adicionado.");
                produtosParaExibir.add(p);
            } else {
                System.out.println("DEBUG:   -> Produto '" + p.getDescricao() + "' não corresponde à pesquisa, categoria ou gênero. Ignorado.");
            }
        }
    } else {
      
        System.out.println("DEBUG: Modo: Filtragem por Categoria");
        if (categoriaAtual.equals("HOME")) {
            
            System.out.println("DEBUG: Categoria HOME. Adicionando todos os produtos e embaralhando.");
            produtosParaExibir.addAll(produtos);
            Collections.shuffle(produtosParaExibir, new Random());
        } else if (categoriaAtual.equals("LIVROS")) {
           
            System.out.println("DEBUG: Categoria LIVROS. Exibindo todos os livros inicialmente.");
            for (Produto p : produtos) {
              
                if ("Livro".equals(p.getCategoria())) {
                    System.out.println("DEBUG:   -> Livro '" + p.getDescricao() + "' adicionado.");
                    produtosParaExibir.add(p);
                }
            }
           
            if (generoAtual != null) {
                ArrayList<Produto> livrosFiltradosPorGenero = new ArrayList<>();
                String lowerCaseGenero = generoAtual.toLowerCase();
                System.out.println("DEBUG: Categoria LIVROS e gênero '" + generoAtual + "'. Filtrando por gênero.");
                for (Produto p : produtosParaExibir) {
                    if (p instanceof Livro && ((Livro) p).getGenero() != null && ((Livro) p).getGenero().toLowerCase().equals(lowerCaseGenero)) {
                        System.out.println("DEBUG:   -> Livro '" + p.getDescricao() + "' com gênero '" + ((Livro) p).getGenero() + "' corresponde. Mantido.");
                        livrosFiltradosPorGenero.add(p);
                    } else {
                        System.out.println("DEBUG:   -> Livro '" + p.getDescricao() + "' não corresponde ao gênero '" + generoAtual + "'. Removido.");
                    }
                }
                produtosParaExibir = livrosFiltradosPorGenero;
            }
        } else if (categoriaAtual.equals("CD")) {
         
            System.out.println("DEBUG: Categoria CD. Exibindo todos os CDs inicialmente.");
            for (Produto p : produtos) {
               
                if ("CD".equals(p.getCategoria())) {
                    System.out.println("DEBUG:   -> CD '" + p.getDescricao() + "' adicionado.");
                    produtosParaExibir.add(p);
                }
            }
           
            if (generoAtual != null) {
                ArrayList<Produto> cdsFiltradosPorGenero = new ArrayList<>();
                String lowerCaseGenero = generoAtual.toLowerCase();
                System.out.println("DEBUG: Categoria CD e gênero '" + generoAtual + "'. Filtrando por gênero.");
                for (Produto p : produtosParaExibir) {
                    if (p instanceof Cd && ((Cd) p).getGenero() != null && ((Cd) p).getGenero().toLowerCase().equals(lowerCaseGenero)) {
                        System.out.println("DEBUG:   -> CD '" + p.getDescricao() + "' com gênero '" + ((Cd) p).getGenero() + "' corresponde. Mantido.");
                        cdsFiltradosPorGenero.add(p);
                    } else {
                        System.out.println("DEBUG:   -> CD '" + p.getDescricao() + "' não corresponde ao gênero '" + generoAtual + "'. Removido.");
                    }
                }
                produtosParaExibir = cdsFiltradosPorGenero;
            }
        } else if (categoriaAtual.equals("DVD")) {
           
            System.out.println("DEBUG: Categoria DVD. Exibindo todos os DVDs inicialmente.");
            for (Produto p : produtos) {
                // CORREÇÃO: Verificar se é um DVD usando a categoria
                if ("DVD".equals(p.getCategoria())) {
                    System.out.println("DEBUG:   -> DVD '" + p.getDescricao() + "' adicionado.");
                    produtosParaExibir.add(p);
                }
            }
          
            if (generoAtual != null) {
                ArrayList<Produto> dvdsFiltradosPorGenero = new ArrayList<>();
                String lowerCaseGenero = generoAtual.toLowerCase();
                System.out.println("DEBUG: Categoria DVD e gênero '" + generoAtual + "'. Filtrando por gênero.");
                for (Produto p : produtosParaExibir) {
                    if (p instanceof Dvd && ((Dvd) p).getGenero() != null && ((Dvd) p).getGenero().toLowerCase().equals(lowerCaseGenero)) {
                        System.out.println("DEBUG:   -> DVD '" + p.getDescricao() + "' com gênero '" + ((Dvd) p).getGenero() + "' corresponde. Mantido.");
                        dvdsFiltradosPorGenero.add(p);
                    } else {
                        System.out.println("DEBUG:   -> DVD '" + p.getDescricao() + "' não corresponde ao gênero '" + generoAtual + "'. Removido.");
                    }
                }
                produtosParaExibir = dvdsFiltradosPorGenero;
            }
        }
    }

    System.out.println("DEBUG: Total de produtos para exibir: " + produtosParaExibir.size());
    System.out.println("DEBUG: --- Fim Atualização Exibição ---");

  
    JPanel novoProdutosGridPanel = new JPanel(new GridLayout(0, 5, 20, 20));
    novoProdutosGridPanel.setBackground(new Color(244, 240, 230));
    novoProdutosGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


    for (Produto p : produtosParaExibir) {
        novoProdutosGridPanel.add(criarCard(p));
    }

   
    produtosPanelContainer.removeAll();

  
    produtosPanelContainer.add(novoProdutosGridPanel);


    produtosGridPanel = novoProdutosGridPanel;

   
    produtosPanelContainer.revalidate();
    produtosPanelContainer.repaint();
    
  
    if (scrollPane != null) {
        scrollPane.revalidate();
        scrollPane.repaint();
    } else {
        System.out.println("DEBUG: AVISO - scrollPane é nulo durante atualização");
    }
    
    this.revalidate();
    this.repaint();
}
}