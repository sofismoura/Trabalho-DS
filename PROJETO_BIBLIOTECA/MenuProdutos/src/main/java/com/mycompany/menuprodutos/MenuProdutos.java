/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.menuprodutos;

import javax.swing.SwingUtilities;
import novoPack.BancoDeDados;

public class MenuProdutos {

    public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Menu_Produtos tela = new Menu_Produtos();
                tela.setVisible(true);
                // Verificar e criar tabelas se necessário
                BancoDeDados.criarTabelasSeNecessario();
            }
        });
    }
        
        
}