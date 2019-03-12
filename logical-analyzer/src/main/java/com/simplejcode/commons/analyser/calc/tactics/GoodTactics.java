package com.simplejcode.commons.analyser.calc.tactics;

import com.simplejcode.commons.analyser.calc.*;
import com.simplejcode.commons.analyser.lang.Token;
import com.simplejcode.commons.analyser.lang.formula.*;

import java.util.Collection;

public class GoodTactics extends DefaultTactics {
    public Code getCodeFor(Sequent sequent) {
        /*
              neg
        left  Conj
        right Disj
        right Impl
        */
        if (findIn(sequent.getLeft(), Token.NEGATION)) {
            return new Code(formula, true);
        }
        if (findIn(sequent.getLeft(), Token.CONJUCTION)) {
            return new Code(formula, true);
        }
        if (findIn(sequent.getRight(), Token.NEGATION)) {
            return new Code(formula, false);
        }
        if (findIn(sequent.getRight(), Token.DISJUNCTION)) {
            return new Code(formula, false);
        }
        if (findIn(sequent.getRight(), Token.IMPLICATION)) {
            return new Code(formula, false);
        }
        return super.getCodeFor(sequent);
    }

    protected boolean findIn(Collection<Formula> list, Token token) {
        for (Formula f : list) {
            if (f instanceof LogicalSymbolFormula) {
                formula = (LogicalSymbolFormula) f;
                if (formula.getValue() == token) {
                    return true;
                }
            }
        }
        return false;
    }
}
