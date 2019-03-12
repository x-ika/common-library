package com.simplejcode.commons.gui;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

public class CustomFrame extends JFrame {

    public CustomFrame(String title) {
        super(title);
        setContentPane(createContentPane());
    }

    protected Container createContentPane() {
        Color bgColor = Color.white;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bgColor);
        panel.setPreferredSize(new Dimension(900, 600));
        return panel;
    }

    public void setContentPane(Container pane) {
        super.setContentPane(pane);
        pack();
        invalidate();
        centerOnScreen();
    }

    public void setJMenuBar(JMenuBar menuBar) {
        super.setJMenuBar(menuBar);
        validate();
    }

    public void setJMenuBar(String[][] menuItemNames, final Object handler, Method onException, Method... actions) {
        setJMenuBar(GraphicUtils.createMenuBar(menuItemNames, handler, onException, actions));
    }

    public void centerOnScreen() {
        GraphicUtils.centerOnScreen(this);
    }

}
