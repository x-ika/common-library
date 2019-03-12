package com.simplejcode.commons.analyser;

import com.simplejcode.commons.analyser.lang.Token;

public class Lexer {
    private int oldPosition;

    private int position;

    private CharSequence line;

    private String currentName;

    private Token currentToken;

    private static boolean isSymbol(char ch) {
        return (ch >= 'a' && ch <= 'z') ||
                (ch >= 'A' && ch <= 'Z') ||
                ch == '_';
    }

    private static boolean isSymbolOrNumeral(char ch) {
        return isSymbol(ch) || (ch >= '0' && ch <= '9');
    }

    public synchronized void setLine(String line) {
        oldPosition = position = 0;
        this.line = line + Token.END_OF_LINE;
    }

    public synchronized int currentPosition() {
        return position;
    }

    public synchronized String currentName() {
        return currentName;
    }

    public synchronized Token currentToken() {
        return currentToken;
    }

    public synchronized void back() {
        position = oldPosition;
    }

    public synchronized Token nextToken() {
        oldPosition = position;
        char ch;
        //noinspection StatementWithEmptyBody
        while ((ch = nextChar()) == ' ') ;
        String begin = String.valueOf(ch);
        if (Token.forBeginOfValue(begin) != null) {
            return readToken(begin);
        }
        if (isSymbol(ch)) {
            return readName(begin);
        }
        throw new RuntimeException("Unknown symbol: '" + ch + "'");
    }

    private Token readToken(String begin) {
        while ((currentToken = Token.forValue(begin)) == null) {
            begin += nextChar();
            if (Token.forBeginOfValue(begin) == null) {
                throw new RuntimeException("Unknown token: '" + begin + "'");
            }
        }
        return currentToken;
    }

    private Token readName(String begin) {
        char ch;
        while (isSymbolOrNumeral(ch = nextChar())) {
            begin += ch;
        }
        position--;
        currentName = begin;
        return currentToken = Token.VARIABLE;
    }

    private char nextChar() {
        return line.charAt(position++);
    }
}
