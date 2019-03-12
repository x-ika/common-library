package com.simplejcode.commons.analyser.calc;

import com.simplejcode.commons.analyser.lang.Token;
import com.simplejcode.commons.analyser.lang.formula.*;
import com.simplejcode.commons.analyser.lang.formula.logsymbol.ConnectiveFormula;

import java.util.*;

@SuppressWarnings("unchecked")
public class Code {
    private boolean isLeft;

    private String name;

    private LogicalSymbolFormula formula;

    public Code(LogicalSymbolFormula lsFormula, boolean isLeftCode) {
        formula = lsFormula;
        isLeft = isLeftCode;
        name = isLeft ? formula.getValue() + "->" : "->" + formula.getValue();
        name = Token.inTheBrackets(name);
    }

    public String toString() {
        return name;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public Formula getFormula() {
        return formula;
    }

    public Collection<Sequent> makeDeduction(Sequent s) {
        ArrayList<Formula> left = new ArrayList<>(s.getLeft());
        ArrayList<Formula> right = new ArrayList<>(s.getRight());
        ConnectiveFormula f = (ConnectiveFormula) formula;
        if (isLeft) {
            removeFrom(left, f);
            switch (f.getValue()) {
                case NEGATION:
                    return get(left, addAll(right, f.getMember(0)));
                case CONJUCTION:
                    return get(addAll(left, f.members()), right);
                case DISJUNCTION:
                    return get(addAll(left, f.getMember(0)), right,
                            addAll(right, f.getMember(1)), right);
                case IMPLICATION:
                    return get(left, addAll(right, f.getMember(0)),
                            addAll(left, f.getMember(1)), right);
                case EQUIVALENCE:
                    return get(left, addAll(right, f.members()),
                            addAll(left, f.members()), right);
                case EXISTENCE:
                case FOR_EITHER:
            }
        } else {
            removeFrom(right, f);
            switch (f.getValue()) {
                case NEGATION:
                    return get(addAll(left, f.getMember(0)), right);
                case CONJUCTION:
                    return get(left, addAll(right, f.getMember(0)),
                            left, addAll(right, f.getMember(1)));
                case DISJUNCTION:
                    return get(left, addAll(right, f.members()));
                case IMPLICATION:
                    return get(addAll(left, f.getMember(0)),
                            addAll(right, f.getMember(1)));
                case EQUIVALENCE:
                    return get(addAll(left, f.getMember(0)),
                            addAll(right, f.getMember(1)),
                            addAll(left, f.getMember(1)),
                            addAll(right, f.getMember(0)));
                case EXISTENCE:
                case FOR_EITHER:
            }
        }
        throw new RuntimeException("Error in Tactics!");
    }

    private Collection<Sequent> get(Collection<Formula>... collections) {
        ArrayList<Sequent> ArrayList = new ArrayList<>();
        for (int i = 0; i < collections.length; i++) {
            ArrayList.add(new Sequent(collections[i], collections[++i]));
        }
        return ArrayList;
    }

    private Collection<Formula> addAll(Collection<Formula> collection, Formula... formulas) {
        ArrayList<Formula> arrayList = new ArrayList<>(collection);
        Collections.addAll(arrayList, formulas);
        return arrayList;
    }

    private void removeFrom(ArrayList list, Object obj) {
        for (int i = 0; i < list.size(); i++) {
            if (obj == list.get(i)) {
                list.remove(i);
            }
        }
    }
}
