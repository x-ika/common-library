package com.simplejcode.commons.gui;

import javax.swing.*;
import java.awt.*;

public class LabelAndField extends LabelAndComponents<JTextField> {

    public LabelAndField(String text, Color bgColor, int columns) {
        this(text, bgColor, null, columns, false);
    }

    public LabelAndField(String text, Color bgColor, int columns, boolean passwordFlag) {
        this(text, bgColor, null, columns, passwordFlag);
    }

    public LabelAndField(String text, Color bgColor, ImageIcon image, int columns, boolean passwordFlag) {
        super(text, bgColor, image, passwordFlag ? new JPasswordField(columns) : new JTextField(columns));
    }

    public LabelAndField(String text, ImageIcon image, int columns, boolean passwordFlag) {
        this(text, Color.white, image, columns, passwordFlag);
    }


    public String getText() {
        return getComp().getText();
    }

    public void setText(String text) {
        getComp().setText(text);
    }

}
