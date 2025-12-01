/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

public class SessaoUsuario {
    private static String emailUsuario;
    private static String nomeUsuario;
    private static int idUsuario;
    private static boolean logado = false;

    public static void login(String email, String nome, int id) {
        emailUsuario = email;
        nomeUsuario = nome;
        idUsuario = id;
        logado = true;
        System.out.println("Sessão iniciada para: " + nome + " (" + email + ")");
    }

    public static void logout() {
        emailUsuario = null;
        nomeUsuario = null;
        idUsuario = -1;
        logado = false;
        System.out.println("Sessão encerrada");
    }

    public static boolean isLogado() {
        return logado;
    }

    public static String getEmailUsuario() {
        return emailUsuario;
    }

    public static String getNomeUsuario() {
        return nomeUsuario;
    }

    public static int getIdUsuario() {
        return idUsuario;
    }
    public static void setNomeUsuario(String novoNome) {
        nomeUsuario = novoNome;
    }
    
}