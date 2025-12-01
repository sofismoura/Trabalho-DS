/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    private ConexaoBD conexaoBD;

    public UsuarioDAO() {
        this.conexaoBD = ConexaoBD.getInstancia();
    }
    public boolean cadastrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome_completo, email, senha, endereco) VALUES (?, ?, ?, ?)";
        
        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getEndereco());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            return false;
        }
    }

    public boolean validarLogin(String email, String senha) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ? AND senha = ?";
        
        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao validar login: " + e.getMessage());
        }
        return false;
    }
    
    public Usuario buscarUsuario(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        
        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nomeCompleto = rs.getString("nome_completo");
                    String senha = rs.getString("senha");
                    String endereco = rs.getString("endereco");
                    
                    return new Usuario(nomeCompleto, email, senha, endereco);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
        }
        return null;
    }
    public boolean atualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome_completo = ?, endereco = ? WHERE email = ?";
        
        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getEndereco());
            stmt.setString(3, usuario.getEmail()); 
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }
    public boolean deletarUsuario(String email) {
        String sql = "DELETE FROM usuarios WHERE email = ?";
        
        try (Connection conexao = conexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
            return false;
        }
    }
}