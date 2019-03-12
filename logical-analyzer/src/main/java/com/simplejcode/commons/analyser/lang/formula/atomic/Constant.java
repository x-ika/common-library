package com.simplejcode.commons.analyser.lang.formula.atomic;

import com.simplejcode.commons.analyser.lang.formula.AtomicFormula;

public class Constant extends AtomicFormula {
    private boolean value;

    public Constant(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public boolean booleanValue() {
        return value;
    }
}
