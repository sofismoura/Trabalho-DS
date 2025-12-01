/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {

    private int borderRadius;

    public RoundedPanel(int borderRadius) {
        this.borderRadius = borderRadius;
        setOpaque(false); 
        setBorder(new EmptyBorder(10, 10, 10, 10)); 
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), borderRadius, borderRadius));
        g2.setColor(getForeground());
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius)); // Desenha a borda
        g2.dispose();
    }
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        repaint(); 
    }
    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
        repaint(); 
    }
}