package com.simplejcode.commons.analyser.calc;

import com.simplejcode.commons.analyser.calc.tactics.BestTactics;
import com.simplejcode.commons.analyser.lang.formula.Formula;
import com.simplejcode.commons.analyser.lang.formula.atomic.Constant;

import java.util.*;

public class Sequent {
    private static Tactics tactics;

    private Collection<Formula> left;

    private Collection<Formula> right;

    public Sequent(Collection<Formula> l, Collection<Formula> r) {
        left = l;
        right = r;
    }

    public Sequent(Formula formula) {
        this(new ArrayList<>(), new ArrayList<>());
        right.add(formula);
    }

    public String toString() {
        String line = "";
        for (Formula f : left) {
            line += f + ", ";
        }
        if (!left.isEmpty()) {
            line = line.substring(0, line.length() - 2);
        }
        line += " --> ";
        for (Formula f : right) {
            line += f + ", ";
        }
        if (!right.isEmpty()) {
            line = line.substring(0, line.length() - 2);
        }
        return line;
    }

    public Collection<Formula> getLeft() {
        return left;
    }

    public Collection<Formula> getRight() {
        return right;
    }

    public ProofSearchTree getProofSearchTree() {
        return getProofSearchTree(new BestTactics(1));
    }

    public ProofSearchTree getProofSearchTree(Tactics tactics) {
        Sequent.tactics = tactics;
        return proofSearchTree(0);
    }

    public boolean isAxiom() {
        for (Formula f : left) {
            if (f instanceof Constant && !((Constant) f).booleanValue()) {
                return true;
            }
        }
        for (Formula f : right) {
            if (f instanceof Constant && ((Constant) f).booleanValue()) {
                return true;
            }
            for (Formula formula : left) {
                if (formula.toString().equals(f.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    private ProofSearchTree proofSearchTree(int depth) {
        ProofSearchTree proofSearchTree = new ProofSearchTree(this, depth, "Axiom");
        if (isAxiom()) {
            return proofSearchTree;
        }
        Code code = tactics.getCodeFor(this);
        if (code == null) {
            proofSearchTree.setComment("Not Axiom");
            return proofSearchTree;
        }
        Object[] sequents = code.makeDeduction(this).toArray();
        for (int i = 0; i < sequents.length; i++) {
            proofSearchTree.addChild(((Sequent) sequents[i]).proofSearchTree(depth + 1));
            if (!proofSearchTree.isright()) {
                for (int j = i + 1; j < sequents.length; j++) {
                    proofSearchTree.addChild(new ProofSearchTree((Sequent) sequents[j], depth + 1, ""));
                }
                break;
            }
        }
        proofSearchTree.setComment("by " + code);
        return proofSearchTree;
    }
}
