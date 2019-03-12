package com.simplejcode.commons.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private BufferedImage image;

    public ImagePanel() {
    }

    public ImagePanel(BufferedImage image) {
        setImage(image);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        if (image != null) {
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

}
