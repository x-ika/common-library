package com.simplejcode.commons.analyser.lang.formula.atomic;

import com.simplejcode.commons.analyser.lang.Token;
import com.simplejcode.commons.analyser.lang.formula.AtomicFormula;

import java.util.Vector;

public class FunctionalVariable extends AtomicFormula {
    private Vector<AtomicFormula> atomicFormulas;

    public FunctionalVariable(String name) {
        super(name);
        atomicFormulas = new Vector<>();
    }

    public Vector<AtomicFormula> terms() {
        return atomicFormulas;
    }

    public void addTerm(AtomicFormula atomicFormula) {
        atomicFormulas.add(atomicFormula);
    }

    public String toString() {
        String line = atomicFormulas.firstElement().toString();
        for (int i = 1; i < atomicFormulas.size(); i++) {
            line += ", " + atomicFormulas.get(i);
        }
        return name + Token.inTheBrackets(line);
    }

    public String withBrackets() {
        String line = "";
        for (AtomicFormula t : atomicFormulas) {
            line += ", " + t.withBrackets();
        }
        line = Token.inTheBrackets(line.substring(2));
        return name + line;
    }

    public String withoutBrackets(int parentPriority) {
        String line = "";
        for (AtomicFormula t : atomicFormulas) {
            line += ", " + t.withoutBrackets(0);
        }
        line = Token.inTheBrackets(line.substring(2));
        return name + line;
    }
}
