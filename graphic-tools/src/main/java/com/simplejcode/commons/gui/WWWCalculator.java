package com.simplejcode.commons.gui;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class WWWCalculator extends Applet implements Runnable, KeyListener {
    private final int ENTER = 10;
    private TextField firstArg, secondArg, answer, operation;
    private String movingString = "Calculator..............................";
    private Boolean stopFlag;

    public void init() {
        setBackground(Color.white);
        addKeyListener(this);
        firstArg = new TextField("First argument", 30);
        firstArg.addKeyListener(this);
        operation = new TextField("Operatoin", 10);
        operation.addKeyListener(this);
        secondArg = new TextField("Second argument", 30);
        secondArg.addKeyListener(this);
        answer = new TextField("Answer", 30);
        answer.setEnabled(false);
        answer.setFont(new Font("Serif", Font.BOLD, 15));
        answer.setBackground(Color.yellow);
        answer.addKeyListener(this);
        add(firstArg);
        add(operation);
        add(secondArg);
        add(answer);
    }

    public void start() {
        stopFlag = false;
        new Thread(this).start();
    }

    public void run() {
        char ch;
        while (!stopFlag) {
            try {
                repaint();
                ch = movingString.charAt(0);
                movingString = movingString.substring(1, movingString.length()) + ch;
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        stopFlag = true;
    }

    public void paint(Graphics g) {
        g.setFont(new Font("Serif", Font.BOLD, 20));
        g.drawString(movingString, 30, 160);
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == ENTER) {
            answer.setText(result(firstArg.getText(), secondArg.getText(), operation.getText().charAt(0)));
        } else if (code == ' ') {
            firstArg.setText("");
            secondArg.setText("");
            operation.setText("");
        }
    }

    private String result(String arg1, String arg2, char operation) {
        Double result;
        Double darg1 = Double.parseDouble(arg1);
        Double darg2 = Double.parseDouble(arg2);
        switch (operation) {
            case '*':
                result = darg1 * darg2;
                break;
            case '/':
                result = darg1 / darg2;
                break;
            case '+':
                result = darg1 + darg2;
                break;
            case '-':
                result = darg1 - darg2;
                break;
            default:
                return "Unknown operation";
        }
        return String.valueOf(result);
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
