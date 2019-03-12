package com.simplejcode.commons.analyser.lang.formula;

import com.simplejcode.commons.analyser.lang.Token;

public abstract class LogicalSymbolFormula implements Formula {
    protected Token value;

    protected LogicalSymbolFormula(Token token) {
        value = token;
    }

    public Token getValue() {
        return value;
    }

    public void setValue(Token token) {
        value = token;
    }
}
