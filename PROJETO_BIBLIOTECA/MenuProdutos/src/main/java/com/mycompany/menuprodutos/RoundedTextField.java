/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.menuprodutos;

import javax.swing.*;
import javax.swing.border.EmptyBorder; 
import java.awt.*;
import java.awt.geom.RoundRectangle2D; 


public class RoundedTextField extends JTextField {

    private Shape shape; 
    private int cornerRadius = 15; 
    private Color borderColor = Color.GRAY; 
    private int borderWidth = 1; 

    public RoundedTextField(int columns) {
        super(columns);
        setBorder(new EmptyBorder(5, cornerRadius, 5, cornerRadius));
        setOpaque(false); 
    }

   
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
       
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

       
        super.paintComponent(g2);

        g2.dispose(); 
    }

    
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
       
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(borderWidth));

      
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose(); 
    }
    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
           
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }
        return shape.contains(x, y); 
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        setBorder(new EmptyBorder(5, cornerRadius, 5, cornerRadius)); 
        repaint(); 
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint(); 
    }
}
