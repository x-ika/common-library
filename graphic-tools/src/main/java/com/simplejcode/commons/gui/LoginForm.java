package com.simplejcode.commons.gui;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField host;
    private JTextField name;
    private JTextField password;

    public LoginForm(Action okAction, Action cancelAction) {
        super("Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addPanel("Server Address", host = new JTextField(20));
        addPanel("Login", name = new JTextField(20));
        addPanel("Password", password = new JPasswordField(20));

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel buttons = new JPanel();
        buttons.add(new JButton(okAction));
        buttons.add(new JButton(cancelAction));
        getContentPane().add(buttons);

        pack();
        GraphicUtils.centerOnScreen(this);
        setVisible(true);
    }

    private void addPanel(String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setDisplayedMnemonic(labelText.charAt(0));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(label);
        panel.add(field);
        getContentPane().add(panel);
    }

    public String getServerAddress() {
        return host.getText();
    }

    public String getLogin() {
        return name.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public void setServerAddress(String address) {
        host.setText(address);
    }

    public void setLogin(String login) {
        name.setText(login);
    }

    public void setPassword(String pass) {
        password.setText(pass);
    }

}
