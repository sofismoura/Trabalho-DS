/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package novoPack;

import com.mycompany.menuprodutos.Cd;
import com.mycompany.menuprodutos.Dvd;
import com.mycompany.menuprodutos.ItemCarrinho;
import com.mycompany.menuprodutos.Livro;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import com.mycompany.menuprodutos.Produto;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BancoDeDados {
    private static final String URL = "jdbc:mysql://localhost:3306/menuprodutos";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    
    public static boolean salvarImagemPerfil(int idUsuario, byte[] imagemBytes) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO perfil_usuario (id_usuario, imagem_perfil) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE imagem_perfil = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setBytes(2, imagemBytes);
            stmt.setBytes(3, imagemBytes);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao salvar imagem de perfil: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
     
    
    public static boolean produtoExiste(int idProduto) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id FROM produtos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idProduto);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar produto: " + e.getMessage());
            return false;
        }
    }
    
    
    public static Date getUltimaAtualizacaoProdutos() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT MAX(data_cadastro) as ultima_atualizacao FROM produtos";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getTimestamp("ultima_atualizacao");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter última atualização: " + e.getMessage());
        }
        return null;
    }
    
    
    public static ArrayList<Integer> getProdutosModificados(Date desde) {
        ArrayList<Integer> idsModificados = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id FROM produtos WHERE data_cadastro > ? OR " +
                        "(SELECT MAX(data_atualizacao) FROM (SELECT data_cadastro as data_atualizacao FROM produtos) as temp) > ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new java.sql.Timestamp(desde.getTime()));
            stmt.setTimestamp(2, new java.sql.Timestamp(desde.getTime()));
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                idsModificados.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter produtos modificados: " + e.getMessage());
        }
        
        return idsModificados;
    }
    
  
    public static boolean excluirProduto(int idProduto) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM produtos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idProduto);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Produto ID " + idProduto + " excluído do banco");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao excluir produto: " + e.getMessage());
        }
        return false;
    }


    public static byte[] carregarImagemPerfil(int idUsuario) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT imagem_perfil FROM perfil_usuario WHERE id_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBytes("imagem_perfil");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar imagem de perfil: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static boolean existeImagemPerfil(int idUsuario) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id FROM perfil_usuario WHERE id_usuario = ? AND imagem_perfil IS NOT NULL";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar imagem de perfil: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
   
    public static boolean verificarCredenciais(String email, String senha) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id FROM usuarios WHERE email = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Erro ao verificar credenciais: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    
    public static String obterNomeUsuario(String email) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT nome_completo FROM usuarios WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("nome_completo");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao obter nome do usuário: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "Usuário";
    }

   
    public static int obterIdUsuario(String email) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id FROM usuarios WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao obter ID do usuário: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }

   
    public static boolean atualizarSenha(String email, String novaSenha) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE usuarios SET senha = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, novaSenha);
            stmt.setString(2, email);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar senha: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    
    public static void salvarCliente(String nome, String endereco, String telefone, String cpf, String email) {
        String senhaPadrao = "123456";
        salvarUsuario(nome, email, senhaPadrao, telefone, endereco, "Cliente");
    }

    public static boolean validarLogin(String email, String senha) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao validar login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    
    public static boolean isAdmin(String email) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT tipo_usuario FROM usuarios WHERE email = ? AND tipo_usuario = 'Admin'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public static ResultSet getUsuarioPorEmail(String email) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id, nome_completo, email, tipo_usuario FROM usuarios WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
   
public static void inserirDadosExemploAnalise() {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        
        
        String sqlVerificaPedidos = "SELECT COUNT(*) as total FROM pedidos";
        PreparedStatement stmtVerifica = conn.prepareStatement(sqlVerificaPedidos);
        ResultSet rs = stmtVerifica.executeQuery();
        
        if (rs.next() && rs.getInt("total") == 0) {
            System.out.println("Inserindo dados de exemplo para análise...");
            
           
            String sqlUsuarios = "SELECT id FROM usuarios LIMIT 3";
            PreparedStatement stmtUsuarios = conn.prepareStatement(sqlUsuarios);
            ResultSet rsUsuarios = stmtUsuarios.executeQuery();
            
            List<Integer> idsUsuarios = new ArrayList<>();
            while (rsUsuarios.next()) {
                idsUsuarios.add(rsUsuarios.getInt("id"));
            }
            
            if (idsUsuarios.isEmpty()) {
                System.out.println("Nenhum usuário encontrado para criar dados de exemplo");
                return;
            }
            
            // Produtos de exemplo para livraria
            String[][] produtosExemplo = {
                {"Dom Casmurro", "Romance", "29.90"},
                {"1984", "Ficção Científica", "34.90"},
                {"O Pequeno Príncipe", "Infantil", "24.90"},
                {"Harry Potter e a Pedra Filosofal", "Fantasia", "39.90"},
                {"A Culpa é das Estrelas", "Romance", "32.90"},
                {"O Senhor dos Anéis", "Fantasia", "49.90"},
                {"Orgulho e Preconceito", "Romance", "27.90"},
                {"O Hobbit", "Fantasia", "36.90"},
                {"A Revolução dos Bichos", "Ficção", "22.90"},
                {"Crime e Castigo", "Clássico", "31.90"}
            };
            
           
            String[] status = {"Pendente", "Pago", "Preparando", "Enviado", "Entregue"};
            
            
            for (int i = 0; i < 15; i++) {
                String codigoPedido = "PED" + System.currentTimeMillis() + i;
                int idUsuario = idsUsuarios.get(i % idsUsuarios.size());
                String statusPedido = status[i % status.length];
                
              
                int mesesAtras = (int) (Math.random() * 6);
                int diasAtras = (int) (Math.random() * 30);
                
                String sqlPedido = "INSERT INTO pedidos (codigo_pedido, id_usuario, nome_cliente, endereco, telefone, cpf, email, preco_total, status, data_pedido) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, DATE_SUB(DATE_SUB(CURRENT_DATE(), INTERVAL ? MONTH), INTERVAL ? DAY))";
                PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido);
                stmtPedido.setString(1, codigoPedido);
                stmtPedido.setInt(2, idUsuario);
                stmtPedido.setString(3, "Cliente Exemplo " + (i + 1));
                stmtPedido.setString(4, "Rua Exemplo, " + (i + 1));
                stmtPedido.setString(5, "(11) 99999-999" + i);
                stmtPedido.setString(6, "123.456.789-0" + i);
                stmtPedido.setString(7, "cliente" + (i + 1) + "@exemplo.com");
                
                double precoTotal = 0;
                
               
                int numItens = (int) (Math.random() * 3) + 1;
                for (int j = 0; j < numItens; j++) {
                    String[] produto = produtosExemplo[(i + j) % produtosExemplo.length];
                    String nomeProduto = produto[0];
                    String categoria = produto[1];
                    double precoUnitario = Double.parseDouble(produto[2]);
                    int quantidade = (int) (Math.random() * 2) + 1; 
                    double precoTotalItem = precoUnitario * quantidade;
                    
                    precoTotal += precoTotalItem;
                    
                    String sqlItem = "INSERT INTO itens_pedido (codigo_pedido, id_produto, nome_produto, quantidade, preco_unitario, preco_total, categoria) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                    stmtItem.setString(1, codigoPedido);
                    stmtItem.setInt(2, (i * 10 + j + 1)); 
                    stmtItem.setString(3, nomeProduto);
                    stmtItem.setInt(4, quantidade);
                    stmtItem.setDouble(5, precoUnitario);
                    stmtItem.setDouble(6, precoTotalItem);
                    stmtItem.setString(7, categoria);
                    stmtItem.executeUpdate();
                    stmtItem.close();
                }
                
                stmtPedido.setDouble(8, precoTotal);
                stmtPedido.setString(9, statusPedido);
                stmtPedido.setInt(10, mesesAtras);
                stmtPedido.setInt(11, diasAtras);
                stmtPedido.executeUpdate();
                stmtPedido.close();
                
                System.out.println("Pedido exemplo criado: " + codigoPedido + " - R$ " + precoTotal);
            }
            
            System.out.println("✓ Dados de exemplo inseridos com sucesso!");
        } else {
            System.out.println("Já existem pedidos no banco. Pulando inserção de dados exemplo.");
        }
        
    } catch (SQLException e) {
        System.err.println("Erro ao inserir dados exemplo: " + e.getMessage());
        e.printStackTrace();
    }
}

   
    public static ResultSet getUsuarioCompletoPorEmail(String email) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id, nome_completo, email, telefone, endereco, tipo_usuario FROM usuarios WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

   
    public static boolean emailExiste(String email) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id FROM usuarios WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public static void salvarCarrinho(int idUsuario, ArrayList<ItemCarrinho> carrinho) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
           
            String sqlLimpar = "DELETE FROM carrinho_usuario WHERE id_usuario = ?";
            PreparedStatement stmtLimpar = conn.prepareStatement(sqlLimpar);
            stmtLimpar.setInt(1, idUsuario);
            stmtLimpar.executeUpdate();

          
            String sqlInserir = "INSERT INTO carrinho_usuario (id_usuario, id_produto, quantidade, nome_produto, categoria, preco) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtInserir = conn.prepareStatement(sqlInserir);
            
            for (ItemCarrinho item : carrinho) {
                Produto produto = item.getProduto();
                stmtInserir.setInt(1, idUsuario);
                stmtInserir.setInt(2, produto.getId());
                stmtInserir.setInt(3, item.getQuantidade());
                stmtInserir.setString(4, produto.getDescricao());
                stmtInserir.setString(5, produto.getCategoria());
                stmtInserir.setDouble(6, produto.getPreco());
                stmtInserir.addBatch();
            }
            stmtInserir.executeBatch();
            
            System.out.println("Carrinho salvo no banco para usuário ID: " + idUsuario);
            
        } catch (SQLException e) {
            System.err.println("Erro ao salvar carrinho: " + e.getMessage());
            e.printStackTrace();
        }
    }

   
    public static ArrayList<ItemCarrinho> carregarCarrinho(int idUsuario) {
        ArrayList<ItemCarrinho> carrinho = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id_produto, quantidade, nome_produto, categoria, preco FROM carrinho_usuario WHERE id_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
               
                Produto produto = new Produto();
                produto.setId(rs.getInt("id_produto"));
                produto.setDescricao(rs.getString("nome_produto"));
                produto.setCategoria(rs.getString("categoria"));
                produto.setPreco(rs.getDouble("preco"));
                
               
                ItemCarrinho item = new ItemCarrinho(produto, rs.getInt("quantidade"));
                carrinho.add(item);
            }
            
            System.out.println("Carrinho carregado do banco para usuário ID: " + idUsuario + " - Itens: " + carrinho.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao carregar carrinho: " + e.getMessage());
            e.printStackTrace();
        }
        return carrinho;
    }

   
    public static void limparCarrinhoUsuario(int idUsuario) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM carrinho_usuario WHERE id_usuario = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
            
            System.out.println("Carrinho limpo para usuário ID: " + idUsuario);
            
        } catch (SQLException e) {
            System.err.println("Erro ao limpar carrinho: " + e.getMessage());
            e.printStackTrace();
        }
    }

  
    public static boolean salvarPedido(String codigoPedido, int idUsuario, String nomeCliente, String endereco, 
                                     String telefone, String cpf, String email, double precoTotal) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO pedidos (codigo_pedido, id_usuario, nome_cliente, endereco, telefone, cpf, email, preco_total, status, data_pedido) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Pendente', NOW())";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, codigoPedido);
            stmt.setInt(2, idUsuario);
            stmt.setString(3, nomeCliente);
            stmt.setString(4, endereco);
            stmt.setString(5, telefone);
            stmt.setString(6, cpf);
            stmt.setString(7, email);
            stmt.setDouble(8, precoTotal);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao salvar pedido: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

   
    public static boolean salvarItensPedido(String codigoPedido, ArrayList<ItemCarrinho> carrinho) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO itens_pedido (codigo_pedido, id_produto, nome_produto, quantidade, preco_unitario, preco_total) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for (ItemCarrinho item : carrinho) {
                stmt.setString(1, codigoPedido);
                stmt.setInt(2, item.getProduto().getId());
                stmt.setString(3, item.getProduto().getDescricao());
                stmt.setInt(4, item.getQuantidade());
                stmt.setDouble(5, item.getProduto().getPreco());
                stmt.setDouble(6, item.calcularPrecoTotal());
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erro ao salvar itens do pedido: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

   
    public static int obterIdUsuarioPorEmail(String email) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id FROM usuarios WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
            return -1;
            
        } catch (SQLException e) {
            System.err.println("Erro ao obter ID do usuário: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

   
    public static boolean codigoPedidoExiste(String codigoPedido) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id FROM pedidos WHERE codigo_pedido = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, codigoPedido);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public static ResultSet getPedidosPorUsuario(int idUsuario) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM pedidos WHERE id_usuario = ? ORDER BY data_pedido DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
   
    public static ResultSet getTodosUsuarios() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "SELECT id, nome_completo, email, telefone, tipo_usuario, data_cadastro " +
                        "FROM usuarios ORDER BY data_cadastro DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuários: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

   
    public static ResultSet getTodosPedidos() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "SELECT codigo_pedido, nome_cliente, email, preco_total, status, data_pedido " +
                        "FROM pedidos ORDER BY data_pedido DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pedidos: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

   
    public static ResultSet getTodosItensPedido() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "SELECT codigo_pedido, nome_produto, quantidade, preco_unitario, preco_total " +
                        "FROM itens_pedido ORDER BY codigo_pedido DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar itens do pedido: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

   
    public static ResultSet getTodosPerfisUsuario() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "SELECT pu.id, pu.id_usuario, u.nome_completo, u.email, pu.imagem_perfil, pu.data_atualizacao " +
                        "FROM perfil_usuario pu " +
                        "INNER JOIN usuarios u ON pu.id_usuario = u.id " +
                        "ORDER BY pu.data_atualizacao DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar perfis de usuário: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static ResultSet getItensPedido(String codigoPedido) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM itens_pedido WHERE codigo_pedido = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, codigoPedido);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
   
    private static void inserirAdministradoresPadrao(Connection conn) {
        try {
            String sql = "INSERT IGNORE INTO usuarios (nome_completo, email, senha, telefone, endereco, tipo_usuario) VALUES (?, ?, ?, ?, ?, 'Admin')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            // Administrador 1
            stmt.setString(1, "Administrador Vinicius");
            stmt.setString(2, "ViniAdmin@gmail.com");
            stmt.setString(3, "admin123");
            stmt.setString(4, "(11) 99999-9999");
            stmt.setString(5, "Endereço Admin");
            stmt.addBatch();
            
            // Administrador 2
            stmt.setString(1, "Administrador Maria");
            stmt.setString(2, "MariAdmin@gmail.com");
            stmt.setString(3, "admin123");
            stmt.setString(4, "(11) 88888-8888");
            stmt.setString(5, "Endereço Admin");
            stmt.addBatch();
            
            // Administrador 3
            stmt.setString(1, "Administrador Sofia");
            stmt.setString(2, "SofiaAdmin@gmail.com");
            stmt.setString(3, "admin123");
            stmt.setString(4, "(11) 77777-7777");
            stmt.setString(5, "Endereço Admin");
            stmt.addBatch();
            
            stmt.executeBatch();
            System.out.println("✓ Administradores padrão inseridos/verificados");
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir administradores: " + e.getMessage());
        }
    }

   
    public static boolean salvarUsuario(String nome, String email, String senha, String telefone, String endereco, String tipoUsuario) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO usuarios (nome_completo, email, senha, telefone, endereco, tipo_usuario) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.setString(4, telefone);
            stmt.setString(5, endereco);
            stmt.setString(6, tipoUsuario);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(null, "Erro: E-mail já cadastrado!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao salvar no banco: " + e.getMessage());
            }
            e.printStackTrace();
            return false;
        }
    }

    
    public static boolean atualizarStatusPedido(String codigoPedido, String novoStatus) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE pedidos SET status = ? WHERE codigo_pedido = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, novoStatus);
            stmt.setString(2, codigoPedido);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do pedido: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
 public static void criarTabelaProdutos() {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        
        // SQL para criar a tabela com estrutura CORRETA
        String sqlProdutos = "CREATE TABLE IF NOT EXISTS produtos (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "descricao VARCHAR(200) NOT NULL," +
                            "genero VARCHAR(100) NOT NULL," + // COLUNA ADICIONADA
                            "estoque INT NOT NULL," +
                            "preco DECIMAL(10,2) NOT NULL," +
                            "tipo_produto ENUM('LIVRO','CD','DVD') NOT NULL," + // CORRETO: tipo_produto
                            "caminho_imagem VARCHAR(255)," +
                            "descricao_detalhada TEXT," +
                            "autor VARCHAR(200)," +
                            "editora VARCHAR(200)," +
                            "edicao VARCHAR(100)," +
                            "artista VARCHAR(200)," +
                            "gravadora VARCHAR(200)," +
                            "pais_origem VARCHAR(100)," +
                            "diretor VARCHAR(200)," +
                            "duracao VARCHAR(50)," +
                            "censura VARCHAR(20)," +
                            "imagem_bytes LONGBLOB," +
                            "data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ")";
        
        PreparedStatement stmt = conn.prepareStatement(sqlProdutos);
        stmt.execute();
        System.out.println("✅ Tabela 'produtos' verificada/criada com sucesso!");
        
        // Verificar se a tabela tem dados
        String sqlCount = "SELECT COUNT(*) as total FROM produtos";
        PreparedStatement stmtCount = conn.prepareStatement(sqlCount);
        ResultSet rs = stmtCount.executeQuery();
        if (rs.next()) {
            int total = rs.getInt("total");
            System.out.println("📊 Total de produtos na tabela: " + total);
        }
        
    } catch (SQLException e) {
        System.err.println("❌ Erro ao criar tabela produtos: " + e.getMessage());
        e.printStackTrace();
    }
}

private static void inserirProdutosExemplo(Connection conn) {
    try {
      
        String[][] produtosExemplo = {
            
            {"Dom Casmurro", "Romance", "15", "29.90", "Livro", "Clássico da literatura brasileira", "Machado de Assis", "Editora Garnier", "1ª Edição", null, null, null, null, null, null},
            {"1984", "Ficção Científica", "12", "34.90", "Livro", "Distopia sobre vigilância totalitária", "George Orwell", "Companhia das Letras", "Edição Especial", null, null, null, null, null, null},
            {"O Pequeno Príncipe", "Infantil", "20", "24.90", "Livro", "Fábula sobre amizade e humanidade", "Antoine de Saint-Exupéry", "Editora Agir", "Edição de Bolso", null, null, null, null, null, null},
            {"Harry Potter e a Pedra Filosofal", "Fantasia", "18", "39.90", "Livro", "Aventura mágica de um jovem bruxo", "J.K. Rowling", "Editora Rocco", "1ª Edição", null, null, null, null, null, null},
            {"A Culpa é das Estrelas", "Romance", "22", "32.90", "Livro", "História de amor entre jovens com câncer", "John Green", "Editora Intrínseca", "Edição Brasileira", null, null, null, null, null, null},
            
          
            {"O Poderoso Chefão", "Drama", "8", "39.90", "DVD", "Clássico do cinema sobre família", null, null, null, null, null, null, "Francis Ford Coppola", "175 min", "16 anos"},
            {"Interestelar", "Ficção Científica", "10", "44.90", "DVD", "Viagem espacial interestelar", null, null, null, null, null, null, "Christopher Nolan", "169 min", "12 anos"},
            {"Matrix", "Ficção Científica", "15", "37.90", "DVD", "Realidade virtual e rebelião das máquinas", null, null, null, null, null, null, "Lana Wachowski", "136 min", "14 anos"},
            {"O Senhor dos Anéis: A Sociedade do Anel", "Fantasia", "12", "42.90", "DVD", "Episódio da jornada para destruir o Um Anel", null, null, null, null, null, null, "Peter Jackson", "178 min", "12 anos"},
            {"Cidade de Deus", "Drama", "9", "35.90", "DVD", "Drama sobre a vida no Rio de Janeiro", null, null, null, null, null, null, "Fernando Meirelles", "130 min", "18 anos"},
            
            
            {"Thriller", "Pop", "20", "59.90", "CD", "Álbum mais vendido da história", null, null, null, "Michael Jackson", "Epic Records", "EUA", null, null, null},
            {"The Dark Side of the Moon", "Rock Progressivo", "18", "49.90", "CD", "Clássico do rock progressivo", null, null, null, "Pink Floyd", "Harvest Records", "Reino Unido", null, null, null},
            {"Abbey Road", "Rock", "12", "54.90", "CD", "Último álbum dos Beatles", null, null, null, "The Beatles", "Apple Records", "Reino Unido", null, null, null},
            {"Legend", "Reggae", "16", "47.90", "CD", "Maiores sucessos de Bob Marley", null, null, null, "Bob Marley", "Island Records", "Jamaica", null, null, null},
            {"Back in Black", "Rock", "14", "52.90", "CD", "Clássico do hard rock", null, null, null, "AC/DC", "Atlantic Records", "Austrália", null, null, null}
        };
        
        
        String sql = "INSERT INTO produtos (descricao, genero, estoque, preco, tipo_produto, descricao_detalhada, " +
                    "autor, editora, edicao, artista, gravadora, pais_origem, diretor, duracao, censura) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        
        for (String[] produto : produtosExemplo) {
            stmt.setString(1, produto[0]); 
            stmt.setString(2, produto[1]); 
            stmt.setInt(3, Integer.parseInt(produto[2])); 
            stmt.setDouble(4, Double.parseDouble(produto[3])); 
            
          
            String categoriaJava = produto[4];
            String tipoProdutoBanco = converterCategoriaParaBanco(categoriaJava);
            stmt.setString(5, tipoProdutoBanco); 
            
            stmt.setString(6, produto[5]); 
            
           
            if ("Livro".equals(categoriaJava)) {
                stmt.setString(7, produto[6]); // autor
                stmt.setString(8, produto[7]); // editora
                stmt.setString(9, produto[8]); // edicao
                stmt.setString(10, null); // artista
                stmt.setString(11, null); // gravadora
                stmt.setString(12, null); // pais_origem
                stmt.setString(13, null); // diretor
                stmt.setString(14, null); // duracao
                stmt.setString(15, null); // censura
                
            } else if ("DVD".equals(categoriaJava)) {
                stmt.setString(7, null); // autor
                stmt.setString(8, null); // editora
                stmt.setString(9, null); // edicao
                stmt.setString(10, null); // artista
                stmt.setString(11, null); // gravadora
                stmt.setString(12, null); // pais_origem
                stmt.setString(13, produto[12]); // diretor
                stmt.setString(14, produto[13]); // duracao
                stmt.setString(15, produto[14]); // censura
                
            } else if ("CD".equals(categoriaJava)) {
                stmt.setString(7, null); // autor
                stmt.setString(8, null); // editora
                stmt.setString(9, null); // edicao
                stmt.setString(10, produto[9]); // artista
                stmt.setString(11, produto[10]); // gravadora
                stmt.setString(12, produto[11]); // pais_origem
                stmt.setString(13, null); // diretor
                stmt.setString(14, null); // duracao
                stmt.setString(15, null); // censura
            }
            
            stmt.addBatch();
        }
        
        int[] resultados = stmt.executeBatch();
        System.out.println("✅ " + resultados.length + " produtos de exemplo inseridos com sucesso!");
        
    } catch (SQLException e) {
        System.err.println("❌ Erro ao inserir produtos exemplo: " + e.getMessage());
        e.printStackTrace();
    }
}


private static String converterCategoriaParaBanco(String categoriaJava) {
    if (categoriaJava == null) return "LIVRO";
    
    switch (categoriaJava.toUpperCase()) {
        case "LIVRO":
        case "BOOK":
            return "LIVRO";
        case "CD":
            return "CD";
        case "DVD":
            return "DVD";
        default:
            return "LIVRO"; // Default
    }
}
  
public static void verificarEstruturaBanco() {
    System.out.println("=== VERIFICANDO ESTRUTURA DO BANCO ===");
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        // ... (código existente de verificação)
        
        // VERIFICAÇÃO DE PEDIDOS - ADICIONE ESTA PARTE
        String sqlPedidosCount = "SELECT COUNT(*) as total FROM pedidos";
        PreparedStatement stmtPedidosCount = conn.prepareStatement(sqlPedidosCount);
        ResultSet rsPedidosCount = stmtPedidosCount.executeQuery();
        int totalPedidos = 0;
        if (rsPedidosCount.next()) {
            totalPedidos = rsPedidosCount.getInt("total");
        }
        System.out.println("✓ Tabela 'pedidos': " + totalPedidos + " registros");
        
        // SE NÃO HOUVER PEDIDOS, INSERE DADOS EXEMPLO
        if (totalPedidos == 0) {
            System.out.println("💡 Inserindo dados de exemplo para análise...");
            inserirDadosExemploAnalise();
        }
        
        rsPedidosCount.close();
        stmtPedidosCount.close();
        criarTabelaProdutos();
        
    } catch (SQLException e) {
        System.err.println("❌ Erro ao verificar estrutura do banco: " + e.getMessage());
    }
}
public static boolean salvarProduto(Produto produto, byte[] imagemBytes) {
    System.out.println("=== DEBUG SALVAR PRODUTO ===");
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        System.out.println("✅ Conexão OK");
        
       
        System.out.println("📦 Dados recebidos:");
        System.out.println("   Descrição: " + produto.getDescricao());
        System.out.println("   Gênero: " + produto.getGenero());
        System.out.println("   Categoria (Java): " + produto.getCategoria());
        System.out.println("   Estoque: " + produto.getEstoque());
        System.out.println("   Preço: " + produto.getPreco());

        
        String tipoProduto = converterCategoriaParaBanco(produto.getCategoria());
        System.out.println("   Tipo Produto (Banco): " + tipoProduto);

        String sql = "INSERT INTO produtos (descricao, genero, estoque, preco, tipo_produto, caminho_imagem, " +
                    "descricao_detalhada, autor, editora, edicao, diretor, duracao, censura, artista, " +
                    "gravadora, pais_origem, imagem_bytes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
        
        stmt.setString(1, produto.getDescricao());
        stmt.setString(2, produto.getGenero());
        stmt.setInt(3, produto.getEstoque());
        stmt.setDouble(4, produto.getPreco());
        stmt.setString(5, tipoProduto); 
        stmt.setString(6, produto.getCaminhoImagem());
        stmt.setString(7, produto.getDescricaoDetalhada());
        
      
        if ("LIVRO".equals(tipoProduto) && produto instanceof Livro) {
            Livro livro = (Livro) produto;
            stmt.setString(8, livro.getAutor());
            stmt.setString(9, livro.getEditora());
            stmt.setString(10, livro.getEdicao());
            stmt.setString(11, null); // diretor
            stmt.setString(12, null); // duracao
            stmt.setString(13, null); // censura
            stmt.setString(14, null); // artista
            stmt.setString(15, null); // gravadora
            stmt.setString(16, null); // pais_origem
        } 
        else if ("DVD".equals(tipoProduto) && produto instanceof Dvd) {
            Dvd dvd = (Dvd) produto;
            stmt.setString(8, null); // autor
            stmt.setString(9, null); // editora
            stmt.setString(10, null); // edicao
            stmt.setString(11, dvd.getDiretor());
            stmt.setString(12, dvd.getDuracao());
            stmt.setString(13, dvd.getCensura());
            stmt.setString(14, null); // artista
            stmt.setString(15, null); // gravadora
            stmt.setString(16, null); // pais_origem
        }
        else if ("CD".equals(tipoProduto) && produto instanceof Cd) {
            Cd cd = (Cd) produto;
            stmt.setString(8, null); // autor
            stmt.setString(9, null); // editora
            stmt.setString(10, null); // edicao
            stmt.setString(11, null); // diretor
            stmt.setString(12, null); // duracao
            stmt.setString(13, null); // censura
            stmt.setString(14, cd.getArtista());
            stmt.setString(15, cd.getGravadora());
            stmt.setString(16, cd.getPaisDeOrigem()); 
        } else {
            
            for (int i = 8; i <= 16; i++) {
                stmt.setString(i, null);
            }
        }
        
        // Imagem
        if (imagemBytes != null && imagemBytes.length > 0) {
            stmt.setBytes(17, imagemBytes);
        } else {
            stmt.setNull(17, java.sql.Types.BLOB);
        }
        
        System.out.println("🚀 Executando INSERT...");
        int rowsAffected = stmt.executeUpdate();
        
        if (rowsAffected > 0) {
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idGerado = generatedKeys.getInt(1);
                System.out.println("🎉 SUCESSO! ID gerado: " + idGerado);
                return true;
            }
        }
        
        return false;
        
    } catch (SQLException e) {
        System.err.println("💥 ERRO SQL: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}


private static String converterCategoriaDoBanco(String categoriaBanco) {
    if (categoriaBanco == null) return "Livro";
    
    switch (categoriaBanco.toUpperCase()) {
        case "LIVRO":
            return "Livro";
        case "CD":
            return "CD";
        case "DVD":
            return "DVD";
        default:
            return "Livro"; // Default
    }
}

public static void testarConexaoProduto() {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        System.out.println("=== TESTE DE CONEXÃO E ESTRUTURA ===");
        
       
        String sql = "SELECT COUNT(*) as total FROM produtos";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            System.out.println("✅ Tabela produtos existe - Total: " + rs.getInt("total"));
        }
        
        
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet colunas = meta.getColumns(null, null, "produtos", null);
        System.out.println("📋 Colunas da tabela produtos:");
        while (colunas.next()) {
            System.out.println("   - " + colunas.getString("COLUMN_NAME") + 
                             " (" + colunas.getString("TYPE_NAME") + ")");
        }
        
    } catch (SQLException e) {
        System.err.println("❌ Erro no teste: " + e.getMessage());
    }
}


public static void verificarProdutoSalvo(int idProduto) {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idProduto);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            System.out.println("✅ VERIFICAÇÃO - PRODUTO ENCONTRADO NO BANCO:");
            System.out.println("   ID: " + rs.getInt("id"));
            System.out.println("   Descrição: " + rs.getString("descricao"));
            System.out.println("   Gênero: " + rs.getString("genero"));
            System.out.println("   Tipo Produto: " + rs.getString("tipo_produto"));
            System.out.println("   Preço: " + rs.getDouble("preco"));
            System.out.println("   Estoque: " + rs.getInt("estoque"));
            System.out.println("   Autor: " + rs.getString("autor"));
            System.out.println("   Diretor: " + rs.getString("diretor"));
            System.out.println("   Artista: " + rs.getString("artista"));
            System.out.println("   Imagem Bytes: " + (rs.getBytes("imagem_bytes") != null ? "SIM" : "NÃO"));
        } else {
            System.out.println("❌ VERIFICAÇÃO - PRODUTO NÃO ENCONTRADO NO BANCO");
        }
    } catch (SQLException e) {
        System.err.println("Erro ao verificar produto: " + e.getMessage());
    }
}

public static ArrayList<Produto> carregarProdutosDoBanco() {
    ArrayList<Produto> produtos = new ArrayList<>();
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        String sql = "SELECT * FROM produtos ORDER BY id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        int count = 0;
        while (rs.next()) {
            Produto produto = criarProdutoFromResultSet(rs);
            if (produto != null) {
                produtos.add(produto);
                count++;
                
               
                System.out.println("📦 Produto " + count + " carregado:");
                System.out.println("   ID: " + produto.getId());
                System.out.println("   Descrição: " + produto.getDescricao());
                System.out.println("   Categoria: " + produto.getCategoria());
                System.out.println("   Gênero: " + produto.getGenero());
                System.out.println("   Preço: " + produto.getPreco());
            }
        }
        
        System.out.println("✅ Total de produtos carregados do banco: " + produtos.size());
        
    } catch (SQLException e) {
        System.err.println("❌ Erro ao carregar produtos: " + e.getMessage());
        e.printStackTrace();
    }
    
    return produtos;
}
public static byte[] carregarImagemProduto(int idProduto) {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        String sql = "SELECT imagem_bytes FROM produtos WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idProduto);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getBytes("imagem_bytes");
        }
    } catch (SQLException e) {
        System.err.println("Erro ao carregar imagem do produto: " + e.getMessage());
        e.printStackTrace();
    }
    return null;
}

private static Produto criarProdutoFromResultSet(ResultSet rs) throws SQLException {
    String tipoProduto = rs.getString("tipo_produto");
    String descricao = rs.getString("descricao");
    String genero = rs.getString("genero");
    int estoque = rs.getInt("estoque");
    double preco = rs.getDouble("preco");
    String caminhoImagem = rs.getString("caminho_imagem");
    String descricaoDetalhada = rs.getString("descricao_detalhada");
    
    Produto produto = null;
    
    if (tipoProduto != null) {
      
        String categoriaJava = converterCategoriaDoBanco(tipoProduto);
        
        switch (tipoProduto.toUpperCase()) {
            case "LIVRO":
                produto = new Livro(descricao, genero, estoque, preco,
                                  rs.getString("autor"),
                                  rs.getString("editora"),
                                  rs.getString("edicao"),
                                  caminhoImagem, descricaoDetalhada);
                produto.setCategoria(categoriaJava); // Usar categoria convertida
                break;
            case "DVD":
                produto = new Dvd(descricao, genero, estoque, preco,
                                rs.getString("diretor"),
                                rs.getString("duracao"),
                                rs.getString("censura"),
                                caminhoImagem, descricaoDetalhada);
                produto.setCategoria(categoriaJava); // Usar categoria convertida
                break;
            case "CD":
                produto = new Cd(descricao, genero, estoque, preco,
                               rs.getString("artista"),
                               rs.getString("gravadora"),
                               rs.getString("pais_origem"),
                               caminhoImagem, descricaoDetalhada);
                produto.setCategoria(categoriaJava); // Usar categoria convertida
                break;
            default:
                produto = new Produto(descricao, genero, estoque, preco, categoriaJava, 
                                    caminhoImagem, descricaoDetalhada);
                break;
        }
    }
    
    if (produto != null) {
        produto.setId(rs.getInt("id"));
    }
    
    return produto;
}
public static void verificarSincronizacaoProdutos() {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        System.out.println("=== VERIFICAÇÃO DE SINCRONIZAÇÃO ===");
        
       
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet colunas = meta.getColumns(null, null, "produtos", null);
        
        System.out.println("Estrutura da tabela produtos:");
        boolean temTipoProduto = false;
        boolean temCategoria = false;
        
        while (colunas.next()) {
            String nomeColuna = colunas.getString("COLUMN_NAME");
            String tipoColuna = colunas.getString("TYPE_NAME");
            System.out.println(" - " + nomeColuna + " (" + tipoColuna + ")");
            
            if (nomeColuna.equalsIgnoreCase("tipo_produto")) {
                temTipoProduto = true;
            }
            if (nomeColuna.equalsIgnoreCase("categoria")) {
                temCategoria = true;
            }
        }
        
        System.out.println("Tem tipo_produto: " + temTipoProduto);
        System.out.println("Tem categoria: " + temCategoria);
        
        // Contar produtos por tipo
        String sqlCount = "SELECT tipo_produto, COUNT(*) as total FROM produtos GROUP BY tipo_produto";
        PreparedStatement stmt = conn.prepareStatement(sqlCount);
        ResultSet rs = stmt.executeQuery();
        
        System.out.println("Produtos no banco por tipo:");
        while (rs.next()) {
            System.out.println(" - " + rs.getString("tipo_produto") + ": " + rs.getInt("total"));
        }
        
    } catch (SQLException e) {
        System.err.println("Erro na verificação de sincronização: " + e.getMessage());
        e.printStackTrace();
    }
}

public static void verificarDadosAnalise() {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        System.out.println("=== VERIFICANDO DADOS PARA ANÁLISE ===");
        
     
        String sqlPedidos = "SELECT COUNT(*) as total FROM pedidos";
        PreparedStatement stmtPedidos = conn.prepareStatement(sqlPedidos);
        ResultSet rsPedidos = stmtPedidos.executeQuery();
        if (rsPedidos.next()) {
            System.out.println("Total de pedidos: " + rsPedidos.getInt("total"));
        }
        
      
        String sqlItens = "SELECT COUNT(*) as total FROM itens_pedido";
        PreparedStatement stmtItens = conn.prepareStatement(sqlItens);
        ResultSet rsItens = stmtItens.executeQuery();
        if (rsItens.next()) {
            System.out.println("Total de itens pedido: " + rsItens.getInt("total"));
        }
        
        
        String sqlProdutos = "SELECT nome_produto, SUM(quantidade) as total FROM itens_pedido GROUP BY nome_produto ORDER BY total DESC LIMIT 5";
        PreparedStatement stmtProdutos = conn.prepareStatement(sqlProdutos);
        ResultSet rsProdutos = stmtProdutos.executeQuery();
        System.out.println("Produtos mais vendidos:");
        while (rsProdutos.next()) {
            System.out.println(" - " + rsProdutos.getString("nome_produto") + ": " + rsProdutos.getInt("total"));
        }
        
    } catch (SQLException e) {
        System.err.println("Erro ao verificar dados: " + e.getMessage());
    }
}

    public static String obterTipoUsuario(String email) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT tipo_usuario FROM usuarios WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("tipo_usuario");
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao obter tipo de usuário: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "Cliente";
    }


    public static boolean testarConexao() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Erro na conexão com o banco: " + e.getMessage());
            return false;
        }
    }
    
    public static String obterEnderecoUsuario(String email) {
       
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT endereco FROM usuarios WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("endereco");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean atualizarPerfilUsuario(String email, String novoNome, String novoEndereco) {
       
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE usuarios SET nome_completo = ?, endereco = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, novoNome);
            stmt.setString(2, novoEndereco);
            stmt.setString(3, email);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

   
    public static void criarTabelasSeNecessario() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            
            // Tabela de usuários
            String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY," +
                                "nome_completo VARCHAR(100) NOT NULL," +
                                "email VARCHAR(100) UNIQUE NOT NULL," +
                                "senha VARCHAR(255) NOT NULL," +
                                "telefone VARCHAR(20)," +
                                "endereco TEXT," +
                                "tipo_usuario ENUM('Cliente', 'Admin') DEFAULT 'Cliente'," +
                                "data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                                ")";
            
            // Tabela de perfil do usuário 
            String sqlPerfil = "CREATE TABLE IF NOT EXISTS perfil_usuario (" +
                              "id INT AUTO_INCREMENT PRIMARY KEY," +
                              "id_usuario INT NOT NULL," +
                              "imagem_perfil LONGBLOB," +
                              "data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                              "FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE," +
                              "UNIQUE KEY (id_usuario)" +
                              ")";
            
            // Tabela de produtos
            String sqlProdutos = "CREATE TABLE IF NOT EXISTS produtos (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY," +
                                "descricao VARCHAR(100) NOT NULL," +
                                "categoria VARCHAR(50) NOT NULL," +
                                "preco DECIMAL(10,2) NOT NULL," +
                                "imagem VARCHAR(255)," +
                                "disponivel BOOLEAN DEFAULT TRUE" +
                                ")";
            
            // Tabela de carrinho do usuário
            String sqlCarrinho = "CREATE TABLE IF NOT EXISTS carrinho_usuario (" +
                                "id INT AUTO_INCREMENT PRIMARY KEY," +
                                "id_usuario INT NOT NULL," +
                                "id_produto INT NOT NULL," +
                                "quantidade INT NOT NULL DEFAULT 1," +
                                "nome_produto VARCHAR(100) NOT NULL," +
                                "categoria VARCHAR(50) NOT NULL," +
                                "preco DECIMAL(10,2) NOT NULL," +
                                "data_adicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                                "FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE" +
                                ")";
            
            // Tabela de pedidos
            String sqlPedidos = "CREATE TABLE IF NOT EXISTS pedidos (" +
                               "id INT AUTO_INCREMENT PRIMARY KEY," +
                               "codigo_pedido VARCHAR(20) UNIQUE NOT NULL," +
                               "id_usuario INT NOT NULL," +
                               "nome_cliente VARCHAR(100) NOT NULL," +
                               "endereco TEXT NOT NULL," +
                               "telefone VARCHAR(20) NOT NULL," +
                               "cpf VARCHAR(14) NOT NULL," +
                               "email VARCHAR(100) NOT NULL," +
                               "preco_total DECIMAL(10,2) NOT NULL," +
                               "status ENUM('Pendente', 'Pago', 'Preparando', 'Enviado', 'Entregue', 'Cancelado') DEFAULT 'Pendente'," +
                               "data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                               "FOREIGN KEY (id_usuario) REFERENCES usuarios(id)" +
                               ")";
            
            // Tabela de itens do pedido
            String sqlItensPedido = "CREATE TABLE IF NOT EXISTS itens_pedido (" +
                                   "id INT AUTO_INCREMENT PRIMARY KEY," +
                                   "codigo_pedido VARCHAR(20) NOT NULL," +
                                   "id_produto INT NOT NULL," +
                                   "nome_produto VARCHAR(100) NOT NULL," +
                                   "quantidade INT NOT NULL," +
                                   "preco_unitario DECIMAL(10,2) NOT NULL," +
                                   "preco_total DECIMAL(10,2) NOT NULL," +
                                   "FOREIGN KEY (codigo_pedido) REFERENCES pedidos(codigo_pedido) ON DELETE CASCADE" +
                                   ")";
            
            PreparedStatement stmtUsuarios = conn.prepareStatement(sqlUsuarios);
            stmtUsuarios.execute();
            
            PreparedStatement stmtPerfil = conn.prepareStatement(sqlPerfil);
            stmtPerfil.execute();
            
            PreparedStatement stmtProdutos = conn.prepareStatement(sqlProdutos);
            stmtProdutos.execute();
            
            PreparedStatement stmtCarrinho = conn.prepareStatement(sqlCarrinho);
            stmtCarrinho.execute();
            
            PreparedStatement stmtPedidos = conn.prepareStatement(sqlPedidos);
            stmtPedidos.execute();
            
            PreparedStatement stmtItensPedido = conn.prepareStatement(sqlItensPedido);
            stmtItensPedido.execute();
            
            // Inserir administradores padrão
            inserirAdministradoresPadrao(conn);
            
            System.out.println("✓ Tabelas verificadas/criadas com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao criar tabelas: " + e.getMessage());
            e.printStackTrace();
        }
    }
 

public static Map<String, Object> obterDadosAnalise() {
    Map<String, Object> dados = new HashMap<>();
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
     
        String sqlVendas = "SELECT SUM(preco_total) as total FROM pedidos WHERE status != 'Cancelado'";
        PreparedStatement stmtVendas = conn.prepareStatement(sqlVendas);
        ResultSet rsVendas = stmtVendas.executeQuery();
        double totalVendas = 0;
        if (rsVendas.next()) {
            totalVendas = rsVendas.getDouble("total");
        }
        dados.put("totalVendas", totalVendas);
        
       
        String sqlPedidos = "SELECT COUNT(*) as total FROM pedidos";
        PreparedStatement stmtPedidos = conn.prepareStatement(sqlPedidos);
        ResultSet rsPedidos = stmtPedidos.executeQuery();
        int totalPedidos = 0;
        if (rsPedidos.next()) {
            totalPedidos = rsPedidos.getInt("total");
        }
        dados.put("totalPedidos", totalPedidos);
        
       
        String sqlUsuarios = "SELECT COUNT(*) as total FROM usuarios";
        PreparedStatement stmtUsuarios = conn.prepareStatement(sqlUsuarios);
        ResultSet rsUsuarios = stmtUsuarios.executeQuery();
        int totalUsuarios = 0;
        if (rsUsuarios.next()) {
            totalUsuarios = rsUsuarios.getInt("total");
        }
        dados.put("totalUsuarios", totalUsuarios);
        
    
        double ticketMedio = totalPedidos > 0 ? totalVendas / totalPedidos : 0;
        dados.put("ticketMedio", ticketMedio);
        
        // Pedidos deste mês
        String sqlPedidosMes = "SELECT COUNT(*) as total FROM pedidos WHERE MONTH(data_pedido) = MONTH(CURRENT_DATE()) AND YEAR(data_pedido) = YEAR(CURRENT_DATE())";
        PreparedStatement stmtPedidosMes = conn.prepareStatement(sqlPedidosMes);
        ResultSet rsPedidosMes = stmtPedidosMes.executeQuery();
        int pedidosEsteMes = 0;
        if (rsPedidosMes.next()) {
            pedidosEsteMes = rsPedidosMes.getInt("total");
        }
        dados.put("pedidosEsteMes", pedidosEsteMes);
        
        // Total de produtos vendidos
        String sqlProdutos = "SELECT SUM(quantidade) as total FROM itens_pedido ip JOIN pedidos p ON ip.codigo_pedido = p.codigo_pedido WHERE p.status != 'Cancelado'";
        PreparedStatement stmtProdutos = conn.prepareStatement(sqlProdutos);
        ResultSet rsProdutos = stmtProdutos.executeQuery();
        int totalProdutosVendidos = 0;
        if (rsProdutos.next()) {
            totalProdutosVendidos = rsProdutos.getInt("total");
        }
        dados.put("totalProdutosVendidos", totalProdutosVendidos);
        
        // Status dos pedidos
        Map<String, Integer> statusPedidos = new HashMap<>();
        String sqlStatus = "SELECT status, COUNT(*) as total FROM pedidos GROUP BY status";
        PreparedStatement stmtStatus = conn.prepareStatement(sqlStatus);
        ResultSet rsStatus = stmtStatus.executeQuery();
        while (rsStatus.next()) {
            statusPedidos.put(rsStatus.getString("status"), rsStatus.getInt("total"));
        }
        dados.put("statusPedidos", statusPedidos);
        
      
        Map<String, Double> vendasPorMes = new HashMap<>();
        String sqlVendasMes = "SELECT DATE_FORMAT(data_pedido, '%Y-%m') as mes, SUM(preco_total) as total " +
                             "FROM pedidos WHERE data_pedido >= DATE_SUB(CURRENT_DATE(), INTERVAL 6 MONTH) " +
                             "AND status != 'Cancelado' GROUP BY mes ORDER BY mes";
        PreparedStatement stmtVendasMes = conn.prepareStatement(sqlVendasMes);
        ResultSet rsVendasMes = stmtVendasMes.executeQuery();
        while (rsVendasMes.next()) {
            vendasPorMes.put(rsVendasMes.getString("mes"), rsVendasMes.getDouble("total"));
        }
        dados.put("vendasPorMes", vendasPorMes);
        
    } catch (SQLException e) {
        System.err.println("Erro ao obter dados de análise: " + e.getMessage());
        e.printStackTrace();
    }
    
    return dados;
}


public static List<Map<String, Object>> obterProdutosMaisVendidos() {
    List<Map<String, Object>> produtos = new ArrayList<>();
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        String sql = "SELECT ip.nome_produto, SUM(ip.quantidade) as quantidade_total, " +
                    "SUM(ip.preco_total) as total_arrecadado, ip.categoria " +
                    "FROM itens_pedido ip " +
                    "LEFT JOIN pedidos p ON ip.codigo_pedido = p.codigo_pedido " +
                    "WHERE p.status IS NULL OR p.status != 'Cancelado' " +
                    "GROUP BY ip.nome_produto, ip.categoria " +
                    "ORDER BY quantidade_total DESC LIMIT 10";
        
        System.out.println("Executando query produtos mais vendidos...");
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        int count = 0;
        while (rs.next()) {
            Map<String, Object> produto = new HashMap<>();
            produto.put("nome_produto", rs.getString("nome_produto"));
            produto.put("quantidade_total", rs.getInt("quantidade_total"));
            produto.put("total_arrecadado", rs.getDouble("total_arrecadado"));
            produto.put("categoria", rs.getString("categoria"));
            produtos.add(produto);
            count++;
            
            System.out.println("Produto encontrado: " + rs.getString("nome_produto") + 
                             " - Qtd: " + rs.getInt("quantidade_total"));
        }
        
        System.out.println("Total de produtos encontrados: " + count);
        
        // SE NENHUM PRODUTO FOR ENCONTRADO, RETORNA DADOS DE EXEMPLO
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado, retornando dados exemplo...");
            return criarDadosExemploProdutos();
        }
        
    } catch (SQLException e) {
        System.err.println("Erro ao obter produtos mais vendidos: " + e.getMessage());
        e.printStackTrace();
        // Em caso de erro, retorna dados exemplo
        return criarDadosExemploProdutos();
    }
    
    return produtos;
}


private static List<Map<String, Object>> criarDadosExemploProdutos() {
    List<Map<String, Object>> produtos = new ArrayList<>();
    
    String[][] dadosExemplo = {
        {"Dom Casmurro", "45", "1345.50", "Romance"},
        {"1984", "38", "1326.20", "Ficção Científica"},
        {"O Pequeno Príncipe", "32", "796.80", "Infantil"},
        {"Harry Potter e a Pedra Filosofal", "28", "1117.20", "Fantasia"},
        {"A Culpa é das Estrelas", "25", "822.50", "Romance"}
    };
    
    for (String[] dados : dadosExemplo) {
        Map<String, Object> produto = new HashMap<>();
        produto.put("nome_produto", dados[0]);
        produto.put("quantidade_total", Integer.parseInt(dados[1]));
        produto.put("total_arrecadado", Double.parseDouble(dados[2]));
        produto.put("categoria", dados[3]);
        produtos.add(produto);
    }
    
    return produtos;
}

public static Map<String, Integer> obterEstatisticasCategorias() {
    Map<String, Integer> categorias = new HashMap<>();
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
       
        String sqlVerifica = "SELECT COUNT(*) as total FROM itens_pedido";
        PreparedStatement stmtVerifica = conn.prepareStatement(sqlVerifica);
        ResultSet rsVerifica = stmtVerifica.executeQuery();
        
        int totalItens = 0;
        if (rsVerifica.next()) {
            totalItens = rsVerifica.getInt("total");
        }
        
        System.out.println("Total de itens na tabela itens_pedido: " + totalItens);
        
        if (totalItens > 0) {
            // Query para obter distribuição por categoria
            String sql = "SELECT categoria, COUNT(*) as total_produtos " +
                        "FROM itens_pedido " +
                        "WHERE categoria IS NOT NULL AND categoria != '' " +
                        "GROUP BY categoria " +
                        "ORDER BY total_produtos DESC";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                String categoria = rs.getString("categoria");
                int total = rs.getInt("total_produtos");
                categorias.put(categoria, total);
                count++;
                System.out.println("Categoria encontrada: " + categoria + " - " + total + " produtos");
            }
            
            System.out.println("Total de categorias encontradas: " + count);
            
           
            if (categorias.isEmpty()) {
                System.out.println("Nenhuma categoria encontrada, usando categorias alternativas...");
                
                
                String sqlFallback = "SELECT 'Livros' as categoria, COUNT(DISTINCT nome_produto) as total_produtos FROM itens_pedido";
                PreparedStatement stmtFallback = conn.prepareStatement(sqlFallback);
                ResultSet rsFallback = stmtFallback.executeQuery();
                
                if (rsFallback.next()) {
                    categorias.put("Livros", rsFallback.getInt("total_produtos"));
                    System.out.println("Categoria fallback: Livros - " + rsFallback.getInt("total_produtos") + " produtos");
                }
            }
        } else {
            System.out.println("Tabela itens_pedido vazia, retornando categorias exemplo");
           
            categorias.put("Romance", 8);
            categorias.put("Ficção Científica", 6);
            categorias.put("Fantasia", 7);
            categorias.put("Infantil", 4);
            categorias.put("Clássico", 3);
        }
        
    } catch (SQLException e) {
        System.err.println("Erro ao obter estatísticas de categorias: " + e.getMessage());
        e.printStackTrace();
       
        categorias.put("Romance", 5);
        categorias.put("Ficção", 3);
        categorias.put("Fantasia", 4);
    }
    
    return categorias;
}


public static Map<String, Object> obterEstatisticasUsuarios() {
    Map<String, Object> stats = new HashMap<>();
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        
        String sqlAdmins = "SELECT COUNT(*) as total FROM usuarios WHERE tipo_usuario = 'Admin'";
        PreparedStatement stmtAdmins = conn.prepareStatement(sqlAdmins);
        ResultSet rsAdmins = stmtAdmins.executeQuery();
        stats.put("totalAdmins", rsAdmins.next() ? rsAdmins.getInt("total") : 0);
        
    
        String sqlClientes = "SELECT COUNT(*) as total FROM usuarios WHERE tipo_usuario = 'Cliente'";
        PreparedStatement stmtClientes = conn.prepareStatement(sqlClientes);
        ResultSet rsClientes = stmtClientes.executeQuery();
        stats.put("totalClientes", rsClientes.next() ? rsClientes.getInt("total") : 0);
        
     
        String sqlNovos = "SELECT COUNT(*) as total FROM usuarios WHERE MONTH(data_cadastro) = MONTH(CURRENT_DATE()) AND YEAR(data_cadastro) = YEAR(CURRENT_DATE())";
        PreparedStatement stmtNovos = conn.prepareStatement(sqlNovos);
        ResultSet rsNovos = stmtNovos.executeQuery();
        stats.put("novosUsuariosMes", rsNovos.next() ? rsNovos.getInt("total") : 0);
       
    
        String sqlComPedidos = "SELECT COUNT(DISTINCT id_usuario) as total FROM pedidos";
        PreparedStatement stmtComPedidos = conn.prepareStatement(sqlComPedidos);
        ResultSet rsComPedidos = stmtComPedidos.executeQuery();
        stats.put("usuariosComPedidos", rsComPedidos.next() ? rsComPedidos.getInt("total") : 0);
        
    } catch (SQLException e) {
        System.err.println("Erro ao obter estatísticas de usuários: " + e.getMessage());
        e.printStackTrace();
    }
    
    return stats;
}

public static List<Map<String, Object>> obterUsuariosMaisAtivos() {
    List<Map<String, Object>> usuarios = new ArrayList<>();
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        String sql = "SELECT u.nome_completo, u.email, COUNT(p.id) as total_pedidos, SUM(p.preco_total) as total_gasto " +
                    "FROM usuarios u JOIN pedidos p ON u.id = p.id_usuario " +
                    "WHERE p.status != 'Cancelado' " +
                    "GROUP BY u.id, u.nome_completo, u.email " +
                    "ORDER BY total_gasto DESC LIMIT 10";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Map<String, Object> usuario = new HashMap<>();
            usuario.put("nome_completo", rs.getString("nome_completo"));
            usuario.put("email", rs.getString("email"));
            usuario.put("total_pedidos", rs.getInt("total_pedidos"));
            usuario.put("total_gasto", rs.getDouble("total_gasto"));
            usuarios.add(usuario);
        }
        
    } catch (SQLException e) {
        System.err.println("Erro ao obter usuários mais ativos: " + e.getMessage());
        e.printStackTrace();
    }
    
    return usuarios;
}
public static void verificarTabelaProdutos() {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet tables = meta.getTables(null, null, "produtos", null);
        
        if (tables.next()) {
            System.out.println("✅ Tabela produtos existe");
            
            
            ResultSet colunas = meta.getColumns(null, null, "produtos", null);
            System.out.println("Colunas da tabela produtos:");
            while (colunas.next()) {
                System.out.println(" - " + colunas.getString("COLUMN_NAME") + 
                                " (" + colunas.getString("TYPE_NAME") + ")");
            }
        } else {
            System.out.println("❌ Tabela produtos não existe");
        }
    } catch (SQLException e) {
        System.err.println("Erro ao verificar tabela: " + e.getMessage());
    }
}

public static Map<String, Object> obterEstatisticasPedidos() {
    Map<String, Object> stats = new HashMap<>();
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
        // Pedidos entregues
        String sqlEntregues = "SELECT COUNT(*) as total FROM pedidos WHERE status = 'Entregue'";
        PreparedStatement stmtEntregues = conn.prepareStatement(sqlEntregues);
        ResultSet rsEntregues = stmtEntregues.executeQuery();
        stats.put("pedidosEntregues", rsEntregues.next() ? rsEntregues.getInt("total") : 0);
        
        // Pedidos pendentes
        String sqlPendentes = "SELECT COUNT(*) as total FROM pedidos WHERE status IN ('Pendente', 'Pago', 'Preparando', 'Enviado')";
        PreparedStatement stmtPendentes = conn.prepareStatement(sqlPendentes);
        ResultSet rsPendentes = stmtPendentes.executeQuery();
        stats.put("pedidosPendentes", rsPendentes.next() ? rsPendentes.getInt("total") : 0);
        
        // Pedidos cancelados
        String sqlCancelados = "SELECT COUNT(*) as total FROM pedidos WHERE status = 'Cancelado'";
        PreparedStatement stmtCancelados = conn.prepareStatement(sqlCancelados);
        ResultSet rsCancelados = stmtCancelados.executeQuery();
        stats.put("pedidosCancelados", rsCancelados.next() ? rsCancelados.getInt("total") : 0);
        
        // Maior pedido
        String sqlMaior = "SELECT MAX(preco_total) as maximo FROM pedidos WHERE status != 'Cancelado'";
        PreparedStatement stmtMaior = conn.prepareStatement(sqlMaior);
        ResultSet rsMaior = stmtMaior.executeQuery();
        stats.put("maiorPedido", rsMaior.next() ? rsMaior.getDouble("maximo") : 0);
        
        // Pedidos hoje
        String sqlHoje = "SELECT COUNT(*) as total FROM pedidos WHERE DATE(data_pedido) = CURRENT_DATE()";
        PreparedStatement stmtHoje = conn.prepareStatement(sqlHoje);
        ResultSet rsHoje = stmtHoje.executeQuery();
        stats.put("pedidosHoje", rsHoje.next() ? rsHoje.getInt("total") : 0);
        
        // Taxa de conclusão
        String sqlTotal = "SELECT COUNT(*) as total FROM pedidos";
        PreparedStatement stmtTotal = conn.prepareStatement(sqlTotal);
        ResultSet rsTotal = stmtTotal.executeQuery();
        int totalPedidos = rsTotal.next() ? rsTotal.getInt("total") : 1;
        int entregues = (int) stats.get("pedidosEntregues");
        double taxaConclusao = (double) entregues / totalPedidos * 100;
        stats.put("taxaConclusao", taxaConclusao);
        
        // Evolução de pedidos 
        Map<String, Integer> evolucao = new HashMap<>();
        String sqlEvolucao = "SELECT DATE_FORMAT(data_pedido, '%Y-%m') as mes, COUNT(*) as total " +
                            "FROM pedidos WHERE data_pedido >= DATE_SUB(CURRENT_DATE(), INTERVAL 6 MONTH) " +
                            "GROUP BY mes ORDER BY mes";
        PreparedStatement stmtEvolucao = conn.prepareStatement(sqlEvolucao);
        ResultSet rsEvolucao = stmtEvolucao.executeQuery();
        while (rsEvolucao.next()) {
            evolucao.put(rsEvolucao.getString("mes"), rsEvolucao.getInt("total"));
        }
        stats.put("evolucaoPedidos", evolucao);
        
    } catch (SQLException e) {
        System.err.println("Erro ao obter estatísticas de pedidos: " + e.getMessage());
        e.printStackTrace();
    }
    
    return stats;
}
}