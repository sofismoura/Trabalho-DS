/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

class RoundedPanel3 extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;
    private Color borderColor;

    public RoundedPanel3(int cornerRadius, Color backgroundColor, Color borderColor) {
        this.cornerRadius = cornerRadius;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      
        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

       
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2)); 
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

        g2.dispose();
    }
}