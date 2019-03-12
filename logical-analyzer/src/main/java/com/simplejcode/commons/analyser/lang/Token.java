package com.simplejcode.commons.analyser.lang;

public enum Token {
    EQUIVALENCE("<=>", 0),
    IMPLICATION("=>", 1),
    DISJUNCTION("|", 2),
    CONJUCTION("&", 3),
    NEGATION("~", 4),
    EXISTENCE("#E", 4),
    FOR_EITHER("#A", 4),
    LEFT_BRACKET("("),
    RIGHT_BRACKET(")"),
    COMMA(","),
    VARIABLE("{variable}"),
    END_OF_LINE("{END}");

    private int priority;

    private String value;

    private Token(String s, int p) {
        priority = p;
        value = s;
    }

    private Token(String s) {
        this(s, 0);
    }

    public int getPriority() {
        return priority;
    }

    public String toString() {
        return value;
    }

    public static Token forValue(String value) {
        for (Token token : values()) {
            if (token.toString().equals(value)) {
                return token;
            }
        }
        return null;
    }

    public static Token forBeginOfValue(String value) {
        for (Token token : values()) {
            if (token.toString().startsWith(value)) {
                return token;
            }
        }
        return null;
    }

    public static String inTheBrackets(String line) {
        return LEFT_BRACKET + line + RIGHT_BRACKET;
    }
}
