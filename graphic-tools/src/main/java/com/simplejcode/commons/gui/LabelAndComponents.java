package com.simplejcode.commons.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LabelAndComponents<T extends JComponent> extends JPanel {

    private static final int DEFAULT_MARGIN = 5;


    private JLabel label;

    private T[] components;

    @SafeVarargs
    public LabelAndComponents(String text, Color bgColor, T... components) {
        this(text, bgColor, null, DEFAULT_MARGIN, components);
    }

    @SafeVarargs
    public LabelAndComponents(String text, Color bgColor, int margin, T... components) {
        this(text, bgColor, null, margin, components);
    }

    @SafeVarargs
    public LabelAndComponents(String text, Color bgColor, ImageIcon image, T... components) {
        this(text, bgColor, image, DEFAULT_MARGIN, components);
    }

    @SafeVarargs
    public LabelAndComponents(String text, Color bgColor, ImageIcon image, int margin, T... components) {
        this.label = new JLabel(text, image, SwingConstants.RIGHT);
        this.components = components;

        Box box = new Box(BoxLayout.X_AXIS);
        for (T component : components) {
            if (component != components[0]) {
                box.add(Box.createHorizontalStrut(5));
            }
            box.add(component);
        }

        this.setBackground(bgColor);
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(margin, 2 * margin, margin, 2 * margin));
        this.add(this.label, BorderLayout.WEST);
        this.add(Box.createHorizontalStrut(20));
        this.add(box, BorderLayout.EAST);
    }

    public JLabel getLabel() {
        return label;
    }

    public T getComp() {
        return getComp(0);
    }

    public T getComp(int index) {
        return components[index];
    }

}
