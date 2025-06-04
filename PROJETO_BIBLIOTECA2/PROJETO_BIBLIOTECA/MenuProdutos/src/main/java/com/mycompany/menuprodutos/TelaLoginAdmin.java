/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

// Aqui é onde importo tudo que preciso pra fazer a interface bonitona funcionar
import javax.swing.*;
import java.awt.*;

public class TelaLoginAdmin extends JFrame { // Essa é a classe da tela de login do admin, que herda da janela do Java

    public TelaLoginAdmin() { // Construtor da classe, que monta toda a tela quando o programa roda
        // Painel principal que vai segurar tudo que vai aparecer na tela
        JPanel painel = new JPanel();
        // Organiza os componentes em coluna, tipo um embaixo do outro
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        // Cor de fundo beeem clarinha, estilo bege
        painel.setBackground(new Color(249, 237, 202)); // bege claro
        // Espaço em volta, tipo uma margem pra não grudar nas bordas da janela
        painel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Painel só pro título, que vai ter duas linhas
        JPanel painelTitulo = new JPanel();
        // Mesma cor clarinha do fundo pra ficar tudo homogêneo
        painelTitulo.setBackground(new Color(249, 237, 202));
        // Organiza as linhas do título uma em cima da outra
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));
        // Centraliza esse painel na janela, porque ficar alinhado à esquerda não dá
        painelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Primeira linha do título, com "Bem-Vindo" e "de Volta" lado a lado
        JPanel linha1 = new JPanel();
        // Fundo clarinho também pra combinar
        linha1.setBackground(new Color(249, 237, 202));
        // FlowLayout deixa os textos lado a lado, e o '10' é o espacinho entre eles
        linha1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Label com o texto "Bem-Vindo"
        JLabel bemVindoDeVolta = new JLabel("Bem-Vindo de Volta");
        // Fonte bonitona e grande, estilo serifada e negrito
        bemVindoDeVolta.setFont(new Font("Serif", Font.BOLD, 22));
        // Cor marrom escuro, pra ficar chique e legível
        bemVindoDeVolta.setForeground(new Color(89, 55, 30));

        // Jogo os dois textos na linha1, lado a lado
        linha1.add(bemVindoDeVolta);

        // Segunda linha do título com o texto "Administrador!"
        JLabel administrador = new JLabel("Administrador!");
        administrador.setFont(new Font("Serif", Font.BOLD, 22));
        administrador.setForeground(new Color(89, 55, 30));
        // Centraliza esse texto no painel do título
        administrador.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agora adiciono as duas linhas no painel do título, uma embaixo da outra
        painelTitulo.add(linha1);
        painelTitulo.add(administrador);

        // Subtítulo explicando o que fazer
        JLabel subtitulo = new JLabel("Coloque seu email e sua senha para continuar:");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Serif", Font.PLAIN, 14)); // fonte menor e sem negrito pra não roubar a cena
        subtitulo.setForeground(new Color(89, 55, 30));
        // Espaço em cima e embaixo pra ficar mais organizado
        subtitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Painel do formulário que vai ter os labels e campos de texto, com layout GridBag (super flexível)
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(new Color(249, 237, 202));
        GridBagConstraints gbc = new GridBagConstraints(); // é tipo as regras pra posicionar os componentes

        // Label "Email (gmail):"
        JLabel labelEmail = new JLabel("E-mail (gmail):");
        labelEmail.setFont(new Font("Serif", Font.PLAIN, 14));
        labelEmail.setForeground(new Color(89, 55, 30));

        // Campo onde o usuário digita o email
        JTextField campoEmail = new JTextField(20);

        // Label "Senha:"
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(new Font("Serif", Font.PLAIN, 14));
        labelSenha.setForeground(new Color(89, 55, 30));

        // Campo pra senha, com aqueles pontinhos pra esconder o que a pessoa digita
        JPasswordField campoSenha = new JPasswordField(20);
        campoSenha.setEchoChar('•'); // pontinho estiloso no lugar das letras

        // Configurações pra deixar os labels e campos alinhadinhos à esquerda e ocupando toda a largura possível
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 0, 0); // espaçamento só em cima pra dar um respiro
        gbc.weightx = 1.0; // pra os campos crescerem na horizontal se precisar

        // Coloca o label do email na primeira linha (x=0, y=0)
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelFormulario.add(labelEmail, gbc);

        // Campo de email na linha de baixo (x=0, y=1)
        gbc.gridy = 1;
        painelFormulario.add(campoEmail, gbc);

        // Label da senha na linha 2 (x=0, y=2)
        gbc.gridy = 2;
        painelFormulario.add(labelSenha, gbc);

        // Campo da senha na linha 3 (x=0, y=3)
        gbc.gridy = 3;
        painelFormulario.add(campoSenha, gbc);

        // Botão "Concluir" centralizado, pra pessoa clicar quando acabar de preencher tudo
        JButton btnEntrar = new JButton("Concluir");
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.setBackground(new Color(89, 35, 30)); // cor marrom escuro bem bonita
        btnEntrar.setForeground(Color.WHITE); // texto branco pra contrastar
        btnEntrar.setFocusPainted(false); // tira aquela borda azul do foco, fica mais limpinho
        btnEntrar.setFont(new Font("Serif", Font.BOLD, 14)); // fonte legal e em negrito
        btnEntrar.setMaximumSize(new Dimension(200, 30)); // tamanho máximo que o botão pode ter
        btnEntrar.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // espacinho em cima só pra respirar
        btnEntrar.setHorizontalAlignment(SwingConstants.CENTER);  // texto do botão centralizado, claro

        // Agora sim, adiciono tudo que criei no painel principal pra aparecer na tela
        painel.add(painelTitulo);
        painel.add(subtitulo);
        painel.add(painelFormulario);
        painel.add(Box.createRigidArea(new Dimension(0, 20))); // espaçamento vertical entre formulário e botão
        painel.add(btnEntrar);

        // Aqui eu adiciono o painel na janela, configuro tamanho e outras coisas da janela
        add(painel);
        setSize(380, 340);  // tamanho da janela, tá mais baixinha agora
        setLocationRelativeTo(null); // faz a janela abrir no meio da tela, porque ninguém gosta de abrir no canto, né?
        setDefaultCloseOperation(EXIT_ON_CLOSE); // fecha tudo quando clica no X, pra não ficar aberto no fundo
        setVisible(true); // mostra a janela, isso é obrigatório pra tela aparecer

        // Agora o botão ganhar uma ação, tipo quando clicar mostra um popup com os dados que a pessoa digitou
        btnEntrar.addActionListener(e -> {
            String email = campoEmail.getText(); // pega o texto digitado no email
            String senha = new String(campoSenha.getPassword()); // pega a senha (que vem como um array de char)

            // Mostra uma janelinha com os dados que a pessoa colocou, só de exemplo mesmo
            JOptionPane.showMessageDialog(this,
                    "E-mail (gmail): " + email + "\nSenha: " + senha,
                    "Dados inseridos",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}