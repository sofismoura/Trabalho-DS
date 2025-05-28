/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package novoPack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

/**
 *
 * @author dti
 */
public class BancoDeDados {

    public static void salvarCliente(String nome, String endereco, String telefone, String cpf, String email) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/menuprodutos", "root", "")) {
            String sql = "INSERT INTO clientes (nome_completo, endereco, telefone, cpf, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, endereco);
            stmt.setString(3, telefone);
            stmt.setString(4, cpf);
            stmt.setString(5, email);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Dados salvos com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar no banco: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}

