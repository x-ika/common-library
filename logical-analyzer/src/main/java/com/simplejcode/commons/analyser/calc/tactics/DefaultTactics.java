package com.simplejcode.commons.analyser.calc.tactics;

import com.simplejcode.commons.analyser.calc.*;
import com.simplejcode.commons.analyser.lang.formula.*;

import java.util.*;

public class DefaultTactics implements Tactics {
    protected LogicalSymbolFormula formula;

    public Code getCodeFor(Sequent sequent) {
        if (findFormulaIn(sequent.getLeft())) {
            return new Code(formula, true);
        }
        if (findFormulaIn(sequent.getRight())) {
            return new Code(formula, false);
        }
        throw new RuntimeException("Can't find deduction!");
    }

    private boolean findFormulaIn(Collection<Formula> collection) {
        for (Formula f : collection) {
            if (f instanceof LogicalSymbolFormula) {
                formula = (LogicalSymbolFormula) f;
                return true;
            }
        }
        return false;
    }

    public Collection<Code> getAllCodesFor(Sequent sequent) {
        ArrayList<Code> codes = new ArrayList<>();
        codes.addAll(getCodesFrom(sequent.getLeft(), true));
        codes.addAll(getCodesFrom(sequent.getRight(), false));
        return codes;
    }

    private Collection<Code> getCodesFrom(Collection<Formula> collection, boolean isLeft) {
        ArrayList<Code> codes = new ArrayList<>();
        for (Formula f : collection) {
            if (f instanceof LogicalSymbolFormula) {
                codes.add(new Code((LogicalSymbolFormula) f, isLeft));
            }
        }
        return codes;
    }
}
