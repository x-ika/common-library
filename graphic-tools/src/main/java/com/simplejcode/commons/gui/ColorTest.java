package com.simplejcode.commons.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ColorTest extends JPanel implements AdjustmentListener {
    private Color c;
    private JScrollBar sbRed, sbGreen, sbBlue, sbA, sbV;

    public ColorTest() {
        super(null);
        sbRed = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 256);
        sbRed.setBackground(Color.red);
        sbRed.setBounds(40, 30, 20, 200);
        sbRed.addAdjustmentListener(this);
        add(sbRed);
        sbGreen = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 256);
        sbGreen.setBackground(Color.green);
        sbGreen.setBounds(70, 30, 20, 200);
        sbGreen.addAdjustmentListener(this);
        add(sbGreen);
        sbBlue = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 256);
        sbBlue.setBackground(Color.blue);
        sbBlue.setBounds(100, 30, 20, 200);
        sbBlue.addAdjustmentListener(this);
        add(sbBlue);
        sbA = new JScrollBar(Scrollbar.VERTICAL, 255, 1, 0, 256);
        sbA.setBackground(Color.white);
        sbA.setBounds(130, 30, 20, 200);
        sbA.addAdjustmentListener(this);
        add(sbA);
        sbV = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 50);
        sbV.setBackground(Color.white);
        sbV.setBounds(10, 30, 20, 200);
        sbV.addAdjustmentListener(this);
        add(sbV);
    }

    public Dimension getPreferredSize() {
        return new Dimension(400, 320);
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        int red = sbRed.getValue();
        int green = sbGreen.getValue();
        int blue = sbBlue.getValue();
        int a = sbA.getValue();
        c = new Color(red, green, blue, a);
        g.setColor(c);
        g.fillRect(200, 50, 150, 150);
        g.setColor(Color.black);
        g.setFont(new Font("Serif", Font.PLAIN, 20));
        g.drawString("RED : " + String.valueOf(red), 200, 220);
        g.drawString("GREEN : " + String.valueOf(green), 200, 250);
        g.drawString("BLUE : " + String.valueOf(blue), 200, 280);
        g.drawString("ALPHA : " + String.valueOf(a), 200, 310);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        repaint();
    }
}
