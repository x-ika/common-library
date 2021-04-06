package com.simplejcode.commons.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ColorChooser extends JFrame implements ActionListener {

    public static Color color = new Color(238, 238, 238);

    private final String OK = "Set", CANCEL = "Cancel", DEFAULT = "Default";
    private final JColorChooser[] ch = new JColorChooser[5];
    private final ChatPanel chatWindow;
    private final JTabbedPane panel;

    public ColorChooser(ChatPanel u) {
        super("Choose Color");
        chatWindow = u;
        panel = new JTabbedPane(JTabbedPane.TOP);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < ch.length; i++) {
            ch[i] = new JColorChooser(color);
        }
        panel.add("Border", ch[0]);
        panel.add("Inner Border", ch[1]);
        panel.add("Around Buttons", ch[2]);
        panel.add("Upper Area", ch[3]);
        panel.add("Lower Area", ch[4]);
        JButton ok = new JButton(OK);
        ok.addActionListener(this);
        JButton cancel = new JButton(CANCEL);
        cancel.addActionListener(this);
        JButton def = new JButton(DEFAULT);
        def.addActionListener(this);
        Box box = Box.createHorizontalBox();
        box.setBorder(new EmptyBorder(0, 0, 10, 0));
        box.add(Box.createHorizontalStrut(80));
        box.add(ok);
        box.add(Box.createHorizontalStrut(40));
        box.add(def);
        box.add(Box.createHorizontalStrut(40));
        box.add(cancel);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(box, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ev) {
        String comand = ev.getActionCommand();
        if (comand.equals(OK)) {
            chatWindow.setBackgroundOn(panel.getSelectedIndex(), ch[panel.getSelectedIndex()].getColor());
        } else if (comand.equals(CANCEL)) {
            this.dispose();
        } else if (comand.equals(DEFAULT)) {
            int index = panel.getSelectedIndex();
            if (index > 2) {
                ch[index].setColor(new Color(255, 255, 200));
            } else {
                ch[index].setColor(new Color(238, 238, 238));
            }
        }
    }

}
