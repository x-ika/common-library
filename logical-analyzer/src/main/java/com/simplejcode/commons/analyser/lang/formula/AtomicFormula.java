package com.simplejcode.commons.analyser.lang.formula;

import com.simplejcode.commons.analyser.lang.Token;

public abstract class AtomicFormula implements Formula {
    protected String name;

    protected AtomicFormula(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public String withBrackets() {
        return Token.inTheBrackets(name);
    }

    public String withoutBrackets(int parentPriority) {
        return name;
    }
}
