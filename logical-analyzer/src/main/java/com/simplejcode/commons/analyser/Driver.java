package com.simplejcode.commons.analyser;

import com.simplejcode.commons.analyser.calc.*;
import com.simplejcode.commons.analyser.calc.tactics.BestTactics;
import com.simplejcode.commons.analyser.lang.formula.Formula;

import java.io.*;

public class Driver {
    private static final String PATH = "resources/";

    private static final String TAB = "   ";

    private Lexer lexer;

    private Parser parser;

    private BufferedReader in;

    private PrintStream out;

    public Driver() throws FileNotFoundException {
        lexer = new Lexer();
        parser = new Parser(lexer);
        out = new PrintStream(new FileOutputStream(PATH + "output.txt"));
        in = new BufferedReader(new InputStreamReader(new FileInputStream(PATH + "input.txt")));
    }

    public boolean processNext() throws IOException {
        String line = in.readLine();
        if (line.equals("")) {
            return false;
        }
        lexer.setLine(line);
        Formula formula = parser.getFormula();

        out.println("Formula:");
        out.println(TAB + line);
        out.println(TAB + formula);
        out.println(TAB + formula.withBrackets());
        ProofSearchTree proofSearchTree = new Sequent(formula).getProofSearchTree(new BestTactics(1));
        out.println("ProofSearchTree: " + (proofSearchTree.isright() ? "(YES)" : "(NO)"));
        print(proofSearchTree);
        out.println();
        out.println();
        return true;
    }

    private void print(ProofSearchTree proofSearchTree) {
        for (int i = 0; i < proofSearchTree.getDepth(); i++) {
            out.print(TAB);
        }
        out.println(TAB + proofSearchTree);
        for (ProofSearchTree child : proofSearchTree.children()) {
            print(child);
        }
    }

}
