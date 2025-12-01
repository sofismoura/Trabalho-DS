/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import java.util.ArrayList;
import java.util.List;

class MenuConfig extends Menu_Produtos {
    private Menu menuObjeto;

    protected MenuConfig() {
        
        Produto produtoDummy = new Produto("Produto Padrão", "Geral", 10, 19.99, "Outros", "caminho/padrao.png", "Descrição detalhada padrão.");
        this.menuObjeto = new Menu(produtoDummy, this); 
    }

    public Menu getMenuObjeto() {
        return menuObjeto;
    }

    public int executar() {
       
        System.out.println("MenuConfig está executando.");
        return 0;
    }
}