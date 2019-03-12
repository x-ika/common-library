package com.simplejcode.commons.analyser.calc.tactics;

import com.simplejcode.commons.analyser.calc.*;

import java.util.Collection;

public class BestTactics extends DefaultTactics {
    private static final int MIN_WEIGHT = -1000;

    private static final int WEIGHT_OF_AXIOM = 1;

    private static final int WEIGHT_OF_NON_AXIOM = 10;

    private int intellect;

    private Code bestCode;

    public BestTactics(int intellect) {
        this.intellect = intellect;
    }

    public Code getCodeFor(Sequent sequent) {
        minWeightOf(sequent, 0);
        return bestCode;
    }

    private int minWeightOf(Sequent sequent, int depth) {
        if (sequent.isAxiom()) {
            return WEIGHT_OF_AXIOM;
        }
        if (depth > intellect) {
            return WEIGHT_OF_NON_AXIOM;
        }
        Collection<Code> codes = getAllCodesFor(sequent);
        if (codes.isEmpty()) {
            bestCode = null;
            return MIN_WEIGHT;
        }
        int minSize = Short.MAX_VALUE;
        M:
        for (Code code : codes) {
            int size = 1;
            for (Sequent s : code.makeDeduction(sequent)) {
                size += minWeightOf(s, depth + 1);
                if (minSize <= size) {
                    continue M;
                }
            }
            minSize = size;
            if (depth == 0)
                bestCode = code;
        }
        return minSize;
    }
}
