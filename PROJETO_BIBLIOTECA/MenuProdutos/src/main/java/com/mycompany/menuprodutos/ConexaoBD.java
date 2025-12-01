/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static final String URL = "jdbc:mysql://localhost:3306/minhalivraria";
    private static final String USUARIO = "seu_usuario";
    private static final String SENHA = "sua_senha";
    private static ConexaoBD instancia;

   
    private ConexaoBD() {
        try {
            // Carrega o driver JDBC para MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC não encontrado. Verifique se o conector está no classpath.");
            e.printStackTrace();
        }
    }

    public static synchronized ConexaoBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoBD();
        }
        return instancia;
    }
    public Connection obterConexao() {
        Connection conexao = null;
        try {
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
        return conexao;
    }

    public void fecharConexao(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("Conexão com o banco de dados fechada.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
