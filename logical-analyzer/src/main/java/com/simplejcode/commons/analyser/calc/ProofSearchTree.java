package com.simplejcode.commons.analyser.calc;

import java.util.Vector;

public class ProofSearchTree {
    private int depth;

    private String comment;

    private Sequent value;

    private Vector<ProofSearchTree> children;

    public ProofSearchTree(Sequent sequent, int depth, String comment) {
        children = new Vector<>();
        this.depth = depth;
        this.comment = comment;
        value = sequent;
    }

    public ProofSearchTree(Sequent sequent, int depth) {
        this(sequent, depth, null);
    }

    public String toString() {
        return "(" + depth + ") " + value + "   " + comment;
    }

    public Vector<ProofSearchTree> children() {
        return children;
    }

    public int length() {
        int size = 1;
        for (ProofSearchTree d : children) {
            size += d.length();
        }
        return size;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Sequent getValue() {
        return value;
    }

    public void setValue(Sequent value) {
        this.value = value;
    }

    public void addChild(ProofSearchTree proofSearchTree) {
        children.add(proofSearchTree);
    }

    public boolean isright() {
        if (children().isEmpty()) {
            return value.isAxiom();
        }
        for (ProofSearchTree child : children) {
            if (!child.isright()) {
                return false;
            }
        }
        return true;
    }
}
