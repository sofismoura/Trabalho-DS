/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;


import java.util.Objects;

public class Produto {
    private int id; // NOVO CAMPO
    private String descricao;
    private String genero;
    private int estoque;
    private double preco;
    private String categoria;
    private String caminhoImagem;
    private String descricaoDetalhada;

  
    public Produto() {
    }

    // Construtor completo
    public Produto(String descricao, String genero, int estoque, double preco, String categoria, String caminhoImagem, String descricaoDetalhada) {
        this.descricao = descricao;
        this.genero = genero;
        this.estoque = estoque;
        this.preco = preco;
        this.categoria = categoria;
        this.caminhoImagem = caminhoImagem;
        this.descricaoDetalhada = descricaoDetalhada;
    }

    // GETTERS E SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
    
   public String getTipoProduto() {
    return this.categoria;
    }
    

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public String getDescricaoDetalhada() {
        return descricaoDetalhada;
    }

    public void setDescricaoDetalhada(String descricaoDetalhada) {
        this.descricaoDetalhada = descricaoDetalhada;
    }

    public String exibirInformacoes() {
        return "Descrição: " + descricao +
               "\nCategoria: " + categoria +
               "\nGênero: " + genero +
               "\nEstoque: " + estoque +
               "\nPreço: R$" + String.format("%.2f", preco);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produto produto = (Produto) obj;
        return Objects.equals(descricao, produto.descricao) &&
               Objects.equals(categoria, produto.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descricao, categoria);
    }
}