package com.simplejcode.commons.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.util.*;
import java.util.List;

public class DesktopFrame extends CustomFrame {

    private BufferedImage background, foreground;

    protected List<JInternalFrame> internalFrames = new ArrayList<>();

    public DesktopFrame(String title, BufferedImage background, BufferedImage foreground) {
        super(title);
        this.background = background;
        this.foreground = foreground;
    }

    public Container createContentPane() {
        return new JDesktopPane() {

            private BufferedImage image;

            public void paintComponent(Graphics g) {
                if (image == null || image.getWidth() != getWidth() || image.getHeight() != getHeight()) {
                    image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D gr = image.createGraphics();

                    if (background != null) {
                        gr.setPaint(new TexturePaint(background, new Rectangle(0, 0, background.getWidth(), background.getHeight())));
                        gr.fill(new Rectangle(0, 0, getWidth(), getHeight()));
                    }

                    if (foreground != null) {
                        gr.drawImage(foreground, getWidth() - foreground.getWidth() >> 1, getHeight() - foreground.getHeight() >> 1, this);
                    }
                }
                g.drawImage(image, 0, 0, this);
            }
        };
    }

    //-----------------------------------------------------------------------------------

    public <T extends JInternalFrame> T getComponent(Class<T> type) {
        for (JInternalFrame frame : internalFrames) {
            if (frame.getClass() == type) {
                return type.cast(frame);
            }
        }
        return null;
    }

    public void addInternalFrame(JInternalFrame frame, boolean open) {
        add(frame);
        GraphicUtils.centerOnParent(frame);
        try {
            frame.setIcon(true);
            if (open) {
                frame.setIcon(false);
            }
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        internalFrames.add(frame);
    }

    public void removeInternalFrame(JInternalFrame frame) {
        remove(frame);
    }

    public void removeAll() {
        for (JInternalFrame frame : internalFrames) {
            removeInternalFrame(frame);
        }
        internalFrames.clear();
    }

}
