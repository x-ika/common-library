package com.simplejcode.commons.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GameWindow extends Frame implements KeyListener, MouseMotionListener {

    private static final int W = 1280, H = 1024;

    private boolean repaint = true;
    private final BufferedImage buffer;

    public GameWindow() {
        super("Game");

        setUndecorated(true);
        setResizable(false);
        setIgnoreRepaint(true);
        GraphicUtils.getDevice().setFullScreenWindow(this);
        GraphicUtils.getDevice().setDisplayMode(new DisplayMode(W, H, 32, 60));

        buffer = GraphicUtils.getDevice().getDefaultConfiguration().createCompatibleImage(W, H);

        new Thread(() -> {
            while (isVisible()) {
                if (repaint) {
                    render(buffer.createGraphics());
                    getGraphics().drawImage(buffer, 0, 0, null);
                    Thread.yield();
                }
            }
        }).start();

        addKeyListener(this);
        addMouseMotionListener(this);
    }

    private void render(Graphics2D g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, W, H);

        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            g.fillOval(
                    r.nextInt(getWidth()), r.nextInt(getHeight()),
                    r.nextInt(10) + 10, r.nextInt(10) + 10);
        }

        g.setColor(Color.black);
        g.fillRect(x, y, w, h);

        g.dispose();
    }

    int x = 100, y = 100, w = 200, h = 200;

    private void adjustXY(int code) {
        int d = 1;
        if (code == KeyEvent.VK_LEFT) {
            x -= d;
        }
        if (code == KeyEvent.VK_RIGHT) {
            x += d;
        }
        if (code == KeyEvent.VK_UP) {
            y -= d;
        }
        if (code == KeyEvent.VK_DOWN) {
            y += d;
        }

        repaint = true;
    }

    public void mouseDragged(MouseEvent e) {
        x = e.getXOnScreen() - w / 2;
        y = e.getYOnScreen() - h / 2;

        repaint = true;
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dispose();
        } else {
            adjustXY(e.getKeyCode());
        }
    }

    public void keyReleased(KeyEvent e) {
    }

}
