package com.simplejcode.commons.analyser.lang.formula.atomic;

import com.simplejcode.commons.analyser.lang.formula.AtomicFormula;

public class Variable extends AtomicFormula {
    public Variable(String name) {
        super(name);
    }

    public String toString() {
        return name;
    }

    public String withBrackets() {
        return name;
    }

    public String withoutBrackets(int parentPriority) {
        return name;
    }
}
