/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

public class Usuario {

    private int id;
    private String nomeCompleto;
    private String email;
    private String senha;
    private String endereco;
   

   
    public Usuario(String nomeCompleto, String email, String senha, String endereco) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.endereco = endereco;
    }

    
    public Usuario(int id, String nomeCompleto, String email, String senha, String endereco) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}