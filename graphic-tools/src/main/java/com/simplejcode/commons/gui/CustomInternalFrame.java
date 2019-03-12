package com.simplejcode.commons.gui;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

public class CustomInternalFrame extends JInternalFrame {

    public CustomInternalFrame(String title) {
        this(title, null);
    }

    public CustomInternalFrame(String title, Container contentPane) {
        super(title, true, true, true, true);
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        setBackground(new Color(0x1E90FF));
        if (contentPane != null) {
            loadContent(contentPane);
        }
        setVisible(true);
    }

    public void loadContent(Container contentPane) {
        boolean icon = isIcon;
        setContentPane(contentPane);
        pack();
        setMinimumSize(getSize());
        GraphicUtils.centerOnParent(this);
        setIcon(icon);
    }

    public void setIcon(boolean b) {
        try {
            super.setIcon(b);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

}
