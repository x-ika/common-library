package com.simplejcode.commons.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class FieldAndButton extends JPanel {

    private final JTextField field;

    private final JButton button;

    public FieldAndButton(ActionListener al, int columns, String text, Color bgColor) {
        field = new JTextField(columns);
        button = new JButton(text);
        button.addActionListener(al);
        setBackground(bgColor);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 10, 5, 10));
        add(field, BorderLayout.WEST);
        add(Box.createHorizontalStrut(10));
        add(button, BorderLayout.EAST);
    }

    public JTextField getField() {
        return field;
    }

    public JButton getButton() {
        return button;
    }

    public String getText() {
        return field.getText();
    }

    public void setText(String text) {
        field.setText(text);
    }

}
