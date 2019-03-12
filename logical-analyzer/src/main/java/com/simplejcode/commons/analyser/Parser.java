package com.simplejcode.commons.analyser;

import com.simplejcode.commons.analyser.lang.Token;
import com.simplejcode.commons.analyser.lang.formula.*;
import com.simplejcode.commons.analyser.lang.formula.atomic.*;
import com.simplejcode.commons.analyser.lang.formula.logsymbol.ConnectiveFormula;

public class Parser {
    private Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public synchronized Formula getFormula() {
        Formula f = formula();
        if (lexer.currentToken() == Token.END_OF_LINE) {
            return f;
        }
        throw new RuntimeException(errMessage("{binary link}"));
    }

    private Formula formula() {
        Formula left = memberOfEquivalence();
        while (true) {
            switch (lexer.currentToken()) {
                case EQUIVALENCE:
                    ConnectiveFormula cf = new ConnectiveFormula(Token.EQUIVALENCE, 2);
                    cf.setMember(0, left);
                    cf.setMember(1, memberOfEquivalence());
                    left = cf;
                    break;
                default:
                    return left;
            }
        }
    }

    private Formula memberOfEquivalence() {
        Formula left = memberOfImplication();
        while (true) {
            switch (lexer.currentToken()) {
                case IMPLICATION:
                    ConnectiveFormula cf = new ConnectiveFormula(Token.IMPLICATION, 2);
                    cf.setMember(0, left);
                    cf.setMember(1, memberOfImplication());
                    left = cf;
                    break;
                default:
                    return left;
            }
        }
    }

    private Formula memberOfImplication() {
        Formula left = memberOfDisjunction();
        while (true) {
            switch (lexer.currentToken()) {
                case DISJUNCTION:
                    ConnectiveFormula cf = new ConnectiveFormula(Token.DISJUNCTION, 2);
                    cf.setMember(0, left);
                    cf.setMember(1, memberOfDisjunction());
                    left = cf;
                    break;
                default:
                    return left;
            }
        }
    }

    private Formula memberOfDisjunction() {
        Formula left = memberOfConjunction();
        while (true) {
            switch (lexer.currentToken()) {
                case CONJUCTION:
                    ConnectiveFormula cf = new ConnectiveFormula(Token.CONJUCTION, 2);
                    cf.setMember(0, left);
                    cf.setMember(1, memberOfConjunction());
                    left = cf;
                    break;
                default:
                    return left;
            }
        }
    }

    private Formula memberOfConjunction() {
        switch (lexer.nextToken()) {
            case NEGATION:
                ConnectiveFormula cf = new ConnectiveFormula(Token.NEGATION, 1);
                cf.setMember(0, memberOfConjunction());
                return cf;
                /*
                case EXISTENCE:
                case FOR_EITHER:
                    QuantifierFormula qf = new QuantifierFormula(lexer.currentToken());
                    if(lexer.nextToken() == Token.VARIABLE) {
                        qf.setVariable(new Variable(lexer.currentName()));
                        qf.setFormula(memberOfConjunction());
                        return qf;
                    }
                    throw new RuntimeException(errMessage(Token.VARIABLE.toString()));
                */
            case LEFT_BRACKET:
                Formula f = formula();
                if (lexer.currentToken() == Token.RIGHT_BRACKET) {
                    lexer.nextToken();
                    return f;
                }
                throw new RuntimeException(errMessage(Token.RIGHT_BRACKET.toString()));
            default:
                return atomicFormula();
        }
    }

    private AtomicFormula atomicFormula() {
        switch (lexer.currentToken()) {
            case VARIABLE:
                if (lexer.nextToken() != Token.LEFT_BRACKET) {
                    return getAtomicFormula(lexer.currentName());
                }
                FunctionalVariable pv = new FunctionalVariable(lexer.currentName());
                do {
                    pv.addTerm(term());
                } while (lexer.currentToken() == Token.COMMA);
                if (lexer.currentToken() == Token.RIGHT_BRACKET) {
                    lexer.nextToken();
                    return pv;
                }
                throw new RuntimeException(errMessage(Token.RIGHT_BRACKET.toString()));
            default:
                throw new RuntimeException(errMessage(Token.VARIABLE.toString()));
        }
    }

    private AtomicFormula term() {
        switch (lexer.nextToken()) {
            case VARIABLE:
                if (lexer.nextToken() != Token.LEFT_BRACKET) {
                    return getTerm(lexer.currentName());
                }
                FunctionalVariable fv = new FunctionalVariable(lexer.currentName());
                do {
                    fv.addTerm(term());
                } while (lexer.currentToken() == Token.COMMA);
                if (lexer.currentToken() == Token.RIGHT_BRACKET) {
                    lexer.nextToken();
                    return fv;
                }
                throw new RuntimeException(errMessage(Token.RIGHT_BRACKET.toString()));
            default:
                throw new RuntimeException(errMessage(Token.VARIABLE.toString()));
        }
    }

    private AtomicFormula getAtomicFormula(String name) {
        if (name.equals("T") || name.equals("true")) {
            return new Constant(name, true);
        }
        if (name.equals("F") || name.equals("false")) {
            return new Constant(name, false);
        }
        return new Variable(lexer.currentName());
    }

    private AtomicFormula getTerm(String name) {
        if (name.equals("T") || name.equals("true")) {
            return new Constant(name, true);
        }
        if (name.equals("F") || name.equals("false")) {
            return new Constant(name, false);
        }
        return new Variable(lexer.currentName());
    }

    private String errMessage(String s) {
        return (lexer.currentPosition() + 1) +
                ": Unexpected token: \"" +
                lexer.currentToken() +
                "\"\n   \"" +
                s + "\" expected.";
    }
}
