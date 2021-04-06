package com.simplejcode.commons.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class JButtons extends JPanel {

    private final JButton[] buttons;

    public JButtons(ActionListener al, Color bgColor, String... texts) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBackground(bgColor);

        buttons = new JButton[texts.length];
        for (int i = 0; i < texts.length; i++) {
            buttons[i] = new JButton(texts[i]);
            buttons[i].addActionListener(al);
            panel.add(buttons[i]);
        }

        setBackground(bgColor);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new EmptyBorder(5, 10, 5, 10));
        for (int i = 0; i < 10; i++) {
            add(Box.createHorizontalGlue());
        }
        add(panel);
        for (int i = 0; i < 10; i++) {
            add(Box.createHorizontalGlue());
        }
    }

    public JButtons(ActionListener al, String... texts) {
        this(al, Color.white, texts);
    }

    public JButtons(ActionListener al) {
        this(al, Color.white, "Ok", "Cancel");
    }

    public JButton get(int index) {
        return buttons[index];
    }

}
