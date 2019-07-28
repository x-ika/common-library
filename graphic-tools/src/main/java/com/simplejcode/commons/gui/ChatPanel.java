package com.simplejcode.commons.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;

public class ChatPanel extends JPanel {

    private JButton button1, button2;

    private JComponent southPanel, southEastPanel;
    private JTextPane upperTextArea, lowerTextArea;

    private Document doc;
    private Style style;

    public ChatPanel() {
        upperTextArea = new JTextPane();
        upperTextArea.setPreferredSize(new Dimension(600, 300));
        upperTextArea.setBackground(new Color(255, 255, 200));
        doc = upperTextArea.getStyledDocument();
        style = upperTextArea.addStyle("", null);
        StyleConstants.setFontFamily(style, "Serif");
        upperTextArea.setEditable(false);
        lowerTextArea = new JTextPane();
        lowerTextArea.setBackground(new Color(255, 255, 200));
        button1 = new JButton();
        button2 = new JButton();

        southEastPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        southEastPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        southEastPanel.add(button1);
        southEastPanel.add(button2);

        southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        southPanel.add(getScrollPane(lowerTextArea));
        southPanel.add(southEastPanel, BorderLayout.EAST);
        southPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, southPanel.getPreferredSize().height));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        add(getScrollPane(upperTextArea));
        add(southPanel);

        southPanel.setOpaque(false);
        southEastPanel.setOpaque(false);
    }

    public JButton getAttachFileButton() {
        return button1;
    }

    public JButton getSendMessageButton() {
        return button2;
    }

    private JScrollPane getScrollPane(Component c) {
        return new JScrollPane(c, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public String getInputText() {
        return lowerTextArea.getText();
    }

    public void setInputText(String text) {
        lowerTextArea.setText(text);
    }

    public void writeText(String message, Color color) {
        EventQueue.invokeLater(() -> {
            try {
//                    StyleConstants.setForeground(style, color);
//                    StyleConstants.setFontSize(style, 15);
//                    StyleConstants.setBold(style, true);
//                    doc.insertString(doc.getLength(), convert(message.getSendTime()) + ":\n", style);

                StyleConstants.setForeground(style, color.brighter());
                StyleConstants.setFontSize(style, 15);
                StyleConstants.setBold(style, false);
                doc.insertString(doc.getLength(), message, style);
                JScrollBar scrollBar = ((JScrollPane) upperTextArea.getParent().getParent()).getVerticalScrollBar();
                scrollBar.setValue(100000);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    public void setBackgroundOn(int index, Color bg) {
        switch (index) {
            case 0:
                setBackground(bg);
                break;
            case 1:
                southPanel.setBackground(bg);
                break;
            case 2:
                southEastPanel.setBackground(bg);
                break;
            case 3:
                upperTextArea.setBackground(bg);
                break;
            case 4:
                lowerTextArea.setBackground(bg);
                break;
        }
    }

    public void clearHistory() {
        upperTextArea.setText("");
    }

    public void sendFile(File file) {
//        message.setBytes(buff);
//        message.setFile(file);
    }

    //-----------------------------------------------------------------------------------

    private static String convert(long date) {
        return new SimpleDateFormat().format(new java.util.Date(date));
    }
}
