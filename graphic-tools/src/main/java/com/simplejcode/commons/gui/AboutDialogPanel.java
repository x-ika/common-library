package com.simplejcode.commons.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

public class AboutDialogPanel extends JPanel {

    private final String name;
    private final String version;
    private final String author;

    private Image logo;
    private BufferedImage content;

    public AboutDialogPanel(String name, Image logo) {
        this(name, "1.0", "Irakli Merabishvili", logo);
    }

    public AboutDialogPanel(String name, String version, String author, Image logo) {
        this.name = name;
        this.author = author;
        this.version = version;
        this.logo = logo;
    }

    public synchronized void init() {
        if (content != null) {
            return;
        }
        content = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = content.createGraphics();
        g.setColor(new Color(150, 200, 255));
        g.fillRect(0, 0, 400, 120);
        g.setColor(Color.black);
        g.setFont(new Font("Curier", Font.PLAIN, 14));
        g.drawString("Program Version " + version, 10, 20);
        g.drawString("© " + author, 10, 50);
        g.drawString("All rights reserved.", 10, 100);

        Point p1 = new Point(0, 120);
        Point p2 = new Point(400, 130);

        Paint mem = g.getPaint();
        g.setPaint(new GradientPaint(p1, Color.yellow, p2, Color.blue));
        g.fillRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
        g.setPaint(mem);

        g.setColor(new Color(0xFFFAEBD7));
        int x = 0, y = 130, w = 400, h = 170;
        g.fillRect(x, y, w, h);


        g.setFont(new Font("Arial", Font.BOLD, 80));
        Rectangle2D r = g.getFontMetrics().getStringBounds(name, g);
        for (int i = 2; i < 3; i++) {
            g.setColor(new Color(0, 0, 0, 100));
            g.drawString(name, (int) (x + 2 * i + (w - r.getWidth()) / 2), y + 3 * i + h / 2);
        }
        g.setColor(new Color(100, 0, 100));
        g.drawString(name, (int) (x + (w - r.getWidth()) / 2), y + h / 2);

        if (logo == null) {
            try {
                logo = ImageIO.read(new File("resources/logo.jpeg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        g.drawImage(logo, 200, 10, new Canvas());
    }

    public void showDialog(Frame owner) {
        JDialog about = new JDialog(owner, "About Program", true);
        about.setContentPane(this);
        about.setSize(getSize());
        about.setResizable(false);
        GraphicUtils.centerOnScreen(about);
        about.setVisible(true);
    }

    public Dimension getSize() {
        return new Dimension(content.getWidth(), content.getHeight());
    }

    public void paint(Graphics gr) {
        gr.drawImage(content, 0, 0, this);
    }

}
