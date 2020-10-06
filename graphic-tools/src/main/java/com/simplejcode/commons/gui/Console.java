package com.simplejcode.commons.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class Console extends JPanel implements ActionListener, KeyListener {

    private class NoWrapEditorKit extends StyledEditorKit {
        public ViewFactory getViewFactory() {
            return new ViewFactory() {
                public View create(Element elem) {
                    String kind = elem.getName();
                    if (kind != null) {
                        if (kind.equals(AbstractDocument.ContentElementName)) {
                            return new LabelView(elem);
                        } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                            return new ParagraphView(elem);
                        } else if (kind.equals(AbstractDocument.SectionElementName)) {
                            return new BoxView(elem, View.Y_AXIS) {
                                public void layout(int width, int height) {
                                    super.layout(Character.MAX_VALUE, height);
                                }

                                public float getMinimumSpan(int axis) {
                                    if (axis == View.Y_AXIS) {
                                        return super.getMinimumSpan(axis);
                                    } else {
                                        return super.getPreferredSpan(axis);
                                    }
                                }
                            };
                        } else if (kind.equals(StyleConstants.ComponentElementName)) {
                            return new ComponentView(elem);
                        } else if (kind.equals(StyleConstants.IconElementName)) {
                            return new IconView(elem);
                        }
                    }
                    // default to text display
                    return new LabelView(elem);
                }
            };
        }
    }

    private final boolean inputAllowed;

    private Window parent;
    private JTextField textField;
    private JTextPane textArea;
    private StyledDocument doc;
    private Style style;
    private JScrollPane scrollPane;

    private final Object lock = new Object();


    public Console() {
        this(null, true);
    }

    public Console(Window parent) {
        this(parent, true);
    }

    public Console(Window parent, boolean inputAllowed) {
        this.parent = parent;
        this.inputAllowed = inputAllowed;
        init(inputAllowed);
        textField.requestFocus();
    }

    private void init(boolean inputAllowed) {

        textArea = new JTextPane();
        textArea.setEditorKit(new NoWrapEditorKit());
        doc = textArea.getStyledDocument();
        style = doc.addStyle("", null);
        textArea.setEditable(false);

        textField = new JTextField(60);
        textField.addKeyListener(this);
        textField.setMinimumSize(new Dimension(0, textField.getHeight()));
        textField.setMaximumSize(new Dimension(1024, textField.getHeight()));

        scrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(650, 300));

        JButtons button = new JButtons(this, getBackground(), "Add Line");
        button.setMinimumSize(new Dimension(0, getHeight()));
        button.setMaximumSize(new Dimension(1024, getHeight()));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(scrollPane);

        if (inputAllowed) {
            add(textField);
            add(button);
        }

    }

    //-----------------------------------------------------------------------------------
    /*
    Public
     */

    public String readLine() {
        checkInputAllowed();
        try {
            while (true) {
                synchronized (lock) {
                    lock.wait();
                }
                String line = textField.getText() == null ? "" : textField.getText().trim();
                if (!line.isEmpty()) {
                    textField.setText("");
                    appendToTextArea(line + '\n');
                    return line;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Problem");
        }
    }

    public void writeLine(Object o) {
        appendToTextArea(o + "\n");
    }

    private void textEntered() {
        synchronized (lock) {
            lock.notify();
        }
    }

    private void appendToTextArea(String line) {
        if (EventQueue.isDispatchThread()) {
            doAppend(line);
        } else {
            try {
                EventQueue.invokeAndWait(() -> doAppend(line));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doAppend(String line) {
        try {
            doc.insertString(doc.getLength(), line, style);
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------------------

    public void setInputText(String text) {
        checkInputAllowed();
        textField.setText(text);
    }

    public void setColor(Color color) {
        StyleConstants.setForeground(style, color);
    }

    public void setFont(String fontName, int fontStyle, int fontSize) {
        StyleConstants.setFontFamily(style, fontName);
        StyleConstants.setBold(style, fontStyle == Font.BOLD);
        StyleConstants.setItalic(style, fontStyle == Font.ITALIC);
        StyleConstants.setFontSize(style, fontSize);
    }

    public void clear() {
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void dispose() {
        if (parent != null) {
            parent.dispose();
        }
    }

    //-----------------------------------------------------------------------------------

    private void checkInputAllowed() {
        if (!inputAllowed) {
            throw new UnsupportedOperationException("Input disallowed!");
        }
    }

    //-----------------------------------------------------------------------------------
    /*
    Event Handling
     */

    public void actionPerformed(ActionEvent e) {
        textEntered();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            textEntered();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    //-----------------------------------------------------------------------------------
    /*
    Static
     */

    public static Console createInstance() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Console");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Console console = new Console();
        console.parent = frame;
        frame.setContentPane(console);
        frame.pack();
        JFrame.setDefaultLookAndFeelDecorated(false);
        GraphicUtils.centerOnScreen(frame);
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);
        return console;
    }

    public static String readString(String message) {
        return readString(message, "");
    }

    public static String readString(String message, String def) {
        Console console = createInstance();
        console.writeLine(message);
        console.setInputText(def);
        String s = console.readLine();
        console.dispose();
        return s;
    }

}
